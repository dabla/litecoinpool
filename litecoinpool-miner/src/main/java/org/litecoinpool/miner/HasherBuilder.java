package org.litecoinpool.miner;

import static com.fasterxml.jackson.databind.node.NullNode.getInstance;
import static com.google.common.collect.Iterables.get;
import static com.google.common.collect.Iterables.getFirst;

import java.util.List;

import org.smartwallet.stratum.StratumMessage;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

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
		return withJobId(getFirst(params, getInstance()).asText(null))
			  .withPreviousHash(get(params, 1, getInstance()).asText(null))
			  .withCoinbase1(get(params, 2, getInstance()).asText(null))
			  .withCoinbase2(get(params, 3, getInstance()).asText(null))
			  .withMerkleBranches(toStringArray(get(params, 4, getInstance())))
			  .withVersion(get(params, 5, getInstance()).asText(null))
			  .withNbits(get(params, 6, getInstance()).asText(null))
			  .withNtime(get(params, 7, getInstance()).asText(null))
			  .withCleanJobs(get(params, 8, getInstance()).asBoolean());
	}

	public HasherBuilder withExtranonce1(String extranonce1) {
		this.extranonce1 = extranonce1;
		return this;
	}

	public HasherBuilder withExtranonce2(String extranonce2) {
		this.extranonce2 = extranonce2;
		return this;
	}

	public HasherBuilder withJobId(String jobId) {
		this.jobId = jobId;
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

	public HasherBuilder withCleanJobs(boolean cleanJobs) {
		this.cleanJobs = cleanJobs;
		return this;
	}

	public Hasher build() {
		return new Hasher(extranonce1, extranonce2, jobId, previousHash, coinbase1, coinbase2, merkleBranches, version, nbits, ntime, cleanJobs);
	}
	
	private static String[] toStringArray(JsonNode jsonNode) {
		if (jsonNode.isArray()) {
			return FluentIterable.from(jsonNode)
								 .transform(asString())
								 .toArray(String.class);
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
