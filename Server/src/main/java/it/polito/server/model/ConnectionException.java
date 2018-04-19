package it.polito.server.model;

public class ConnectionException extends Exception {
  public ConnectionException(Exception e) {
    super(e);
  }
}
