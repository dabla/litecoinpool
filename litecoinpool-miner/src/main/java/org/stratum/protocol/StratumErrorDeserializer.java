package org.stratum.protocol;

import static org.stratum.protocol.ErrorCode.errorCode;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@SuppressWarnings("serial")
public class StratumErrorDeserializer extends StdDeserializer<StratumError> {
    public StratumErrorDeserializer() {
        this(null);
    }
   
    public StratumErrorDeserializer(Class<StratumError> type) {
        super(type);
    }
 
	@Override
	public StratumError deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
		JsonNode node = parser.getCodec().readTree(parser);
		
		if (node.isArray() && node.size() == 3) {
			return new StratumError(errorCode(node.get(0).asInt()),
												  node.get(1).asText(),
												  node.get(2).asText());
		}
		
		return null;
	}
	
}