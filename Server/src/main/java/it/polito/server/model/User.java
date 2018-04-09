package it.polito.server.model;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class User {
  private ReadWriteLock lock = new ReentrantReadWriteLock(true);
  private Positions positions = new Positions();

  private String name;

  public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  private String pwd;

  public User(String name, String pwd) {
    this.name = name;
    this.pwd = pwd;
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
