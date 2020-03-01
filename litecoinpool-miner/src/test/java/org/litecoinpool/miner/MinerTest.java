package org.litecoinpool.miner;

import static io.reactivex.Observable.fromIterable;
import static io.reactivex.Observable.just;
import static io.reactivex.schedulers.Schedulers.computation;
import static org.litecoinpool.miner.HasherBuilderTest.EXTRANONCE1;
import static org.litecoinpool.miner.HasherBuilderTest.EXTRANONCE2;
import static org.litecoinpool.miner.Miner.miner;
import static org.litecoinpool.miner.Nonce.min;

import org.junit.Before;
import org.junit.Test;
import org.stratum.protocol.StratumMessage;
import org.unitils.inject.annotation.TestedObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class MinerTest extends AbstractTest {
	static final String MESSAGE = "{\"params\": [\"b3ba\", \"7dcf1304b04e79024066cd9481aa464e2fe17966e19edf6f33970e1fe0b60277\", \"01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff270362f401062f503253482f049b8f175308\", \"0d2f7374726174756d506f6f6c2f000000000100868591052100001976a91431482118f1d7504daf1c001cbfaf91ad580d176d88ac00000000\", [\"57351e8569cb9d036187a79fd1844fd930c1309efcd16c46af9bb9713b6ee734\", \"936ab9c33420f187acae660fcdb07ffdffa081273674f0f41e6ecc1347451d23\"], \"00000002\", \"1b44dfdb\", \"53178f9f\", true], \"id\": null, \"method\": \"mining.notify\"}";
	
	@TestedObject
	private Miner miner;
	
	@Before
	public void setUp() throws Exception {
		miner = miner().withMessage(new ObjectMapper().readValue(MESSAGE, StratumMessage.class)).withExtranonce1(EXTRANONCE1).withExtranonce2(EXTRANONCE2).withDifficulty(32);
	}
	
	@Test
	public void mine() throws Exception {
		fromIterable(min())
			.flatMap(loop())
			.blockingSubscribe();
		//for (Nonce nonce : min()) {
		//	miner = miner.hash(nonce);
		//}
		
		//assertThat(actual).isEqualTo("7441207b6390054623bc5e659ffe2581356dafc5ec41db44d27de85035000000");
	}

	private Function<Nonce,ObservableSource<Nonce>> loop() {
		return new Function<Nonce,ObservableSource<Nonce>>() {
			@Override
			public ObservableSource<Nonce> apply(Nonce nonce) throws Exception {
				return just(nonce).subscribeOn(computation()).map(hash());
			}
		};
	}
	
	private Function<Nonce,Nonce> hash() {
		return new Function<Nonce,Nonce>() {
			@Override
			public Nonce apply(Nonce nonce) throws Exception {
				miner = miner.hash(nonce);
				return nonce;
			}
		};
	}
}
