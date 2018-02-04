package org.litecoinpool.miner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.glassfish.grizzly.http.util.HexUtils.convert;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

/**
 * 
 * @see <a href="http://learnmeabitcoin.com/guide/difficulty#finding-the-target-using-the-difficulty">Finding the target using the difficulty</a>
 *
 */
public class TargetMatcherTest {
	@Test
	public void maxTarget() throws Exception {
		assertThat(TargetMatcher.MAX_TARGET).
			isEqualTo(new BigDecimal(new BigInteger(convert("0000ffff00000000000000000000000000000000000000000000000000000000"))));
		assertThat(TargetMatcher.MAX_TARGET).
			isEqualTo(new BigDecimal(Nonce.fromHex("0000ffff00000000000000000000000000000000000000000000000000000000")));
	}
	
	@Test
	public void getTarget() throws Exception {
		assertThat(TargetMatcher.withDifficulty(1).getTarget()).isEqualTo(TargetMatcher.MAX_TARGET);
		assertThat(TargetMatcher.withDifficulty(32).getTarget()).isEqualTo(new BigDecimal(Nonce.fromHex("000007fff8000000000000000000000000000000000000000000000000000000")));
		assertThat(TargetMatcher.withDifficulty(Nonce.fromHex("0000003cc3660000000000000000000000000000000000000000000000000000")).getTarget()).isEqualTo(new BigDecimal(1078.5297507748264));
		assertThat(TargetMatcher.withDifficulty("1b3cc366").getTarget()).isEqualTo(new BigDecimal(1078.5297507748264));
	}
	
	@Test
	// https://github.com/Stratehm/stratum-proxy/blob/master/src/main/java/strat/mining/stratum/proxy/utils/mining/SHA256HashingUtils.java
	public void matches_withDifficulty() throws Exception {
		assertThat(TargetMatcher.withDifficulty(32)
								.matches("f6f13e350aa4f251e192ab8a78690ee99f1cc2d930d4ae16c4172a0a8aefddd0")).isFalse();
		assertThat(TargetMatcher.withDifficulty(32)
								.matches("7441207b6390054623bc5e659ffe2581356dafc5ec41db44d27de85035000000")).isTrue();
	}
	
	@Test
	public void matches_withTarget() throws Exception {
		assertThat(TargetMatcher.withTarget("7fff800000000000000000000000000000000000000000000000000000000000")
								.matches(   "f6f13e350aa4f251e192ab8a78690ee99f1cc2d930d4ae16c4172a0a8aefddd0")).isFalse();
		assertThat(TargetMatcher.withTarget("7fff800000000000000000000000000000000000000000000000000000000000")
							    .matches(   "7441207b6390054623bc5e659ffe2581356dafc5ec41db44d27de85035000000")).isTrue();
	}
}
