package it.polito.ai.springserver.resource.model.repository;

import it.polito.ai.springserver.resource.model.Position;

public interface LatestUserPositionRepository {
  Position findByUseridLatestPosition(long userid);
}
