package org.stratum.protocol;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class StratumErrorSerializer extends StdSerializer<StratumError> {
    public StratumErrorSerializer() {
        this(null);
    }
   
    public StratumErrorSerializer(Class<StratumError> type) {
        super(type);
    }
 
    @Override
    public void serialize(StratumError error, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
        if (error != null) {
	    	generator.writeStartArray(3);
	        generator.writeNumber(error.getErrorCode().getValue());
	        generator.writeString(error.getMessage());
	        generator.writeString(error.getTraceback());
	        generator.writeEndArray();
        }
        else {
        	generator.writeNull();
        }
    }
}