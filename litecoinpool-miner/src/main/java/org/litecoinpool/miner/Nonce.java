package org.litecoinpool.miner;

import static com.google.common.base.Strings.padEnd;
import static java.util.Arrays.copyOfRange;
import static org.apache.commons.codec.binary.Hex.encodeHexString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
		return nonce(new BigInteger(value, 16).intValue());
	}
	
	public static Nonce nonce(byte value[]) {
		return nonce(reverseHex(encodeHexString(copyOfRange(value, 76, 79))));
	}
	
	public static Nonce nonce(int value) {
		return VALUES[value];
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
	
	@Override
	public String toString() {
		return encodeHexString(intToByteArray(value));
	}
	
	public static Nonce[] values() {
		return VALUES;
	}
	
	public static final byte[] intToByteArray(int value) {
	    return new byte[] {
	            (byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
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
}
