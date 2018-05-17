package it.polito.ai.springserver.resource.model;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class PurchasedPosition {
  @DBRef
  private Position position;
  private boolean payed;

  public PurchasedPosition(Position position) {
    this.position = position;
    this.payed = false;
  }

  public Position getPosition() {
    return position;
  }

  public boolean isPayed() {
    return payed;
  }
}
