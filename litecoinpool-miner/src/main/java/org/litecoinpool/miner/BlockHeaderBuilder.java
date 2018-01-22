package org.litecoinpool.miner;

import static com.google.common.base.Joiner.on;
import static com.google.common.base.Splitter.fixedLength;
import static com.google.common.collect.FluentIterable.from;

import com.google.common.base.Function;

public class BlockHeaderBuilder {
	private static final String NONCE = "00000000";
	private String version;
	private String previousHash;
	private String merkleRoot;
	private String ntime;
	private String nbits;
	
	private BlockHeaderBuilder() {}
	
	public static BlockHeaderBuilder aBlockHeader() {
		return new BlockHeaderBuilder();
	}

	public BlockHeaderBuilder withVersion(String version) {
		this.version = version;
		return this;
	}

	public BlockHeaderBuilder withPreviousHash(String previousHash) {
		this.previousHash = previousHash;
		return this;
	}

	public BlockHeaderBuilder withMerkleRoot(String merkleRoot) {
		this.merkleRoot = merkleRoot;
		return this;
	}

	public BlockHeaderBuilder withNtime(String ntime) {
		this.ntime = ntime;
		return this;
	}

	public BlockHeaderBuilder withNbits(String nbits) {
		this.nbits = nbits;
		return this;
	}

	public String build() {
		return new StringBuffer()
			.append(reverseHex(version))
			.append(from(fixedLength(8).split(previousHash)).transform(reverseHex()).join(on("")))
			.append(merkleRoot)
			.append(reverseHex(ntime))
			.append(reverseHex(nbits))
			.append(NONCE)
			.toString();
	}
	
	private static Function<String,String> reverseHex() {
		return new Function<String,String>() {
			@Override
			public String apply(String value) {
				return reverseHex(value);
			}
		};
	}

	private static String reverseHex(String value) {
	    // TODO: Validation that the length is even
	    int lengthInBytes = value.length() / 2;
	    char[] chars = new char[lengthInBytes * 2];
	    for (int index = 0; index < lengthInBytes; index++) {
	        int reversedIndex = lengthInBytes - 1 - index;
	        chars[reversedIndex * 2] = value.charAt(index * 2);
	        chars[reversedIndex * 2 + 1] = value.charAt(index * 2 + 1);
	    }
	    return new String(chars);
	}
}
