package org.litecoinpool.miner;

import static java.security.MessageDigest.getInstance;
import static java.security.Security.addProvider;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypto {
	private final MessageDigest sha256;
	private final MessageDigest scrypt;
	
	static {
		addProvider(new SCryptProvider());
	}
	
	private Crypto(MessageDigest sha256, MessageDigest scrypt) {
		this.sha256 = sha256;
		this.scrypt = scrypt;
	}
	
	private static final ThreadLocal<Crypto> INSTANCE = new ThreadLocal<Crypto>() {
		@Override
		protected Crypto initialValue() {
			try {
				return new Crypto(getInstance("SHA-256"),
								  getInstance("SCrypt"));
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalArgumentException(e);
			}
		}
	};
	
	public static Crypto crypto() {
		return INSTANCE.get();
	}
	
	public byte[] sha256(byte[] value) {
		sha256.update(value);
	    return sha256.digest();
	}
	
	public byte[] dsha256(byte[] value) {
		return sha256(sha256(value));
	}
	
	public byte[] scrypt(byte[] value) {
		scrypt.update(value);
	    return scrypt.digest();
	}
	
	public byte[] scrypt(byte[] value, int nonce) throws DigestException {
	    scrypt.update(value, nonce, value.length);
	    return scrypt.digest();
	}
}
