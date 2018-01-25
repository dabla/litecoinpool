package org.stratum.protocol;

import static com.fasterxml.jackson.databind.node.NullNode.getInstance;
import static org.stratum.protocol.StratumMethod.NULL;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by devrandom on 2015-Aug-25.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StratumMessage {
	public static final StratumMessage SENTINEL = new StratumMessage();

	private Long id;
    private StratumMethod method;
    private List<JsonNode> params;
    private JsonNode result;
    private StratumError error;

    private StratumMessage() {
    	this(null, NULL, Collections.<JsonNode>emptyList(), getInstance());
    }

    @JsonIgnore
    StratumMessage(Long id, StratumMethod method, List<JsonNode> params, JsonNode result) {
        this.id = id;
        this.method = method;
        this.params = params;
        this.result = result;
    }
    
    @JsonProperty("id")
    public Long getId() {
		return id;
	}
    
    @JsonProperty("method")
    public StratumMethod getMethod() {
		return method;
	}
    
    @JsonProperty("params")
    public List<JsonNode> getParams() {
		return params;
	}
    
    @JsonProperty("result")
    public JsonNode getResult() {
		return result;
	}
    
    @JsonProperty("error")
    public StratumError getError() {
		return error;
	}

    @JsonIgnore
    public boolean isResult() {
        return id != null && result != null;
    }

    @JsonIgnore
    public boolean isMessage() {
        return id == null && method != null && params != null;
    }

    @JsonIgnore
    public boolean isSentinel() {
        return this == SENTINEL;
    }

    @JsonIgnore
    public boolean isError() {
        return id != null && error != null;
    }
}
