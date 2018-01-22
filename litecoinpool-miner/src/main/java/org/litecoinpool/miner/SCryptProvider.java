package org.litecoinpool.miner;

@SuppressWarnings("serial")
public class SCryptProvider extends java.security.Provider {
	SCryptProvider() {
		super("SCrypt", 1.0, "SCrypt v1.0");
		put("MessageDigest.SCrypt", SCryptMessageDigest.class.getName());
	}
}
