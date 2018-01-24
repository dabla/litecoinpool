package org.litecoinpool.miner;

import java.math.BigInteger;

public class Nonce {
	public static final int MIN_VALUE = 0;
	public static final int MAX_VALUE = 65535;
	private static final Nonce[] VALUES = new Nonce[MAX_VALUE + 1];
	
	private final int value;
	
	static {
		for (int value = MIN_VALUE; value <= MAX_VALUE; value++) {
			VALUES[value] = new Nonce(value);
		}
	}
	
	private Nonce(int value) {
		this.value = value;
	}
	
	public static Nonce nonce(String value) {
		return VALUES[new BigInteger(value, 16).intValue()];
	}
	
	public static Nonce min() {
		return VALUES[MIN_VALUE];
	}
	
	public static Nonce max() {
		return VALUES[VALUES.length - 1];
	}
	
	public int getValue() {
		return value;
	}
	
	public static Nonce[] values() {
		return VALUES;
	}
}
