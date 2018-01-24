package org.litecoinpool.miner;

import static org.litecoinpool.miner.Crypto.crypto;

import org.apache.commons.codec.DecoderException;

public class Hasher {
	private final String extranonce1;
	private final String extranonce2;
	private final String jobId;
	private final BlockHeaderBuilder blockHeaderBuilder;
	private final boolean cleanJobs;
	
	Hasher(String extranonce1, String extranonce2, String jobId, BlockHeaderBuilder blockHeaderBuilder, boolean cleanJobs) {
		this.extranonce1 = extranonce1;
		this.extranonce2 = extranonce2;
		this.jobId = jobId;
		this.blockHeaderBuilder = blockHeaderBuilder;
		this.cleanJobs = cleanJobs;
	}
	
	static Hasher hasher(String extranonce1, String extranonce2, String jobId, BlockHeaderBuilder blockHeaderBuilder, boolean cleanJobs) {
		return new Hasher(extranonce1, extranonce2, jobId, blockHeaderBuilder, cleanJobs);
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

	public boolean isCleanJobs() {
		return cleanJobs;
	}
	
	public byte[] hash() throws DecoderException {
		return crypto().scrypt(blockHeaderBuilder.build());
	}
	
	public byte[] hash(String nonce) throws DecoderException {
		return crypto().scrypt(blockHeaderBuilder.withNonce(nonce).build());
	}
}
