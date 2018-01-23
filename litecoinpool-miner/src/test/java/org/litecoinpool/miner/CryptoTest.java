package org.litecoinpool.miner;

import static org.apache.commons.codec.binary.Hex.decodeHex;
import static org.litecoinpool.miner.ByteAsserter.assertThat;
import static org.litecoinpool.miner.Crypto.crypto;

import org.junit.Test;

public class CryptoTest {
	@Test
	public void scrypt() throws Exception {
		assertThat(crypto().scrypt(decodeHex("020000000413cf7d02794eb094cd66404e46aa816679e12f6fdf9ee11f0e97337702b6e00b1edc1ccf82d3214423fc68234f4946119e39df2cc2137e31ebc186191d54229b8f1753dbdf441b00000000"))).isEqualTo("f6f13e350aa4f251e192ab8a78690ee99f1cc2d930d4ae16c4172a0a8aefddd0");
	}
}
