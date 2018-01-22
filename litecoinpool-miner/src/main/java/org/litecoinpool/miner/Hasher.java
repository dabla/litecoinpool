package org.litecoinpool.miner;

import static org.apache.commons.codec.binary.Hex.decodeHex;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.stripToNull;
import static org.litecoinpool.miner.Crypto.crypto;

import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.DecoderException;

public class Hasher {
	private final String extranonce1;
	private final String extranonce2;
	private final String jobId;
	private final String previousHash;
	private final byte[] coinbase;
	private final String coinbase1;
	private final String coinbase2;
	private final String[] merkleBranches;
	private final byte[] merkleRoot;
	private final String version;
	private final String nbits;
	private final String ntime;
	private final boolean cleanJobs;
	
	Hasher(String extranonce1, String extranonce2, String jobId, String previousHash, byte[] coinbase,
		   String coinbase1, String coinbase2, String[] merkleBranches, byte[] merkleRoot, String version, String nbits, String ntime, boolean cleanJobs) {
		this.extranonce1 = extranonce1;
		this.extranonce2 = extranonce2;
		this.jobId = jobId;
		this.previousHash = previousHash;
		this.coinbase = coinbase;
		this.coinbase1 = coinbase1;
		this.coinbase2 = coinbase2;
		this.merkleBranches = merkleBranches;
		this.merkleRoot = merkleRoot;
		this.version = version;
		this.nbits = nbits;
		this.ntime = ntime;
		this.cleanJobs = cleanJobs;
	}
	
	static Hasher hasher(String extranonce1, String extranonce2, String jobId, String previousHash, String coinbase1,
			   	         String coinbase2, String[] merkleBranches, String version, String nbits, String ntime, boolean cleanJobs) throws NoSuchAlgorithmException, DecoderException {
		String coinbase = stripToNull(join(coinbase1, extranonce1, extranonce2, coinbase2));
		byte[] hashedCoinbase = crypto().dsha256(decodeHex(coinbase));
		byte[] merkleRoot = hashedCoinbase;
		
		for (String merkleBranch : merkleBranches) {
			merkleRoot = crypto().dsha256(addAll(merkleRoot, decodeHex(merkleBranch)));
		}
		
		return new Hasher(extranonce1, extranonce2, jobId, previousHash, hashedCoinbase, coinbase1, coinbase2, merkleBranches, merkleRoot, version, nbits, ntime, cleanJobs);
		
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

	public String getPreviousHash() {
		return previousHash;
	}
	
	public byte[] getCoinbase() {
		return coinbase;
	}

	public String getCoinbase1() {
		return coinbase1;
	}

	public String getCoinbase2() {
		return coinbase2;
	}

	public String[] getMerkleBranches() {
		return merkleBranches;
	}
	
	public byte[] getMerkleRoot() {
		return merkleRoot;
	}

	public String getVersion() {
		return version;
	}

	public String getNbits() {
		return nbits;
	}

	public String getNtime() {
		return ntime;
	}

	public boolean isCleanJobs() {
		return cleanJobs;
	}
}
