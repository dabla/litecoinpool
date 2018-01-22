package org.smartwallet.stratum;

import static com.fasterxml.jackson.databind.node.NullNode.getInstance;
import static org.smartwallet.stratum.StratumMethod.NULL;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by devrandom on 2015-Aug-25.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StratumMessage {
	public static final StratumMessage SENTINEL = new StratumMessage();

	private Long id;
    private StratumMethod method;
    private List<JsonNode> params;
    private JsonNode result;
    private String error;

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
    
    public Long getId() {
		return id;
	}
    
    @JsonUnwrapped
    public StratumMethod getMethod() {
		return method;
	}
    
    public List<JsonNode> getParams() {
		return params;
	}
    
    @JsonProperty("result")
    public JsonNode getResult() {
		return result;
	}
    
    @JsonProperty("error")
    public String getError() {
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
