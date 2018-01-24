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
	public void getTarget() throws Exception {
		assertThat(TargetMatcher.withDifficulty("1").getTarget().toString(16)).isEqualTo("ffff0000000000000000000000000000000000000000000000000000000000000");
		assertThat(TargetMatcher.withDifficulty("32").getTarget().toString(16)).isEqualTo("7fff800000000000000000000000000000000000000000000000000000000000");
		assertThat(TargetMatcher.withDifficulty("14484.16236123").getTarget().toString(16)).isEqualTo("4864f52edf8c9ea5dbf193d4bb7e327a976fc64f52edf8c9ea5dbf193d4bb7");
	}
	
	@Test
	public void matches_withDifficulty() throws Exception {
		assertThat(TargetMatcher.withDifficulty("14484.16236123")
							    .matches("000000000003ba27aa200b1cecaad478d2b00432346c3f1f3986da1afd33e506")).isTrue();
		assertThat(TargetMatcher.withDifficulty("32")
								.matches("f6f13e350aa4f251e192ab8a78690ee99f1cc2d930d4ae16c4172a0a8aefddd0")).isFalse();
		assertThat(TargetMatcher.withDifficulty("32")
			    				.matches("7441207b6390054623bc5e659ffe2581356dafc5ec41db44d27de85035000000")).isTrue();
	}
	
	@Test
	public void matches_withTarget() throws Exception {
		assertThat(TargetMatcher.withTarget("4864f52edf8c9ea5dbf193d4bb7e327a976fc64f52edf8c9ea5dbf193d4bb7")
			    			    .matches("000000000003ba27aa200b1cecaad478d2b00432346c3f1f3986da1afd33e506")).isTrue();
		assertThat(TargetMatcher.withTarget("7fff800000000000000000000000000000000000000000000000000000000000")
								.matches(   "f6f13e350aa4f251e192ab8a78690ee99f1cc2d930d4ae16c4172a0a8aefddd0")).isFalse();
		assertThat(TargetMatcher.withTarget("7fff800000000000000000000000000000000000000000000000000000000000")
							    .matches(   "7441207b6390054623bc5e659ffe2581356dafc5ec41db44d27de85035000000")).isTrue();
	}
}
