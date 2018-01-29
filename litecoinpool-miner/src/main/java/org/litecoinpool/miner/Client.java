package org.litecoinpool.miner;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.AUTO_CLOSE_TARGET;
import static com.fasterxml.jackson.databind.node.NullNode.getInstance;
import static com.google.common.collect.Iterables.get;
import static com.google.common.collect.Iterables.getFirst;
import static io.reactivex.Observable.just;
import static io.reactivex.Observable.range;
import static io.reactivex.schedulers.Schedulers.computation;
import static org.litecoinpool.miner.HasherBuilder.aHasher;
import static org.litecoinpool.miner.Nonce.intToHexString;
import static org.litecoinpool.miner.Nonce.max;
import static org.litecoinpool.miner.Nonce.min;
import static org.litecoinpool.miner.Nonce.nonce;
import static org.litecoinpool.miner.TargetMatcher.withDifficulty;
import static org.slf4j.LoggerFactory.getLogger;
import static org.stratum.protocol.StratumMessageBuilder.aStratumMessage;
import static org.stratum.protocol.StratumMethod.MINING_SUBMIT;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.stratum.protocol.StratumMessage;
import org.stratum.protocol.StratumMessageBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class Client {
	private static final Logger LOGGER = getLogger(Client.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private final ObservableSocket socket;
    private final Subject<StratumMessage> messages = PublishSubject.<StratumMessage>create().toSerialized();
    private final AtomicLong id = new AtomicLong(1);
    private TargetMatcher matcher = withDifficulty(1);
    private String extranonce1 = null;
    private String extranonce2 = null;
	
	static {
		MAPPER.configure(AUTO_CLOSE_TARGET, false);
	}

	private Client(ObservableSocket socket) {
		this.socket = socket;
	}
	
	public static Client address(String host, int port) throws IOException {
		return new Client(ObservableSocket.from(new InetSocketAddress(host, port)));
	}
	
	public Client send(StratumMessageBuilder message) throws IOException {
		socket.write(message.withId(id.getAndIncrement()).build());
		return this;
	}

	public void listen() throws IOException {
		messages.flatMap(process(matcher)).map(submit()).subscribe();
		
		socket.read()
        	  .takeWhile(isConnected())
        	  .repeat()
        	  .map(setDifficulty())
        	  .filter(isNotify())
        	  .subscribeWith(messages)
        	  .blockingSubscribe();
	}

	private Function<StratumMessageBuilder,StratumMessageBuilder> submit() {
		return new Function<StratumMessageBuilder,StratumMessageBuilder>() {
			@Override
			public StratumMessageBuilder apply(StratumMessageBuilder message) throws Exception {
				send(message);
				return message;
			}
		};
	}

	private Function<StratumMessage,StratumMessage> setDifficulty() {
		return new Function<StratumMessage,StratumMessage>() {
			@Override
			public StratumMessage apply(StratumMessage message) throws Exception {
				if (message.getMethod().isSetDifficulty()) {
					int difficulty = getFirst(message.getParams(), getInstance()).asInt(1);
					LOGGER.info("difficulty: {}", difficulty);
					matcher = withDifficulty(difficulty);
				}
				else if (message.getMethod().isNull() &&
						 message.getResult() != null && 
						 message.getResult().isArray()) {
					extranonce1 = get(message.getResult(), 1, getInstance()).asText();
					extranonce2 = intToHexString(get(message.getResult(), 1, getInstance()).asInt());
					
					/*for (JsonNode node : message.getResult()) {
						if (node.isArray()) {
							for (JsonNode subNode : node) {
								StratumMethod stratumMethod = stratumMethod(getFirst(subNode, getInstance()).asText());
								LOGGER.info("stratumMethod: {}", stratumMethod);
								if (stratumMethod.isSetDifficulty()) {
									BigInteger difficulty = new BigInteger(getLast(subNode, getInstance()).asText(), 16);
									LOGGER.info("difficulty: {}", difficulty);
									matcher = withDifficulty(difficulty);
								}
							}
						}
					}*/
				}
				
				return message;
			}
		};
	}

	private static Predicate<StratumMessage> isNotify() {
		return new Predicate<StratumMessage>() {
			@Override
			public boolean test(StratumMessage message) throws Exception {
				return message.getMethod().isNotify();
			}
		};
	}

	private static Function<StratumMessage,StratumMessage> log(final String format) {
		return new Function<StratumMessage,StratumMessage>() {
			@Override
			public StratumMessage apply(StratumMessage message) throws Exception {
				LOGGER.info(format, MAPPER.writeValueAsString(message));
				
				return message;
			}
		};
	}
	
	private Function<StratumMessage,ObservableSource<StratumMessageBuilder>> process(final TargetMatcher matcher) {
		return new Function<StratumMessage,ObservableSource<StratumMessageBuilder>>() {
			@Override
			public ObservableSource<StratumMessageBuilder> apply(StratumMessage message) throws Exception {
				return just(message).observeOn(computation())
									.map(hasher())
						  			.flatMap(hash(matcher, message));
			}
		};
	}
	
	private Function<StratumMessage,Hasher> hasher() {
		return new Function<StratumMessage,Hasher>() {
			@Override
			public Hasher apply(StratumMessage message) throws Exception {
				return aHasher().from(message)
		 				 		.withExtranonce1(extranonce1)
		 				 		.withExtranonce2(extranonce2)
		 				 		.build();
			}
		};
	}
	
	private static Function<Hasher,ObservableSource<StratumMessageBuilder>> hash(final TargetMatcher matcher, final StratumMessage message) {
		return new Function<Hasher,ObservableSource<StratumMessageBuilder>>() {
			@Override
			public ObservableSource<StratumMessageBuilder> apply(Hasher hasher) throws Exception {
				return range(min().getValue(), max().getValue()).map(toNonce())
																.flatMap(new Function<Nonce, ObservableSource<StratumMessageBuilder>>() {
					@Override
					public ObservableSource<StratumMessageBuilder> apply(Nonce nonce) throws Exception {
						return just(nonce)
								.observeOn(computation())
								.map(hash(hasher))
								.filter(matches(matcher))
								.map(submit(hasher, nonce));
					}
				});
			}
		};
	}
	
	private static Function<Integer,Nonce> toNonce() {
		return new Function<Integer,Nonce>() {
			@Override
			public Nonce apply(Integer value) throws Exception {
				return nonce(value);
			}
		};
	}
	
	private static io.reactivex.functions.Function<Nonce, byte[]> hash(final Hasher hasher) {
		return new io.reactivex.functions.Function<Nonce, byte[]>() {
			@Override
			public byte[] apply(Nonce nonce) throws Exception {
				return hasher.hash(nonce);
			}
		};
	}
	
	private static Predicate<byte[]> matches(final TargetMatcher matcher) {
		return new Predicate<byte[]>() {
			@Override
			public boolean test(byte[] hash) throws Exception {
				//LOGGER.info("hashing... {}", encodeHexString(hash));
				
				return matcher.matches(hash);
			}
		};
	}
	
	private static Function<byte[],StratumMessageBuilder> submit(final Hasher hasher, final Nonce nonce) {
		return new Function<byte[],StratumMessageBuilder>() {
			@Override
			public StratumMessageBuilder apply(byte[] hash) throws Exception {
				return aStratumMessage().withMethod(MINING_SUBMIT)
										.withParams("dabla.1", hasher.getJobId(), hasher.getExtranonce2(), hasher.getNtime(), nonce.toString());
			}
		};
	}
	
	private Predicate<StratumMessage> isConnected() {
        return new Predicate<StratumMessage>() {
            @Override
            public boolean test(StratumMessage message) throws Exception {
                return socket.isConnected();
            }
        };
    }
}