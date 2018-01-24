package org.litecoinpool.miner;

import static java.util.Collections.unmodifiableCollection;

import java.util.ArrayList;
import java.util.Collection;

public class Nonce {
	public static final int MAX_VALUE = 65535;
	private static final Collection<Nonce> VALUES;
	
	private final int value;
	
	static {
		Collection<Nonce> values = new ArrayList<Nonce>(MAX_VALUE);
		
		for (int value = 0; value <= MAX_VALUE; value++) {
			values.add(new Nonce(value));
		}
		
		VALUES = unmodifiableCollection(values);
	}
	
	private Nonce(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static Collection<Nonce> values() {
		return VALUES;
	}
}
