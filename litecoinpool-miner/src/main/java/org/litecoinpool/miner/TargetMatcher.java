package org.litecoinpool.miner;

import static org.slf4j.LoggerFactory.getLogger;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.slf4j.Logger;

public class TargetMatcher {
	private static final Logger LOGGER = getLogger(TargetMatcher.class);
	private static final BigInteger MAX_TARGET = new BigInteger("ffff0000000000000000000000000000000000000000000000000000000000000", 16);
	private final BigInteger target;
	
	private TargetMatcher(BigInteger target) {
		this.target = target;
	}

	public static TargetMatcher withDifficulty(String difficulty) {
		return withDifficulty(new BigDecimal(difficulty).toBigInteger());
	}
	
	public static TargetMatcher withDifficulty(BigInteger difficulty) {
		return withTarget(MAX_TARGET.divide(difficulty));
	}
	
	public static TargetMatcher withTarget(String target) {
		return withTarget(new BigInteger(target, 16));
	}
	
	public static TargetMatcher withTarget(BigInteger target) {
		LOGGER.info("Target is {} ({})", target, target.toString(16));
		return new TargetMatcher(target);
	}
	
	public BigInteger getTarget() {
		return target;
	}
	
	public boolean matches(String hash) {
		return new BigInteger(hash, 16).doubleValue() < target.doubleValue();
	}
}
