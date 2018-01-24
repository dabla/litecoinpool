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
		//assertThat(new BigDecimal("26959535291011309493156476344723991336010898738574164086137773096960").divide(new BigDecimal("14484.162361"), RoundingMode.UP).toBigInteger().toString(10)).isEqualTo("1861311315012765306929610463010191006516769515973403833769533171");
		//assertThat(new BigDecimal("1861311315012765306929610463010191006516769515973403833769533170").toBigInteger().toString(16)).isEqualToIgnoringCase("4864C00004D6AC7CD33F734B8EB28B24729FE151953EC57A21EF2");
		assertThat(TargetMatcher.withDifficulty("1").getTarget().toString(16)).isEqualTo("ffff0000000000000000000000000000000000000000000000000000000000000");
		assertThat(TargetMatcher.withDifficulty("32").getTarget().toString(16)).isEqualTo("7fff800000000000000000000000000000000000000000000000000000000000");
		assertThat(TargetMatcher.withTarget("1861311315012765306929610463010191006516769515973403833769533170").getTarget().toString(16)).isEqualTo("1861311315012765306929610463010191006516769515973403833769533170");
	}
	
	@Test
	public void matches_withDifficulty() throws Exception {
		assertThat(TargetMatcher.withDifficulty("14484.16236123")
							    .matches("000000000003ba27aa200b1cecaad478d2b00432346c3f1f3986da1afd33e506")).isTrue();
		assertThat(TargetMatcher.withDifficulty("32")
								.matches("f6f13e350aa4f251e192ab8a78690ee99f1cc2d930d4ae16c4172a0a8aefddd0")).isFalse();
		//System.out.println("target: " + new BigInteger("7fff800000000000000000000000000000000000000000000000000000000000", 16).toString(10));
		//System.out.println("difficulty: " + new BigInteger("57895161195125708519620700855593582467377078590871844210249731200855918510080").toString(10));
		//System.out.println("difficulty: " + new BigInteger("57895161195125708519620700855593582467377078590871844210249731200855918510080").divide(new BigInteger("32")).toString(10));
		//System.out.println("difficulty: " + new BigInteger("ffff0000000000000000000000000000000000000000000000000000000000000", 16).divide(new BigInteger("32")).toString(10));
		//System.out.println("difficulty: " + TargetMatcher.withDifficulty("32").getTarget());
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
