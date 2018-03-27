package it.polito.ese1.model;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class User {
  private ReadWriteLock lock = new ReentrantReadWriteLock(true);
  private GlobalPositions positions = new GlobalPositions();

  private String name;

  public User(String name) {
    this.name = name;
  }


  public void addPositions(List<GlobalPosition> positions) throws PositionException {
    this.lock.writeLock().lock();
    this.positions.addPositions(positions);
    this.lock.writeLock().unlock();
  }

  public List<GlobalPosition> getPositions() {
    this.lock.readLock().lock();
    var positions = this.positions.getPositions();
    this.lock.readLock().unlock();
    return positions;
  }
}
