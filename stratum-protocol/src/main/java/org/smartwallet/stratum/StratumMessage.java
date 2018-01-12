package org.smartwallet.stratum;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by devrandom on 2015-Aug-25.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StratumMessage {
    private Long id;

    /** RPC method - for calls */
    private StratumMethod method;

    /** Parameters - for calls */
    private List<JsonNode> params;

    /** Result - for result */
    @JsonProperty("result")
    private JsonNode result;

    @JsonProperty("error")
    private String error;

    public static final StratumMessage SENTINEL = new StratumMessage();

    private StratumMessage() {}

    /*@JsonIgnore
    public StratumMessage(Long id, JsonNode result) {
    	this(id, null, null, null);
    }

    @JsonIgnore
    StratumMessage(Long id, String method, JsonNode result) {
    	this(id, method, null, null);
    }
    @JsonIgnore
    StratumMessage(Long id, String method, List<JsonNode> params) {
        this(id, method, params, null);
    }*/

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
    
    public StratumMethod getMethod() {
		return method;
	}
    
    public List<JsonNode> getParams() {
		return params;
	}
    
    public JsonNode getResult() {
		return result;
	}
    
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
