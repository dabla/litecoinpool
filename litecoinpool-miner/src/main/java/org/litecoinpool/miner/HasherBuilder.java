package org.litecoinpool.miner;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.apache.commons.codec.DecoderException;
import org.stratum.protocol.StratumMessage;

import java.util.List;
import java.util.stream.Stream;

import static com.fasterxml.jackson.databind.node.NullNode.getInstance;
import static com.google.common.collect.Iterables.get;
import static java.util.stream.Stream.of;
import static org.apache.commons.codec.binary.Hex.decodeHex;
import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.stripToNull;
import static org.litecoinpool.miner.BlockHeaderBuilder.aBlockHeader;
import static org.litecoinpool.miner.CoinbaseBuilder.aCoinbase;
import static org.litecoinpool.miner.Crypto.crypto;
import static org.litecoinpool.miner.Hasher.hasher;

public class HasherBuilder {
	private static final String[] EMPTY_MERKLE_BRANCHES = new String[]{};
	
	private String extranonce1;
	private String extranonce2;
	private String jobId;
	private String previousHash;
	private String coinbase1;
	private String coinbase2;
	private String[] merkleBranches = EMPTY_MERKLE_BRANCHES;
	private String version;
	private String nbits;
	private String ntime;
	private boolean cleanJobs = false;

	private HasherBuilder() {}
	
	public static HasherBuilder aHasher() {
		return new HasherBuilder();
	}
	
	public HasherBuilder from(StratumMessage message) {
		return from(message.getParams());
	}
	
	public HasherBuilder from(List<JsonNode> params) {
		return withPreviousHash(get(params, 1, getInstance()).asText(null))
			  .withCoinbase1(get(params, 2, getInstance()).asText(null))
			  .withCoinbase2(get(params, 3, getInstance()).asText(null))
			  .withMerkleBranches(toStringArray(get(params, 4, getInstance())))
			  .withVersion(get(params, 5, getInstance()).asText(null))
			  .withNbits(get(params, 6, getInstance()).asText(null))
			  .withNtime(get(params, 7, getInstance()).asText(null));
	}

	public HasherBuilder withExtranonce1(String extranonce1) {
		this.extranonce1 = extranonce1;
		return this;
	}

	public HasherBuilder withExtranonce2(String extranonce2) {
		this.extranonce2 = extranonce2;
		return this;
	}

	public HasherBuilder withPreviousHash(String previousHash) {
		this.previousHash = previousHash;
		return this;
	}

	public HasherBuilder withCoinbase1(String coinbase1) {
		this.coinbase1 = coinbase1;
		return this;
	}

	public HasherBuilder withCoinbase2(String coinbase2) {
		this.coinbase2 = coinbase2;
		return this;
	}

	public HasherBuilder withMerkleBranches(String... merkleBranches) {
		this.merkleBranches = merkleBranches;
		return this;
	}

	public HasherBuilder withVersion(String version) {
		this.version = version;
		return this;
	}

	public HasherBuilder withNbits(String nbits) {
		this.nbits = nbits;
		return this;
	}

	public HasherBuilder withNtime(String ntime) {
		this.ntime = ntime;
		return this;
	}

	public Hasher build() throws DecoderException {
		byte[] coinbase = coinbase(coinbase1, extranonce1, extranonce2, coinbase2);
		String merkleRoot = MerkleBranchJoiner.on(coinbase).join(merkleBranches);

		BlockHeaderBuilder blockHeaderBuilder = aBlockHeader()
				.withVersion(version)
				.withPreviousHash(previousHash)
				.withMerkleRoot(merkleRoot)
				.withNtime(ntime)
				.withNbits(nbits);

		return hasher(extranonce1, extranonce2, ntime, blockHeaderBuilder);
	}
	
	private static byte[] coinbase(String coinbase1, String extranonce1, String extranonce2, String coinbase2) throws DecoderException {
		return aCoinbase()
				.withCoinbase1(coinbase1)
				.withExtranonce1(extranonce1)
				.withExtranonce2(extranonce2)
				.withCoinbase2(coinbase2)
				.build();
	}
	
	private static String[] toStringArray(JsonNode jsonNode) {
		if (jsonNode.isArray()) {
			return of(jsonNode).map(asString()).toArray(String[]::new);
		}
		
		return EMPTY_MERKLE_BRANCHES;
	}

	private static Function<JsonNode,String> asString() {
		return new Function<JsonNode,String>() {
			@Override
			public String apply(JsonNode jsonNode) {
				return jsonNode.asText();
			}
		};
	}
}
