package org.litecoinpool.miner;

import static org.apache.commons.codec.binary.Hex.decodeHex;
import static org.apache.commons.codec.binary.Hex.encodeHexString;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static org.litecoinpool.miner.Crypto.crypto;

import org.apache.commons.codec.DecoderException;

public class MerkleBranchJoiner {
	private final byte[] coinbase;
	
	private MerkleBranchJoiner(byte[] coinbase) {
		this.coinbase = coinbase;
	}
	
	public static MerkleBranchJoiner on(byte[] coinbase) {
		return new MerkleBranchJoiner(coinbase);
	}
	
	public String join(String... merkleBranches) throws DecoderException {
		byte[] merkleRoot = coinbase;
		
		for (String merkleBranch : merkleBranches) {
			merkleRoot = crypto().dsha256(addAll(merkleRoot, decodeHex(merkleBranch)));
		}
		
		return encodeHexString(merkleRoot);
	}
}
