package org.litecoinpool.miner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.litecoinpool.miner.BlockHeaderBuilder.aBlockHeader;

import org.junit.Test;

public class BlockHeaderBuilderTest {
	@Test
	public void build() throws Exception {
		String actual = aBlockHeader().withVersion("00000002")
					  				  .withPreviousHash("7dcf1304b04e79024066cd9481aa464e2fe17966e19edf6f33970e1fe0b60277")
					  				  .withMerkleRoot("0b1edc1ccf82d3214423fc68234f4946119e39df2cc2137e31ebc186191d5422")
					  				  .withNtime("53178f9b")
					  				  .withNbits("1b44dfdb")
					  				  .build();
		
		assertThat(actual).isEqualTo("020000000413cf7d02794eb094cd66404e46aa816679e12f6fdf9ee11f0e97337702b6e00b1edc1ccf82d3214423fc68234f4946119e39df2cc2137e31ebc186191d54229b8f1753dbdf441b00000000");
	}
}
