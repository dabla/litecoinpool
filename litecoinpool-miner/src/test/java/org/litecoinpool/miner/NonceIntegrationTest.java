package org.litecoinpool.miner;

import static io.reactivex.Observable.fromIterable;
import static io.reactivex.Observable.just;
import static io.reactivex.schedulers.Schedulers.computation;
import static org.litecoinpool.miner.Nonce.min;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.Test;
import org.slf4j.Logger;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NonceIntegrationTest {
	private static final Logger LOGGER = getLogger(NonceIntegrationTest.class);
	
	@Test
	public void iterate() throws Exception {
		/*for (Nonce nonce : Nonce.min()) {
			System.out.println("nonce: " + nonce);
		}*/
		
		fromIterable(min())
			.flatMap(loop())
			.blockingSubscribe();
	}

	private static Function<Nonce,ObservableSource<Nonce>> loop() {
		return new Function<Nonce,ObservableSource<Nonce>>() {
			@Override
			public ObservableSource<Nonce> apply(Nonce nonce) throws Exception {
				return just(nonce)
						//.subscribeOn(computation())
						.map(log());
			}
		};
	}

	private static Function<Nonce,Nonce> log() {
		return new Function<Nonce,Nonce>() {
			@Override
			public Nonce apply(Nonce nonce) throws Exception {
				//LOGGER.info("nonce: {}", nonce);
				if (nonce.getValue() % 20000 == 0)
				System.out.println("nonce: " + nonce);
				return nonce;
			}
		};
	}
}
