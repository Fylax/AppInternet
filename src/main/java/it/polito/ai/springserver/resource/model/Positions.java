package it.polito.ai.springserver.resource.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.polito.ai.springserver.resource.controller.deserializer.UserPositionDeserializer;

import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using = UserPositionDeserializer.class)
public class Positions {
  private List<Position> positionList;

  public Positions(Position pos) {
    this.positionList = new ArrayList<>(1);
    this.positionList.add(pos);
  }

  public Positions(List<Position> positions) {
    this.positionList = positions;
  }

  public List<Position> getPositionList() {
    return this.positionList;
  }
}
