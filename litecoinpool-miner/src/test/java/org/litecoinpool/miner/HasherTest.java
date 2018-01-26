package org.litecoinpool.miner;

import static org.apache.commons.codec.binary.Hex.encodeHexString;
import static org.litecoinpool.miner.ByteAsserter.assertThat;
import static org.litecoinpool.miner.HasherBuilder.aHasher;
import static org.litecoinpool.miner.HasherBuilderTest.EXTRANONCE1;
import static org.litecoinpool.miner.HasherBuilderTest.EXTRANONCE2;
import static org.litecoinpool.miner.HasherBuilderTest.MESSAGE;
import static org.litecoinpool.miner.Nonce.min;
import static org.litecoinpool.miner.Nonce.nonce;
import static org.litecoinpool.miner.TargetMatcher.withDifficulty;

import org.junit.Ignore;
import org.junit.Test;
import org.stratum.protocol.StratumMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

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
	public void hash_withNonce() throws Exception {
		Hasher actual = aHasher().from(new ObjectMapper().readValue(MESSAGE, StratumMessage.class))
				 				 .withExtranonce1(EXTRANONCE1)
				 				 .withExtranonce2(EXTRANONCE2)
				 				 .withNtime("53178f9f")
				 				 .build();
		
		assertThat(actual.hash(nonce("00007f8a"))).isEqualTo("7441207b6390054623bc5e659ffe2581356dafc5ec41db44d27de85035000000");
	}
	
	@Test
	@Ignore
	public void hash_forLoop() throws Exception {
		Hasher actual = aHasher().from(new ObjectMapper().readValue(MESSAGE, StratumMessage.class))
				 				 .withExtranonce1(EXTRANONCE1)
				 				 .withExtranonce2(EXTRANONCE2)
				 				 .build();
		
		assertThat(actual.hash()).isEqualTo("f6f13e350aa4f251e192ab8a78690ee99f1cc2d930d4ae16c4172a0a8aefddd0");
		assertThat(actual.hash(min())).isEqualTo("f6f13e350aa4f251e192ab8a78690ee99f1cc2d930d4ae16c4172a0a8aefddd0");
		
		TargetMatcher matcher = withDifficulty("32");
		
		// 0 to 65535
		for (Nonce nonce : Nonce.values()) {
			String hash = encodeHexString(actual.hash(nonce));
			boolean found = matcher.matches(hash);
			if (found) {
				System.out.println(hash);
			}
		}
	}
}
