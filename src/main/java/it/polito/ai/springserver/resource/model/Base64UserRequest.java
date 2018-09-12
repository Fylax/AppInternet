package it.polito.ai.springserver.resource.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.util.encoders.Base64;

import java.io.IOException;

public class Base64UserRequest {
  private UserRequest cr;

  public Base64UserRequest(String json) throws IOException {
    this.cr = (new ObjectMapper()).
            readValue((new String(Base64.decode(json))), UserRequest.class);

  }

  public UserRequest getCr() {
    return cr;
  }
}
