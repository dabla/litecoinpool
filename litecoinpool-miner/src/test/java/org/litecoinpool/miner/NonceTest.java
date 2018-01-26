package org.litecoinpool.miner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.litecoinpool.miner.Nonce.MAX_VALUE;
import static org.litecoinpool.miner.Nonce.MIN_VALUE;

import org.junit.Test;

public class NonceTest {
	@Test
	public void min() throws Exception {
		assertThat(Nonce.min().getValue()).isEqualTo(MIN_VALUE);
	}
	
	@Test
	public void max() throws Exception {
		assertThat(Nonce.max().getValue()).isEqualTo(MAX_VALUE);
	}
	
	@Test
	public void nonce() throws Exception {
		assertThat(Nonce.nonce("00007f8a").getValue()).isEqualTo(32650);
	}
	
	@Test
	public void asString() throws Exception {
		assertThat(Nonce.nonce("00007f8a").toString()).isEqualTo("00007f8a");
		assertThat(Nonce.nonce(32650).toString()).isEqualTo("00007f8a");
	}
}
