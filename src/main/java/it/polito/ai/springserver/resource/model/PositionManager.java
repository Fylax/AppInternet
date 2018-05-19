package it.polito.ai.springserver.resource.model;

import it.polito.ai.springserver.resource.model.repository.LatestUserPositionRepository;

public class PositionManager {

  private Position cachedPosition = null;
  private final HaversineDistance distance;

  public PositionManager(long userId, LatestUserPositionRepository repo) {
    this.distance = new HaversineDistance();
    this.cachedPosition = repo.findByUseridLatestPosition(userId);
  }

  public boolean checkPositionValidity(Position position) {
    boolean valid = position.getTimestamp() > cachedPosition.getTimestamp() &&
                    ((distance.getDistance(position, cachedPosition)) /
                     (position.getTimestamp() - cachedPosition.getTimestamp())) < 100;
    if (valid) {
      cachedPosition = position;
    }
    return valid;
  }
}
