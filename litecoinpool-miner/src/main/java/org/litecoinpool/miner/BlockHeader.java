package org.litecoinpool.miner;

import static org.apache.commons.codec.binary.Hex.decodeHex;

import org.apache.commons.codec.DecoderException;

public class BlockHeader {
	private final String value;
	
	BlockHeader(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	public byte[] getBytes() throws DecoderException {
		return decodeHex(value);
	}
}
