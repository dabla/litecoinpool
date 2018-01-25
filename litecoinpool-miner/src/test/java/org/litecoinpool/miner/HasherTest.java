package org.litecoinpool.miner;

import static io.reactivex.Observable.just;
import static io.reactivex.schedulers.Schedulers.computation;
import static java.util.Arrays.asList;
import static org.apache.commons.codec.binary.Hex.encodeHexString;
import static org.litecoinpool.miner.ByteAsserter.assertThat;
import static org.litecoinpool.miner.HasherBuilder.aHasher;
import static org.litecoinpool.miner.HasherBuilderTest.EXTRANONCE1;
import static org.litecoinpool.miner.HasherBuilderTest.EXTRANONCE2;
import static org.litecoinpool.miner.HasherBuilderTest.MESSAGE;
import static org.litecoinpool.miner.TargetMatcher.withDifficulty;

import java.security.DigestException;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;
import org.stratum.protocol.StratumMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;

import be.dabla.parallel.iterable.ParallelIterable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;

public class HasherTest {
	@Test
	public void hash() throws Exception {
		Hasher actual = aHasher().from(new ObjectMapper().readValue(MESSAGE, StratumMessage.class))
				 				 .withExtranonce1(EXTRANONCE1)
				 				 .withExtranonce2(EXTRANONCE2)
				 				 .build();
		
		assertThat(actual.hash()).isEqualTo("f6f13e350aa4f251e192ab8a78690ee99f1cc2d930d4ae16c4172a0a8aefddd0");
	}
	
	@Test
	public void hash_forLoop() throws Exception {
		Hasher actual = aHasher().from(new ObjectMapper().readValue(MESSAGE, StratumMessage.class))
				 				 .withExtranonce1(EXTRANONCE1)
				 				 .withExtranonce2(EXTRANONCE2)
				 				 .build();
		
		assertThat(actual.hash()).isEqualTo("f6f13e350aa4f251e192ab8a78690ee99f1cc2d930d4ae16c4172a0a8aefddd0");
		assertThat(actual.hash(0)).isEqualTo("f6f13e350aa4f251e192ab8a78690ee99f1cc2d930d4ae16c4172a0a8aefddd0");
		
		TargetMatcher matcher = withDifficulty("32");
		
		// 0 to 65535
		for (int nonce = 0; nonce < 65536; nonce++) {
			String hash = encodeHexString(actual.hash(nonce));
			boolean found = matcher.matches(hash);
			if (found) {
				System.out.println(hash);
			}
		}
	}
	
	@Test
	public void hash_parallelIterable() throws Exception {
		Hasher actual = aHasher().from(new ObjectMapper().readValue(MESSAGE, StratumMessage.class))
				 				 .withExtranonce1(EXTRANONCE1)
				 				 .withExtranonce2(EXTRANONCE2)
				 				 .build();
		
		assertThat(actual.hash()).isEqualTo("f6f13e350aa4f251e192ab8a78690ee99f1cc2d930d4ae16c4172a0a8aefddd0");
		assertThat(actual.hash(0)).isEqualTo("f6f13e350aa4f251e192ab8a78690ee99f1cc2d930d4ae16c4172a0a8aefddd0");
		
		ParallelIterable.<Nonce>aParallelIterable().from(asList(Nonce.values())).transform(hash(actual));
	}
	
	@Test
	public void hash_reactiveStreams() throws Exception {
		Hasher actual = aHasher().from(new ObjectMapper().readValue(MESSAGE, StratumMessage.class))
				 				 .withExtranonce1(EXTRANONCE1)
				 				 .withExtranonce2(EXTRANONCE2)
				 				 .build();
		
		assertThat(actual.hash()).isEqualTo("f6f13e350aa4f251e192ab8a78690ee99f1cc2d930d4ae16c4172a0a8aefddd0");
		assertThat(actual.hash(0)).isEqualTo("f6f13e350aa4f251e192ab8a78690ee99f1cc2d930d4ae16c4172a0a8aefddd0");
		
		final TargetMatcher matcher = withDifficulty("32");
		
		Observable.fromArray(Nonce.values()).flatMap(new io.reactivex.functions.Function<Nonce, ObservableSource<String>>() {
			@Override
			public ObservableSource<String> apply(Nonce nonce) throws Exception {
				return just(nonce)
						.observeOn(computation())
						.map(hash(actual, matcher));
			}
		}).blockingSubscribe();
	}
	
	private static io.reactivex.functions.Function<Nonce, String> hash(Hasher actual, final TargetMatcher matcher) {
		return new io.reactivex.functions.Function<Nonce, String>() {
			@Override
			public String apply(Nonce nonce) throws Exception {
				String hash = encodeHexString(actual.hash(nonce.getValue()));
				boolean found = matcher.matches(hash);
				if (found) {
					System.out.println(hash);
				}
				return hash;
			}
		};
	}

	private static Function<Nonce, String> hash(final Hasher hasher) {
		final TargetMatcher matcher = withDifficulty("32");
		
		return new Function<Nonce, String>() {
			@Override
			public String apply(Nonce nonce) {
				try {
					return hash(hasher, matcher, nonce);
				} catch (DecoderException | DigestException e) {
					throw new RuntimeException(e);
				}
			}

			private String hash(final Hasher hasher, final TargetMatcher matcher, Nonce nonce) throws DecoderException, DigestException {
				String hash = encodeHexString(hasher.hash(nonce.getValue()));
				boolean found = matcher.matches(hash);
				if (found) {
					System.out.println(hash);
				}
				return hash;
			}
		};
	}
}
