package org.litecoinpool.miner;

import static java.lang.Integer.MAX_VALUE;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.litecoinpool.miner.Nonce.nonce;

import org.junit.Test;

public class NonceTest {
	@Test
	public void min() throws Exception {
		assertThat(Nonce.min().getValue()).isZero();
	}
	
	@Test
	public void max() throws Exception {
		assertThat(Nonce.max().getValue()).isEqualTo(MAX_VALUE);
	}
	
	@Test
	public void fromInteger() throws Exception {
		assertThat(nonce(0).toString()).isEqualTo("00000000");
		assertThat(nonce(4).toString()).isEqualTo("00000004");
		assertThat(nonce(32650).toString()).isEqualTo("00007f8a");
		assertThat(nonce(456967014).toString()).isEqualTo("1b3cc366");
		assertThat(nonce(MAX_VALUE).toString()).isEqualTo("7fffffff");
		assertThat(nonce(32650).toString()).isEqualTo("00007f8a");
	}
	
	@Test
	public void fromHex() throws Exception {
		assertThat(nonce("00000000").getValue()).isZero();
		assertThat(nonce("00007f8a").getValue()).isEqualTo(32650);
		assertThat(nonce("1b3cc366").getValue()).isEqualTo(456967014);
		assertThat(nonce("7fffffff").getValue()).isEqualTo(MAX_VALUE);
	}

	@Test
	public void partition() throws Exception {
		assertThat(Nonce.max().partition(8))
				.containsExactly(nonce("00000000"), nonce("10000000"), nonce("20000000"), nonce("30000000"),
								 nonce("40000000"), nonce("50000000"), nonce("60000000"), nonce("70000000"));
	}
}
