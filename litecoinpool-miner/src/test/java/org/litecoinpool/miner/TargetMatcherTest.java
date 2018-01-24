package org.litecoinpool.miner;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * 
 * @see <a href="http://learnmeabitcoin.com/guide/difficulty#finding-the-target-using-the-difficulty">Finding the target using the difficulty</a>
 *
 */
public class TargetMatcherTest {
	@Test
	public void matches_withDifficulty() throws Exception {
		assertThat(TargetMatcher.withDifficulty(14484.162361)
								.matches("000000000003ba27aa200b1cecaad478d2b00432346c3f1f3986da1afd33e506")).isTrue();
	}
	
	@Test
	public void matches_withTarget() throws Exception {
		assertThat(TargetMatcher.withTarget("000007fff8000000000000000000000000000000000000000000000000000000")
								.matches("f6f13e350aa4f251e192ab8a78690ee99f1cc2d930d4ae16c4172a0a8aefddd0")).isFalse();
		assertThat(TargetMatcher.withTarget("000007fff8000000000000000000000000000000000000000000000000000000")
							    .matches("0000003550e87dd244db41ecc5af6d358125fe9f655ebc23460590637b204174")).isTrue();
	}
}
