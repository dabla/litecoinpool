package org.litecoinpool.miner;

import static org.apache.commons.codec.binary.Hex.encodeHexString;

import org.assertj.core.api.Assertions;

public class ByteAsserter {
	private final byte[] actual;
	
	private ByteAsserter(byte[] actual) {
		this.actual = actual;
	}
	
	public static ByteAsserter assertThat(byte[] actual) {
		return new ByteAsserter(actual);
	}
	
	public ByteAsserter isEqualTo(String expected) {
		Assertions.assertThat(encodeHexString(actual)).isEqualTo(expected);
		return this;
	}
}
