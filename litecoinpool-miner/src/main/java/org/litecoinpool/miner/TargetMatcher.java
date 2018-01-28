package org.litecoinpool.miner;

import static com.google.common.base.Strings.padEnd;
import static org.apache.commons.codec.binary.Hex.decodeHex;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static org.litecoinpool.miner.Nonce.fromHex;
import static org.slf4j.LoggerFactory.getLogger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;

public class TargetMatcher {
	private static final Logger LOGGER = getLogger(TargetMatcher.class);
	private static final byte[] BIG_INTEGER_FAKE_SIGN_ARRAY = new byte[] { (byte) 0 };
	static final BigDecimal MAX_TARGET = new BigDecimal(fromHex("0000ffff00000000000000000000000000000000000000000000000000000000"));
	private final BigDecimal target;
	
	private TargetMatcher(BigDecimal target) {
		this.target = target;
	}

	public static TargetMatcher withDifficulty(String difficulty) {
		if (difficulty.length() == 8) {
			return withDifficulty(fromHex("00000000" + padEnd(difficulty.substring(2), 58, '0')));
		}
		
		throw new IllegalArgumentException("Wrong " + difficulty + " value!");
	}
	
	public static TargetMatcher withDifficulty(int difficulty) {
		return withDifficulty(BigInteger.valueOf(difficulty));
	}
	
	public static TargetMatcher withDifficulty(BigInteger difficulty) {
		return withDifficulty(difficulty.doubleValue());
	}
	
	public static TargetMatcher withDifficulty(double difficulty) {
		return withTarget(new BigDecimal(MAX_TARGET.doubleValue() / difficulty));
	}
	
	public static TargetMatcher withTarget(String target) {
		return withTarget(fromHex(target));
	}
	
	public static TargetMatcher withTarget(BigInteger target) {
		return withTarget(new BigDecimal(target));
	}
	
	public static TargetMatcher withTarget(BigDecimal target) {
		LOGGER.info("Target is {} ({})", target.doubleValue(), target.toBigInteger().toString(16));
		return new TargetMatcher(target);
	}
	
	public BigDecimal getTarget() {
		return target;
	}
	
	public boolean matches(String hash) throws DecoderException {
		return matches(decodeHex(hash));
	}
	
	// https://github.com/Stratehm/stratum-proxy/blob/master/src/main/java/strat/mining/stratum/proxy/utils/mining/SHA256HashingUtils.java
	public boolean matches(byte[] hashIntegerBytes) {
		// Convert the hashInteger to a 256 bits (32 bytes) bytes array
		byte[] hashBytes256Bits = new byte[32];
		Arrays.fill(hashBytes256Bits, (byte) 0);
		copyInto(hashIntegerBytes, hashBytes256Bits, 32 - hashIntegerBytes.length);

		// Then swap bytes from big-endian to little-endian
		hashBytes256Bits = swapBytes(hashBytes256Bits, 4);

		// And reverse the order of 4-bytes words (big-endian to little-endian
		// 256-bits integer)
		hashBytes256Bits = reverseWords(hashBytes256Bits, 4);

		// Build the integer (with a 0 value byte to fake an unsigned int (sign
		// bit to 0)
		BigInteger hashInteger = new BigInteger(addAll(BIG_INTEGER_FAKE_SIGN_ARRAY, hashBytes256Bits));

		return new BigDecimal(hashInteger).compareTo(target) < 0;
	}
	
	/**
	 * Return an array which contains the same data as dataToSwap but with byte
	 * reversed. The word length is used to know on which base the swap has to
	 * be done.
	 * 
	 * The wordByteLength has to be a multiple of the length of the given
	 * dataToSwap, else an IndexOutOfBoundException is thrown.
	 * 
	 * For example, with parameters: dataToSwap = B0 B1 B2 B3 B4 B5 B6 B7
	 * 
	 * wordByteLength=1 return B0 B1 B2 B3 B4 B5 B6 B7
	 * 
	 * wordByteLength=2 return B1 B0 B3 B2 B5 B4 B7 B6
	 * 
	 * wordByteLength=4 return B3 B2 B1 B0 B7 B6 B5 B4
	 * 
	 * wordByteLength=8 return B7 B6 B5 B4 B3 B2 B1 B0
	 * 
	 * @param dataToSwap
	 * @param wordByteLength
	 * @return
	 */
	public static final byte[] swapBytes(byte[] dataToSwap, int wordByteLength) throws IndexOutOfBoundsException {
		byte[] result = null;
		if (dataToSwap != null) {

			if (wordByteLength < 1 || dataToSwap.length % wordByteLength > 0) {
				throw new IndexOutOfBoundsException("The wordByteLength is not a multiple of input data. wordByteLength=" + wordByteLength
						+ ", inputDataSize=" + dataToSwap.length);
			}

			result = new byte[dataToSwap.length];

			for (int i = 0; i < dataToSwap.length; i += wordByteLength) {
				for (int resultOffset = 0, inputOffset = wordByteLength - 1; resultOffset < wordByteLength; resultOffset++, inputOffset--) {
					result[i + resultOffset] = dataToSwap[i + inputOffset];
				}
			}
		}

		return result;
	}

	/**
	 * Reverse the order of the words of the given dataToSwap array based on the
	 * wordByteLength.
	 * 
	 * For example, with parameters: dataToSwap = B0 B1 B2 B3 B4 B5 B6 B7
	 * 
	 * wordByteLength=1 return B7 B6 B5 B4 B3 B2 B1 B0
	 * 
	 * wordByteLength=2 return B6 B7 B4 B5 B2 B3 B0 B1
	 * 
	 * wordByteLength=4 return B4 B5 B6 B7 B0 B1 B2 B3
	 * 
	 * wordByteLength=8 return B0 B1 B2 B3 B4 B5 B6 B7
	 * 
	 * @param dataToSwap
	 * @param wordByteLength
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public static final byte[] reverseWords(byte[] dataToSwap, int wordByteLength) throws IndexOutOfBoundsException {
		byte[] result = null;
		if (dataToSwap != null) {

			if (wordByteLength < 1 || dataToSwap.length % wordByteLength > 0) {
				throw new IndexOutOfBoundsException("The wordByteLength is not a multiple of input data. wordByteLength=" + wordByteLength
						+ ", inputDataSize=" + dataToSwap.length);
			}

			result = new byte[dataToSwap.length];

			for (int inputIndex = 0, resultIndex = dataToSwap.length - wordByteLength; inputIndex < dataToSwap.length; inputIndex += wordByteLength, resultIndex -= wordByteLength) {
				for (int i = 0; i < wordByteLength; i++) {
					result[resultIndex + i] = dataToSwap[inputIndex + i];
				}
			}
		}

		return result;
	}

	/**
	 * Copy the toCopy array into the into Array. The copy start at the
	 * intoStartIndex in the into array.
	 * 
	 * Copy as far as possible. (For example, copy will be full if toCopy is
	 * longer than (into - intoStartIndex)).
	 * 
	 * @param toCopy
	 * @param into
	 * @param intoStartIndex
	 */
	public static final void copyInto(byte[] toCopy, byte[] into, int intoStartIndex) {
		int maxCopyIndex = into.length - intoStartIndex;
		for (int i = 0; i < toCopy.length && i <= maxCopyIndex; i++) {
			into[i + intoStartIndex] = toCopy[i];
		}
}
}
