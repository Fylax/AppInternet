package it.polito.server.model;

import java.util.List;

public class User {

  private int uid;
  private String username;
  private String email;
  private UserStatus userStatus;
  private PositionManager positionManager;

  public User(int uid, String name, String email, UserStatus userStatus) throws ConnectionException {
    this.uid = uid;
    this.username = name;
    this.email = email;
    this.userStatus = userStatus;
    this.positionManager = new PositionManager(this);
  }

  public int getUid() {
    return this.uid;
  }

  public String getUsername() {
    return username;
  }

  public void addPositions(List<Position> positions) throws PositionException, ConnectionException {
      this.positionManager.add(positions);
  }


  public List<Position> getPositions(String start, String end) throws ConnectionException {
    return this.positionManager.get(start, end);
  }
}
