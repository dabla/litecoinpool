package org.smartwallet.stratum;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using=StratumMethodSerializer.class)
@JsonDeserialize(using=StratumMethodDeserializer.class)
public class StratumMethod {
	public static final String BLOCKCHAIN_HEADERS_SUBSCRIBE = "blockchain.headers.subscribe";
    public static final String BLOCKCHAIN_ADDRESS_SUBSCRIBE = "blockchain.address.subscribe";
    public static final String BLOCKCHAIN_GET_HEADER = "blockchain.block.get_header";
    public static final String BLOCKCHAIN_GET_CHUNK = "blockchain.block.get_chunk";
    
    public static final StratumMethod NULL = new StratumMethod(null);
    public static final StratumMethod HEADERS_SUBSCRIBE = new StratumMethod(BLOCKCHAIN_HEADERS_SUBSCRIBE);
    public static final StratumMethod ADDRESS_SUBSCRIBE = new StratumMethod(BLOCKCHAIN_ADDRESS_SUBSCRIBE);
    public static final StratumMethod GET_HEADER = new StratumMethod(BLOCKCHAIN_GET_HEADER);
    public static final StratumMethod GET_CHUNK = new StratumMethod(BLOCKCHAIN_GET_CHUNK);
    
    private final String value;
    
    private StratumMethod() {
    	this(null);
    }
    
    private StratumMethod(String value) {
		this.value = value;
    }
    
    public static StratumMethod stratumMethod(String value) {
    	if (value == null) {
    		return NULL;
    	}
    	
    	if (BLOCKCHAIN_HEADERS_SUBSCRIBE.equals(value)) {
    		return HEADERS_SUBSCRIBE;
    	}
    	
    	if (BLOCKCHAIN_ADDRESS_SUBSCRIBE.equals(value)) {
    		return ADDRESS_SUBSCRIBE;
    	}
    	
    	if (BLOCKCHAIN_GET_HEADER.equals(value)) {
    		return GET_HEADER;
    	}
    	
    	if (BLOCKCHAIN_GET_CHUNK.equals(value)) {
    		return GET_CHUNK;
    	}
    	
    	return new StratumMethod(value);
    }

    @JsonIgnore
    public boolean isSubscribe() { 
    	return value.endsWith(".subscribe");
    }
    
    @JsonIgnore
    public boolean isHeadersSubscribe() { 
    	return BLOCKCHAIN_HEADERS_SUBSCRIBE.equals(value);
    }
    
    @JsonIgnore
    public boolean isAddressSubscribe() { 
    	return BLOCKCHAIN_ADDRESS_SUBSCRIBE.equals(value);
    }
    
    @JsonIgnore
    public boolean isGetHeader() { 
    	return BLOCKCHAIN_GET_HEADER.equals(value);
    }
    
    @JsonIgnore
    public boolean isGetChunk() { 
    	return BLOCKCHAIN_GET_CHUNK.equals(value);
    }
    
    @JsonProperty("method")
    public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StratumMethod other = (StratumMethod) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	public String toString() {
		return value;
	}
}
