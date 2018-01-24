package org.litecoinpool.miner;

import static org.litecoinpool.miner.ByteAsserter.assertThat;
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
		
		assertThat(actual.hash()).isEqualTo("f6f13e350aa4f251e192ab8a78690ee99f1cc2d930d4ae16c4172a0a8aefddd0");
	}
}
