package org.litecoinpool.miner;

import static org.litecoinpool.miner.BlockHeaderBuilder.aBlockHeader;
import static org.litecoinpool.miner.ByteAsserter.assertThat;

import org.junit.Test;

public class BlockHeaderBuilderTest {
	private static final String VERSION = "00000002";
	private static final String PREVIOUS_HASH = "7dcf1304b04e79024066cd9481aa464e2fe17966e19edf6f33970e1fe0b60277";
	private static final String MERKLE_ROOT = "0b1edc1ccf82d3214423fc68234f4946119e39df2cc2137e31ebc186191d5422";
	private static final String NBITS = "1b44dfdb";

	@Test
	public void build() throws Exception {
		byte[] actual = aBlockHeader().withVersion(VERSION)
					  				  .withPreviousHash(PREVIOUS_HASH)
					  				  .withMerkleRoot(MERKLE_ROOT)
					  				  .withNtime("53178f9b")
					  				  .withNbits(NBITS)
					  				  .build();
		
		assertThat(actual).isEqualTo("020000000413cf7d02794eb094cd66404e46aa816679e12f6fdf9ee11f0e97337702b6e00b1edc1ccf82d3214423fc68234f4946119e39df2cc2137e31ebc186191d54229b8f1753dbdf441b00000000");
	}
	
	@Test
	public void build_withOtherNtimeAndNonce() throws Exception {
		byte[] actual = aBlockHeader().withVersion(VERSION)
									  .withPreviousHash(PREVIOUS_HASH)
									  .withMerkleRoot(MERKLE_ROOT)
									  .withNtime("53178f9f")
									  .withNbits(NBITS)
									  .withNonce("00007f8a")
									  .build();
		
		assertThat(actual).isEqualTo("020000000413cf7d02794eb094cd66404e46aa816679e12f6fdf9ee11f0e97337702b6e00b1edc1ccf82d3214423fc68234f4946119e39df2cc2137e31ebc186191d54229f8f1753dbdf441b8a7f0000");
	}
}
