package it.polito.ai.springserver.resource.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.util.encoders.Base64;

import java.io.IOException;

public class Base64CustomerRequest {
  private CustomerRequest cr;

  public Base64CustomerRequest(String json) throws IOException {
    this.cr = (new ObjectMapper()).
            readValue((new String(Base64.decode(json))), CustomerRequest.class);

  }

  public CustomerRequest getCr() {
    return cr;
  }
}
