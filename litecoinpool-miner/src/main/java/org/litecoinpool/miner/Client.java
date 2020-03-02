package org.litecoinpool.miner;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.stratum.protocol.StratumMessage;
import org.stratum.protocol.StratumMessageBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.AUTO_CLOSE_TARGET;
import static com.fasterxml.jackson.databind.node.NullNode.getInstance;
import static com.google.common.collect.Iterables.get;
import static com.google.common.collect.Iterables.getFirst;
import static io.reactivex.Flowable.fromIterable;
import static io.reactivex.schedulers.Schedulers.computation;
import static org.apache.commons.codec.binary.Hex.encodeHexString;
import static org.litecoinpool.miner.HasherBuilder.aHasher;
import static org.litecoinpool.miner.Job.NO_JOB;
import static org.litecoinpool.miner.Job.job;
import static org.litecoinpool.miner.Nonce.intToHexString;
import static org.litecoinpool.miner.Nonce.max;
import static org.litecoinpool.miner.Nonce.min;
import static org.litecoinpool.miner.TargetMatcher.withDifficulty;
import static org.slf4j.LoggerFactory.getLogger;
import static org.stratum.protocol.StratumMessageBuilder.aStratumMessage;
import static org.stratum.protocol.StratumMethod.MINING_SUBMIT;

public class Client {
	private static final Logger LOGGER = getLogger(Client.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private final ObservableSocket socket;
	private final FlowableProcessor<Job> jobs = PublishProcessor.<Job>create().toSerialized();
	private final Map<String,Job> currentJobs = new ConcurrentHashMap<>();
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
		for (Nonce nonce : max().partition(8)) {
            jobs.flatMap(hash(nonce)).map(submit()).subscribe();
        }

		socket.read()
				.takeWhile(isConnected())
				.repeat()
				.map(setDifficulty())
				.filter(isNotify())
				.map(hasher())
				.filter(Job::exists)
				.subscribeWith(jobs)
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
					if (message.getResult().get(0).isArray()) {
						for (int index = 0; index < message.getResult().get(0).size(); index++) {
							String method = message.getResult().get(0).get(index).get(0).asText();
							if ("mining.set_difficulty".equals(method)) {
								String difficulty = message.getResult().get(0).get(index).get(1).asText();
								matcher = withDifficulty(difficulty);
							}
							else if ("mining.notify".equals(method)) {

							}
						}
					}
					extranonce1 = get(message.getResult(), 1, getInstance()).asText();
					extranonce2 = intToHexString(get(message.getResult(), 1, getInstance()).asInt());
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

	private Function<StratumMessage, Job> hasher() {
		return new Function<StratumMessage, Job>() {
			@Override
			public Job apply(StratumMessage message) throws Exception {
				String jobId = getFirst(message.getParams(), getInstance()).asText(null);
				boolean clean = get(message.getParams(), 8, getInstance()).asBoolean(false);
				if (clean) {
					LOGGER.info("Aborting jobId {}", jobId);
					return currentJobs.getOrDefault(jobId, NO_JOB).abort();
				}
				Hasher hasher = aHasher().from(message)
						.withExtranonce1(extranonce1)
						.withExtranonce2(extranonce2)
						.build();
				Job job = job(jobId, matcher, hasher);
				LOGGER.info("Started hashing jobId {}", job.getJobId());
				currentJobs.put(jobId, job);
				return job;
			}
		};
	}

	private static Function<Job,Flowable<StratumMessageBuilder>> hash(final Nonce nonce) {
		return new Function<Job,Flowable<StratumMessageBuilder>>() {
			@Override
			public Flowable<StratumMessageBuilder> apply(Job job) throws Exception {
				return fromIterable(nonce)
						.parallel()
						.runOn(computation())
						.flatMap(new Function<Nonce, Publisher<StratumMessageBuilder>>() {
							@Override
							public Publisher<StratumMessageBuilder> apply(Nonce nonce) throws Exception {
								return Flowable.just(nonce)
										.map(hash(job))
										.onErrorResumeNext(Flowable.empty())
										.filter(matches(job, nonce))
										.map(submit(job, nonce));
							}
						}).sequential();
			}
		};
	}

	private static io.reactivex.functions.Function<Nonce, byte[]> hash(final Job job) {
		return new io.reactivex.functions.Function<Nonce, byte[]>() {
			@Override
			public byte[] apply(Nonce nonce) throws Exception {
				if (nonce.getValue() % 10000 == 0) {
					LOGGER.info("Still hashing jobId {} for nonce {} of {}", job.getJobId(), nonce, nonce.getMax());
				}

				return job.hash(nonce);
			}
		};
	}

	private static Predicate<byte[]> matches(final Job job, Nonce nonce) {
		return new Predicate<byte[]>() {
			@Override
			public boolean test(byte[] hash) throws Exception {
				return job.matches(hash) || nonce.isMax();
			}
		};
	}

	private static Function<byte[],StratumMessageBuilder> submit(final Job job, final Nonce nonce) {
		return new Function<byte[],StratumMessageBuilder>() {
			@Override
			public StratumMessageBuilder apply(byte[] hash) throws Exception {
				return aStratumMessage().withMethod(MINING_SUBMIT)
						.withParams("dabla.1", job.getJobId(), job.getExtranonce2(), job.getNtime(), nonce.toString());
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