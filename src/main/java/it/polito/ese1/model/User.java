package it.polito.ese1.model;

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
    }
    catch(PositionException e){
      this.lock.writeLock().unlock();
      throw e;
    }
    this.lock.writeLock().unlock();
  }


  public List<Position> getPositions(String startString, String endString) {
    long start =  startString == null ? 0 : Long.valueOf(startString);
    long end =  endString == null ? Long.MAX_VALUE : Long.valueOf(endString);
    this.lock.readLock().lock();
    var positions = this.positions.getPositions(start, end);
    this.lock.readLock().unlock();
    return positions;
  }
}
