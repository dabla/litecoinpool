package org.litecoinpool.miner;

import static com.google.common.base.Joiner.on;
import static com.google.common.base.Splitter.fixedLength;
import static com.google.common.collect.FluentIterable.from;
import static org.apache.commons.codec.binary.Hex.decodeHex;
import static org.apache.commons.codec.binary.Hex.encodeHexString;
import static org.litecoinpool.miner.Nonce.intToByteArray;
import static org.litecoinpool.miner.Nonce.reverseHex;

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
			this.previousHash = from(fixedLength(8).split(previousHash)).transform(reversedHex()).join(on(""));
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
	
	public BlockHeaderBuilder withNonce(int nonce) {
		return withNonce(encodeHexString(intToByteArray(nonce)));
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
	
	private static Function<String,String> reversedHex() {
		return new Function<String,String>() {
			@Override
			public String apply(String value) {
				return reverseHex(value);
			}
		};
	}
}
