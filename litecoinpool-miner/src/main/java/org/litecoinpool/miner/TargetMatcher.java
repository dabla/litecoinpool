package org.litecoinpool.miner;

import static java.math.RoundingMode.UP;
import static org.slf4j.LoggerFactory.getLogger;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.slf4j.Logger;

public class TargetMatcher {
	private static final Logger LOGGER = getLogger(TargetMatcher.class);
	//private static final BigInteger MAX_TARGET = new BigInteger("ffff0000000000000000000000000000000000000000000000000000000000000", 16);
	private static final BigDecimal MAX_TARGET = new BigDecimal(new BigInteger("ffff0000000000000000000000000000000000000000000000000000000000000", 16));
	//private static final BigDecimal MAX_TARGET = new BigDecimal("26959535291011309493156476344723991336010898738574164086137773096960");
	private final BigInteger target;
	
	private TargetMatcher(BigInteger target) {
		this.target = target;
	}

	public static TargetMatcher withDifficulty(String difficulty) {
		return withDifficulty(new BigDecimal(difficulty));
	}
	
	public static TargetMatcher withDifficulty(BigDecimal difficulty) {
		return withTarget(MAX_TARGET.divide(difficulty, UP).toBigInteger());
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
