package org.litecoinpool.miner;

import static java.lang.Integer.MAX_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

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
		assertThat(Nonce.nonce(0).toString()).isEqualTo("00000000");
		assertThat(Nonce.nonce(4).toString()).isEqualTo("00000004");
		assertThat(Nonce.nonce(32650).toString()).isEqualTo("00007f8a");
		assertThat(Nonce.nonce(456967014).toString()).isEqualTo("1b3cc366");
		assertThat(Nonce.nonce(MAX_VALUE).toString()).isEqualTo("7fffffff");
		assertThat(Nonce.nonce(32650).toString()).isEqualTo("00007f8a");
	}
	
	@Test
	public void fromHex() throws Exception {
		assertThat(Nonce.nonce("00000000").getValue()).isZero();
		assertThat(Nonce.nonce("00007f8a").getValue()).isEqualTo(32650);
		assertThat(Nonce.nonce("1b3cc366").getValue()).isEqualTo(456967014);
		assertThat(Nonce.nonce("7fffffff").getValue()).isEqualTo(MAX_VALUE);
	}
}
