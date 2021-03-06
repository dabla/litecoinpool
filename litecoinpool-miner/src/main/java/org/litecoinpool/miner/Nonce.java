package org.litecoinpool.miner;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Objects;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.copyOfRange;
import static org.apache.commons.codec.binary.Hex.encodeHexString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class Nonce implements Iterable<Nonce> {
	private static final Nonce MIN = new Nonce(0);
	private static final Nonce MAX = new MaxNonce();
	
	private final int value;
	private final int max;

	private Nonce(int value) {
		this(value, MAX_VALUE);
	}
	
	private Nonce(int value, int max) {
		this.value = value;
		this.max = max;
	}
	
	public static Nonce nonce(String value) {
		return nonce(parseInt(value, 16));
	}
	
	public static Nonce nonce(byte value[]) {
		return nonce(reverseHex(encodeHexString(copyOfRange(value, 76, 79))));
	}

	public static Nonce nonce(int value) {
		return nonce(value, MAX_VALUE);
	}
	
	public static Nonce nonce(int value, int max) {
		if (value == 0) return MIN;
		if (value >= MAX_VALUE) return MAX;
		return new Nonce(value, max);
	}
	
	public static Nonce min() {
		return MIN;
	}

	public static Nonce max() {
		return MAX;
	}

	public Nonce[] partition(int number) {
		int quotient  = MAX_VALUE / number;
		Nonce[] nonces = new Nonce[number];
		for (int index = 0; index < number; index++) {
			nonces[index] = new Nonce((index * quotient) + (index * 1), (index + 1) * quotient + (index * 1));
		}
		return nonces;
	}
	
	public int getValue() {
		return value;
	}

	public int getMax() {
		return max;
	}

	public boolean isMax() {
		return value >= max;
	}
	
	public Nonce increment() {
		return nonce(value + 1, max);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Nonce nonce = (Nonce) o;
		return value == nonce.value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public String toString() {
		return intToHexString(value);
	}

	// TODO: extract helper methods to HexUtils class
	public static final byte[] intToByteArray(int value) {
	    return new byte[] {
	            (byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
	
	public static String intToHexString(int value) {
		return encodeHexString(intToByteArray(value));
	}
	
	public static String reverseHex(String value) {
		if (isNotBlank(value)) {
		    // TODO: Validation that the length is even
		    int lengthInBytes = value.length() / 2;
		    char[] chars = new char[lengthInBytes * 2];
		    for (int index = 0; index < lengthInBytes; index++) {
		        int reversedIndex = (lengthInBytes - 1 - index) * 2;
		        int position = index * 2;
		        chars[reversedIndex] = value.charAt(position);
		        chars[++reversedIndex] = value.charAt(++position);
		    }
		    return new String(chars);
		}
		
		return null;
	}
	
	public static BigInteger fromHex(String value) {
		return new BigInteger(value, 16);
	}

	@Override
	public Iterator<Nonce> iterator() {
		return new NonceIterator(this);
	}
	
	private static final class MaxNonce extends Nonce {
		private MaxNonce() {
			super(MAX_VALUE, MAX_VALUE);
		}
		
		@Override
		public boolean isMax() {
			return true;
		}
		
		@Override
		public Nonce increment() {
			return this;
		}
		
		@Override
		public String toString() {
			return "7fffffff";
		}
	}

	private class NonceIterator implements Iterator<Nonce> {
		private Nonce nonce;

		private NonceIterator(Nonce nonce) {
			this.nonce = nonce;
		}

		@Override
		public boolean hasNext() {
			return !nonce.isMax();
		}

		@Override
		public Nonce next() {
			Nonce next = nonce;
			nonce = nonce.increment();
			return next;
		}
	}
}
