package org.litecoinpool.miner;

import com.google.common.base.Optional;
import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.stratum.protocol.StratumMessage;

import java.security.DigestException;
import java.security.NoSuchAlgorithmException;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static org.apache.commons.codec.binary.Hex.encodeHexString;
import static org.litecoinpool.miner.Hasher.NO_HASHER;
import static org.litecoinpool.miner.HasherBuilder.aHasher;
import static org.litecoinpool.miner.TargetMatcher.DEFAULT_MATCHER;
import static org.slf4j.LoggerFactory.getLogger;

public class Miner {
	private static final Logger LOGGER = getLogger(Miner.class);
	private final Optional<Nonce> nonce;
	private final Hasher hasher;
	private final TargetMatcher matcher;
	
	private Miner(Optional<Nonce> nonce, Hasher hasher, TargetMatcher matcher) {
		this.nonce = nonce;
		this.hasher = hasher;
		this.matcher = matcher;
	}
	
	public static Miner miner() {
		return new Miner(absent(), NO_HASHER, DEFAULT_MATCHER);
	}
	
	public MinerBuilder withMessage(StratumMessage message) {
		return new MinerBuilder(aHasher().from(message), matcher);
	}
	
	final class MinerBuilder {
		private final HasherBuilder hasherBuilder;
		private final TargetMatcher matcher;
		
		private MinerBuilder(HasherBuilder hasherBuilder, TargetMatcher matcher) {
			this.hasherBuilder = hasherBuilder;
			this.matcher = matcher;
		}
		
		public MinerBuilder withExtranonce1(String extranonce1) {
			hasherBuilder.withExtranonce1(extranonce1);
			return this;
		}
		
		public Miner withExtranonce2(String extranonce2) throws NoSuchAlgorithmException, DecoderException {
			return new Miner(nonce, hasherBuilder.withExtranonce1(extranonce2).build(), matcher);
		}
	}
	
	public Miner withDifficulty(int difficulty) {
		return new Miner(nonce, hasher, TargetMatcher.withDifficulty(difficulty));
	}
	
	public Miner hash(Nonce nonce) throws DigestException, DecoderException {
		if (nonce.equals(this.nonce.orNull())) {
			throw new IllegalArgumentException("No valid hashes found!");
		}
		
		byte[] hash = hasher.hash(nonce);
		
		if (matcher.matches(hash)) {
			return new CompletedMiner(of(nonce), hasher, matcher, hash);
		}
		
		return new Miner(of(this.nonce.or(nonce)), hasher, matcher);
	}
	
	public byte[] getHash() {
		return null;
	}
	
	private static final class CompletedMiner extends Miner {
		private final byte[] hash;

		private CompletedMiner(Optional<Nonce> nonce, Hasher hasher, TargetMatcher matcher, byte[] hash) {
			super(nonce, hasher, matcher);
			this.hash = hash;
		}
		
		@Override
		public Miner hash(Nonce nonce) throws DigestException, DecoderException {
			throw new IllegalStateException("Valid hash '" + encodeHexString(hash) + "' already found!");
		}
		
		@Override
		public byte[] getHash() {
			return hash;
		}
	}
}
