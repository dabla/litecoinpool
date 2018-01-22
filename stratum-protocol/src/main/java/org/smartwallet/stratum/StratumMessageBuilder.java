package org.smartwallet.stratum;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static org.smartwallet.stratum.StratumMethod.stratumMethod;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;

public class StratumMessageBuilder {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	private Long id;
	private StratumMethod method;
	private List<JsonNode> params = emptyList();
	private JsonNode result;
	
	private StratumMessageBuilder() {}
	
	public static StratumMessageBuilder aStratumMessage() {
		return new StratumMessageBuilder();
	}
	
	public StratumMessageBuilder withId(Long id) {
		this.id = id;
		return this;
	}

	public StratumMessageBuilder withMethod(String method) {
		return withMethod(stratumMethod(method));
	}
	
	public StratumMessageBuilder withMethod(StratumMethod method) {
		this.method = method;
		return this;
	}

	public StratumMessageBuilder withParams(Iterable<Object> params) {
		List<JsonNode> nodes = newArrayList(from(params).transform(toJsonNode()));
		return withParams(nodes);
	}
	
	public StratumMessageBuilder withParams(Object... params) {
		List<JsonNode> nodes = newArrayList(from(params).transform(toJsonNode()));
		return withParams(nodes);
	}
	
	public StratumMessageBuilder withParams(List<JsonNode> params) {
		this.params = unmodifiableList(params);
		return this;
	}
	
	public StratumMessageBuilder withResult(String result) {
		return withResult(MAPPER.valueToTree(result));
	}
	
	public StratumMessageBuilder withResult(JsonNode result) {
		this.result = result;
		return this;
	}

	public StratumMessage build() {
		return new StratumMessage(id, method, params, result);
	}

	private static Function<Object, JsonNode> toJsonNode() {
		return new Function<Object, JsonNode>() {
			@Override
			public JsonNode apply(Object param) {
				return MAPPER.valueToTree(param);
			}
		};
	}
}
