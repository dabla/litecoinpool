package org.litecoinpool.miner;

import static org.apache.commons.codec.binary.Hex.encodeHexString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.litecoinpool.miner.HasherBuilder.aHasher;
import static org.litecoinpool.miner.HasherBuilderTest.EXTRANONCE1;
import static org.litecoinpool.miner.HasherBuilderTest.EXTRANONCE2;
import static org.litecoinpool.miner.HasherBuilderTest.MESSAGE;

import org.junit.Test;
import org.smartwallet.stratum.StratumMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HasherTest {
	@Test
	public void getCoinbase() throws Exception {
		Hasher actual = aHasher().from(new ObjectMapper().readValue(MESSAGE, StratumMessage.class))
				 .withExtranonce1(EXTRANONCE1)
				 .withExtranonce2(EXTRANONCE2)
				 .build();
		
		assertThat(encodeHexString(actual.getCoinbase())).isEqualTo("280b3927f6763b1ed90cae2a3cef4d27c743f6a7d91e3901dc3816a46acacf36");
		assertThat(encodeHexString(actual.getMerkleRoot())).isEqualTo("0b1edc1ccf82d3214423fc68234f4946119e39df2cc2137e31ebc186191d5422");
	}
}
