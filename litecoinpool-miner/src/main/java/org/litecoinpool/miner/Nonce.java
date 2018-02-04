package org.litecoinpool.miner;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.copyOfRange;
import static org.apache.commons.codec.binary.Hex.encodeHexString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.math.BigInteger;
import java.util.Iterator;

public class Nonce implements Iterable<Nonce> {
	private static final Nonce MIN = new Nonce(0);
	private static final Nonce MAX = new MaxNonce();
	
	private final int value;
	
	private Nonce(int value) {
		this.value = value;
	}
	
	public static Nonce nonce(String value) {
		return nonce(parseInt(value, 16));
	}
	
	public static Nonce nonce(byte value[]) {
		return nonce(reverseHex(encodeHexString(copyOfRange(value, 76, 79))));
	}
	
	public static Nonce nonce(int value) {
		switch(value) {
			case 0 : 		 return MIN;
			case MAX_VALUE : return MAX;
			default : 		 return new Nonce(value);
		}
	}
	
	public static Nonce min() {
		return MIN;
	}
	
	public static Nonce max() {
		return MAX;
	}
	
	public int getValue() {
		return value;
	}
	
	public boolean isMax() {
		return false;
	}
	
	public Nonce increment() {
		return nonce(value + 1);
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
			super(MAX_VALUE);
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
	
	private static final class NonceIterator implements Iterator<Nonce> {
		private Nonce nonce;
		
		public NonceIterator(Nonce nonce) {
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
