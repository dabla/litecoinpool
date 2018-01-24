package org.litecoinpool.miner;

import static org.apache.commons.codec.binary.Hex.encodeHexString;
import static org.litecoinpool.miner.ByteAsserter.assertThat;
import static org.litecoinpool.miner.HasherBuilder.aHasher;
import static org.litecoinpool.miner.HasherBuilderTest.EXTRANONCE1;
import static org.litecoinpool.miner.HasherBuilderTest.EXTRANONCE2;
import static org.litecoinpool.miner.HasherBuilderTest.MESSAGE;
import static org.litecoinpool.miner.TargetMatcher.withDifficulty;

import org.junit.Test;
import org.smartwallet.stratum.StratumMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HasherTest {
	@Test
	public void hash() throws Exception {
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
			System.out.println(found + ": " + hash);
		}
	}
}
