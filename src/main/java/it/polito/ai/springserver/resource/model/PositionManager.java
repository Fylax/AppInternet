package it.polito.ai.springserver.resource.model;

import it.polito.ai.springserver.resource.model.repository.PositionRepositoryInterfaceCustom;

/**
 * This class contains method to check validity of new positions
 */
public class PositionManager {

  private Position cachedPosition;
  private final HaversineDistance distance;

  public PositionManager(long userId, PositionRepositoryInterfaceCustom repo) {
    this.distance = new HaversineDistance();
    this.cachedPosition = repo.getUserLatestPosition(userId);
  }

  public boolean checkPositionValidity(Position position) {
    boolean valid = cachedPosition == null ||
                    (position.getTimestamp() > cachedPosition.getTimestamp() &&
                    ((distance.getDistance(position, cachedPosition)) /
                     (position.getTimestamp() - cachedPosition.getTimestamp())) < 100);
    if (valid) {
      cachedPosition = position;
    }
    return valid;
  }
}
