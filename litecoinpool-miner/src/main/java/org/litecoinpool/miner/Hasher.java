package org.litecoinpool.miner;

import static org.litecoinpool.miner.Crypto.crypto;

import java.security.DigestException;

import org.apache.commons.codec.DecoderException;

public class Hasher {
	private final String extranonce1;
	private final String extranonce2;
	private final String jobId;
	private final String ntime;
	private final BlockHeaderBuilder blockHeaderBuilder;
	private final boolean cleanJobs;
	
	Hasher(String extranonce1, String extranonce2, String jobId, String ntime, BlockHeaderBuilder blockHeaderBuilder, boolean cleanJobs) {
		this.extranonce1 = extranonce1;
		this.extranonce2 = extranonce2;
		this.jobId = jobId;
		this.ntime = ntime;
		this.blockHeaderBuilder = blockHeaderBuilder;
		this.cleanJobs = cleanJobs;
	}
	
	public String getExtranonce1() {
		return extranonce1;
	}

	public String getExtranonce2() {
		return extranonce2;
	}

	public String getJobId() {
		return jobId;
	}
	
	public String getNtime() {
		return ntime;
	}

	public boolean isCleanJobs() {
		return cleanJobs;
	}
	
	public byte[] hash() throws DecoderException {
		return crypto().scrypt(blockHeaderBuilder.build().getBytes());
	}
	
	public byte[] hash(Nonce nonce) throws DecoderException, DigestException {
		return hash(nonce.getValue());
	}
	
	private byte[] hash(int nonce) throws DecoderException, DigestException {
		return crypto().scrypt(blockHeaderBuilder.build().getBytes(), nonce); // TODO: maybe pass nonce via blockHeaderBuilder
	}
	
	public byte[] hash(String nonce) throws DecoderException {
		return crypto().scrypt(blockHeaderBuilder.withNonce(nonce).build().getBytes());
	}
}
