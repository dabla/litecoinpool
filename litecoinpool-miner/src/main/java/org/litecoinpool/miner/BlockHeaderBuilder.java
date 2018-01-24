package org.litecoinpool.miner;

import static com.google.common.base.Joiner.on;
import static com.google.common.base.Splitter.fixedLength;
import static com.google.common.collect.FluentIterable.from;
import static org.apache.commons.codec.binary.Hex.decodeHex;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import org.apache.commons.codec.DecoderException;

import com.google.common.base.Function;

public class BlockHeaderBuilder {
	public static final String NONCE = "00000000";
	
	private String version;
	private String previousHash;
	private String merkleRoot;
	private String ntime;
	private String nbits;
	private String nonce = NONCE;
	
	private BlockHeaderBuilder() {}
	
	public static BlockHeaderBuilder aBlockHeader() {
		return new BlockHeaderBuilder();
	}

	public BlockHeaderBuilder withVersion(String version) {
		this.version = reverseHex(version);
		return this;
	}

	public BlockHeaderBuilder withPreviousHash(String previousHash) {
		if (previousHash != null) {
			this.previousHash = from(fixedLength(8).split(previousHash)).transform(reverseHex()).join(on(""));
		}
		return this;
	}

	public BlockHeaderBuilder withMerkleRoot(String merkleRoot) {
		this.merkleRoot = merkleRoot;
		return this;
	}

	public BlockHeaderBuilder withNtime(String ntime) {
		this.ntime = reverseHex(ntime);
		return this;
	}

	public BlockHeaderBuilder withNbits(String nbits) {
		this.nbits = reverseHex(nbits);
		return this;
	}
	
	public BlockHeaderBuilder withNonce(String nonce) {
		this.nonce = reverseHex(nonce);
		return this;
	}

	public byte[] build() throws DecoderException {
		String blockHeader = new StringBuffer()
			.append(version)
			.append(previousHash)
			.append(merkleRoot)
			.append(ntime)
			.append(nbits)
			.append(nonce)
			.toString();
		
		return decodeHex(blockHeader);
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
		if (isNotBlank(value)) {
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
		
		return null;
	}
}
