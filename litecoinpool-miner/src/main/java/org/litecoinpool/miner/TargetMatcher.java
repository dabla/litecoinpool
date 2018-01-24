package org.litecoinpool.miner;

import static java.lang.Double.parseDouble;
import static org.slf4j.LoggerFactory.getLogger;

import java.math.BigInteger;

import org.slf4j.Logger;

public class TargetMatcher {
	private static final Logger LOGGER = getLogger(TargetMatcher.class);
	private static final double MAX_TARGET = new BigInteger("00000000FFFF0000000000000000000000000000000000000000000000000000", 16).doubleValue();
	
	private final double target;

	private TargetMatcher(double target) {
		this.target = target;
	}

	public static TargetMatcher withDifficulty(String difficulty) {
		return withDifficulty(parseDouble(difficulty));
	}
	
	public static TargetMatcher withDifficulty(double difficulty) {
		return withTarget(MAX_TARGET / difficulty);
	}
	
	public static TargetMatcher withTarget(String target) {
		return withTarget(new BigInteger(target, 16));
	}
	
	public static TargetMatcher withTarget(BigInteger target) {
		LOGGER.info("Target is {}", target.toString(16));
		return new TargetMatcher(target.doubleValue());
	}
	
	public static TargetMatcher withTarget(Double target) {
		return new TargetMatcher(target);
	}
	
	public double getTarget() {
		return target;
	}
	
	public boolean matches(String hash) {
		return new BigInteger(hash, 16).doubleValue() < target;
	}
}
