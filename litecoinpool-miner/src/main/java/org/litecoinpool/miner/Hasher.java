package org.litecoinpool.miner;

import org.apache.commons.codec.DecoderException;

import java.security.DigestException;

import static org.litecoinpool.miner.Crypto.crypto;

public class Hasher {
	static final Hasher NO_HASHER = new Hasher() {
		@Override
		public byte[] hash(Nonce nonce) throws DigestException {
			throw new IllegalStateException();
		}
	};

	private final String extranonce1;
	private final String extranonce2;
	private final String ntime;
	private final byte[] blockHeader;

	private Hasher() {
		this(null, null, null, null);
	}

	private Hasher(String extranonce1, String extranonce2, String ntime, byte[] blockHeader) {
		this.extranonce1 = extranonce1;
		this.extranonce2 = extranonce2;
		this.ntime = ntime;
		this.blockHeader = blockHeader;
	}

	static Hasher hasher(String extranonce1, String extranonce2, String ntime, BlockHeaderBuilder blockHeaderBuilder) throws DecoderException {
		return new Hasher(extranonce1, extranonce2, ntime, blockHeaderBuilder.build().getBytes());
	}

	String getExtranonce1() {
		return extranonce1;
	}

	String getExtranonce2() {
		return extranonce2;
	}

	String getNtime() {
		return ntime;
	}

	byte[] getBlockHeader() {
		return blockHeader;
	}

	public byte[] hash() {
		return crypto().scrypt(blockHeader);
	}
	
	public byte[] hash(Nonce nonce) throws DigestException {
		return hash(nonce.getValue());
	}
	
	private byte[] hash(int nonce) throws DigestException {
		return crypto().scrypt(blockHeader, nonce); // TODO: maybe pass nonce via blockHeaderBuilder
	}
}
