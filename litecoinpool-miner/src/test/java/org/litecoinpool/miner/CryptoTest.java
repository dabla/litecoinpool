package org.litecoinpool.miner;

import static org.apache.commons.codec.binary.Hex.decodeHex;
import static org.litecoinpool.miner.ByteAsserter.assertThat;
import static org.litecoinpool.miner.Crypto.crypto;
import static org.litecoinpool.miner.Nonce.min;
import static org.litecoinpool.miner.Nonce.nonce;

import org.junit.Test;

public class CryptoTest {
	@Test
	public void scrypt() throws Exception {
		assertThat(crypto().scrypt(decodeHex("020000000413cf7d02794eb094cd66404e46aa816679e12f6fdf9ee11f0e97337702b6e00b1edc1ccf82d3214423fc68234f4946119e39df2cc2137e31ebc186191d54229b8f1753dbdf441b00000000"))).isEqualTo("f6f13e350aa4f251e192ab8a78690ee99f1cc2d930d4ae16c4172a0a8aefddd0");
		assertThat(crypto().scrypt(decodeHex("020000000413cf7d02794eb094cd66404e46aa816679e12f6fdf9ee11f0e97337702b6e00b1edc1ccf82d3214423fc68234f4946119e39df2cc2137e31ebc186191d54229b8f1753dbdf441b00000000"), min().getValue())).isEqualTo("f6f13e350aa4f251e192ab8a78690ee99f1cc2d930d4ae16c4172a0a8aefddd0");
		assertThat(crypto().scrypt(decodeHex("020000000413cf7d02794eb094cd66404e46aa816679e12f6fdf9ee11f0e97337702b6e00b1edc1ccf82d3214423fc68234f4946119e39df2cc2137e31ebc186191d54229f8f1753dbdf441b00000000"), nonce("00007f8a").getValue())).isEqualTo("7441207b6390054623bc5e659ffe2581356dafc5ec41db44d27de85035000000");
		assertThat(crypto().scrypt(decodeHex("020000000413cf7d02794eb094cd66404e46aa816679e12f6fdf9ee11f0e97337702b6e00b1edc1ccf82d3214423fc68234f4946119e39df2cc2137e31ebc186191d54229f8f1753dbdf441b8a7f0000"))).isEqualTo("7441207b6390054623bc5e659ffe2581356dafc5ec41db44d27de85035000000");
	}
}
