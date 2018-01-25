package org.stratum.protocol;

import org.litecoinpool.core.EnumValue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using=StratumMethodSerializer.class)
@JsonDeserialize(using=StratumMethodDeserializer.class)
public enum StratumMethod implements EnumValue<String> {
	NULL(null),
	MINING_SUBSCRIBE("mining.subscribe"),
	MINING_AUTHORIZE("mining.authorize"),
	MINING_NOTIFY("mining.notify"),
	MINING_SET_DIFFICULTY("mining.set_difficulty"),
	MINING_SUBMIT("mining.submit"),
	CLIENT_GET_VERSION("client.get_version");
    
    private final String value;
    
    private StratumMethod() {
    	this(null);
    }
    
    private StratumMethod(String value) {
		this.value = value;
    }
    
    public static StratumMethod stratumMethod(String value) {
    	return EnumValue.valueOf(StratumMethod.class, value, NULL);
    }

    @JsonIgnore
    public boolean isSubscribe() { 
    	return "mining.subscribe".equals(value);
    }
    
    @JsonIgnore
    public boolean isNotify() { 
    	return "mining.notify".equals(value);
    }
    
    @JsonIgnore
    public boolean isSetDifficulty() { 
    	return "mining.set_difficulty".equals(value);
    }

    @JsonProperty("method")
    public String getValue() {
		return value;
	}
	
    @Override
	public String toString() {
		return value;
	}
}
