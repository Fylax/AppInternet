package it.polito.server.model;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class User {
  private ReadWriteLock lock = new ReentrantReadWriteLock(true);
  private Positions positions = new Positions();

  private int uid;
  private String username;
  private String email;
  private UserStatus userStatus;

  public User(int uid, String name, String email, UserStatus userStatus) {
    this.uid = uid;
    this.username = name;
    this.email = email;
    this.userStatus = userStatus;
  }


  public void addPositions(List<Position> positions) throws PositionException {
    this.lock.writeLock().lock();
    try {
      this.positions.addPositions(positions);
    } finally {
      this.lock.writeLock().unlock();
    }
  }


  public List<Position> getPositions(String startString, String endString) {
    long start;
    try {
      start = Math.round(Double.valueOf(startString));
    } catch (NumberFormatException | NullPointerException e) {
      start = 0;
    }
    long end;
    try {
      end = Math.round(Double.valueOf(endString));
    } catch (NumberFormatException | NullPointerException e) {
      end = Long.MAX_VALUE;
    }
    List<Position> positions;
    this.lock.readLock().lock();
    try {
      positions = this.positions.getPositions(start, end);
    } finally {
      this.lock.readLock().unlock();
    }
    return positions;
  }
}
