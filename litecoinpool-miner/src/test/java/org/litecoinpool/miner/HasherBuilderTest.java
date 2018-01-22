package org.litecoinpool.miner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.litecoinpool.miner.HasherBuilder.aHasher;
import static org.smartwallet.stratum.StratumMessage.SENTINEL;

import org.junit.Test;
import org.smartwallet.stratum.StratumMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @see <a href="https://bitcoin.stackexchange.com/questions/22929/full-example-data-for-scrypt-stratum-client">Full example data for scrypt Stratum-client</a>
 *
 */
public class HasherBuilderTest extends AbstractTest {
	static final String EXTRANONCE1 = "f8002c90";
	static final String EXTRANONCE2 = "00000002";
	static final String MESSAGE = "{\"params\": [\"b3ba\", \"7dcf1304b04e79024066cd9481aa464e2fe17966e19edf6f33970e1fe0b60277\", \"01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff270362f401062f503253482f049b8f175308\", \"0d2f7374726174756d506f6f6c2f000000000100868591052100001976a91431482118f1d7504daf1c001cbfaf91ad580d176d88ac00000000\", [\"57351e8569cb9d036187a79fd1844fd930c1309efcd16c46af9bb9713b6ee734\", \"936ab9c33420f187acae660fcdb07ffdffa081273674f0f41e6ecc1347451d23\"], \"00000002\", \"1b44dfdb\", \"53178f9b\", true], \"id\": null, \"method\": \"mining.notify\"}";
			
	@Test
	public void build_fromStratumMessage() throws Exception {
		Hasher actual = aHasher().from(new ObjectMapper().readValue(MESSAGE, StratumMessage.class))
								 .withExtranonce1(EXTRANONCE1)
								 .withExtranonce2(EXTRANONCE2)
								 .build();

		assertThat(actual.getExtranonce1()).isEqualTo(EXTRANONCE1);
		assertThat(actual.getExtranonce2()).isEqualTo(EXTRANONCE2);
		assertThat(actual.getJobId()).isEqualTo("b3ba");
		assertThat(actual.getPreviousHash()).isEqualTo("7dcf1304b04e79024066cd9481aa464e2fe17966e19edf6f33970e1fe0b60277");
		assertThat(actual.getCoinbase()).isEqualTo("01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff270362f401062f503253482f049b8f175308f8002c90000000020d2f7374726174756d506f6f6c2f000000000100868591052100001976a91431482118f1d7504daf1c001cbfaf91ad580d176d88ac00000000");
		assertThat(actual.getCoinbase1()).isEqualTo("01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff270362f401062f503253482f049b8f175308");
		assertThat(actual.getCoinbase2()).isEqualTo("0d2f7374726174756d506f6f6c2f000000000100868591052100001976a91431482118f1d7504daf1c001cbfaf91ad580d176d88ac00000000");
		assertThat(actual.getMerkleBranches()).contains("57351e8569cb9d036187a79fd1844fd930c1309efcd16c46af9bb9713b6ee734", "936ab9c33420f187acae660fcdb07ffdffa081273674f0f41e6ecc1347451d23");
		assertThat(actual.getVersion()).isEqualTo("00000002");
		assertThat(actual.getNbits()).isEqualTo("1b44dfdb");
		assertThat(actual.getNtime()).isEqualTo("53178f9b");
		assertThat(actual.isCleanJobs()).isTrue();
	}
	
	@Test
	public void build_fromSentinelStratumMessage() throws Exception {
		Hasher actual = aHasher().from(SENTINEL).build();
		
		assertThat(actual.getExtranonce1()).isNull();
		assertThat(actual.getExtranonce2()).isNull();
		assertThat(actual.getJobId()).isNull();
		assertThat(actual.getPreviousHash()).isNull();
		assertThat(actual.getCoinbase()).isNull();
		assertThat(actual.getCoinbase1()).isNull();
		assertThat(actual.getCoinbase2()).isNull();
		assertThat(actual.getMerkleBranches()).isEmpty();
		assertThat(actual.getVersion()).isNull();
		assertThat(actual.getNbits()).isNull();
		assertThat(actual.getNtime()).isNull();
		assertThat(actual.isCleanJobs()).isFalse();
	}
}