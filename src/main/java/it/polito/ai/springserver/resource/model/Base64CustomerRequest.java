package it.polito.ai.springserver.resource.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.polito.ai.springserver.resource.controller.deserializer.Base64CustomerRequestDeserializer;

@JsonDeserialize(using = Base64CustomerRequestDeserializer.class)
public class Base64CustomerRequest {
  private CustomerRequest cr;

  public Base64CustomerRequest(CustomerRequest cr) {
    this.cr = cr;
  }

  public CustomerRequest getCr() {
    return cr;
  }
}
