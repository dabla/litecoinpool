package org.litecoinpool.miner;

import static org.apache.commons.codec.digest.DigestUtils.getDigest;

import java.security.MessageDigest;

public class Crypto {
	private final MessageDigest messageDigest;
	
	private Crypto(MessageDigest messageDigest) {
		this.messageDigest = messageDigest;
	}
	
	private static final ThreadLocal<Crypto> INSTANCE = new ThreadLocal<Crypto>() {
		@Override
		protected Crypto initialValue() {
			return new Crypto(getDigest("SHA-256"));
		}
	};
	
	public static Crypto crypto() {
		return INSTANCE.get();
	}
	
	public byte[] sha256(byte[] value) {
		messageDigest.update(value);
	    return messageDigest.digest();
	}
	
	public byte[] dsha256(byte[] value) {
		return sha256(sha256(value));
	}
}
