package it.polito.ai.springserver.resource.controller.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polito.ai.springserver.resource.model.Base64CustomerRequest;
import it.polito.ai.springserver.resource.model.CustomerRequest;
import org.bouncycastle.util.encoders.Base64;

import java.io.IOException;

public class Base64CustomerRequestDeserializer extends StdDeserializer<Base64CustomerRequest> {

  public Base64CustomerRequestDeserializer() {
    this(null);
  }

  public Base64CustomerRequestDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public Base64CustomerRequest deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
    CustomerRequest currRequest = (new ObjectMapper()).
            readValue((new String(Base64.decode(jsonParser.toString()))), CustomerRequest.class);
    return new Base64CustomerRequest(currRequest);
  }
}
