package org.smartwallet.stratum;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class StratumMethodSerializer extends StdSerializer<StratumMethod> {
    public StratumMethodSerializer() {
        this(null);
    }
   
    public StratumMethodSerializer(Class<StratumMethod> type) {
        super(type);
    }
 
    @Override
    public void serialize(StratumMethod method, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
        generator.writeString(method.getValue());
    }
}