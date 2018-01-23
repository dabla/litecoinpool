package org.smartwallet.stratum;

import static org.smartwallet.stratum.StratumMethod.stratumMethod;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@SuppressWarnings("serial")
public class StratumMethodDeserializer extends StdDeserializer<StratumMethod> {
    public StratumMethodDeserializer() {
        this(null);
    }
   
    public StratumMethodDeserializer(Class<StratumMethod> type) {
        super(type);
    }
 
	@Override
	public StratumMethod deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
		JsonNode node = parser.getCodec().readTree(parser);
		return stratumMethod(node.asText());
	}
}