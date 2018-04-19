package it.polito.server.model.dao.postgres;

import it.polito.server.model.ConnectionException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

class PostgresConnection {
  private static PostgresConnection dbConn;
  private DataSource dataSource;

  private PostgresConnection() throws ConnectionException {
    try {
      Context initialContext = new InitialContext();
      Context environmentContext = (Context) initialContext.lookup("java:comp/env");

      this.dataSource = (DataSource) environmentContext.lookup("jdbc/postgres");
    } catch (NamingException e) {
      throw new ConnectionException(e);
    }
  }

  static Connection getConnection() throws SQLException {
    if (dbConn == null) {
      try {
        dbConn = new PostgresConnection();
      }catch(ConnectionException e){
        dbConn = null;
        throw new SQLException(e);
      }
    }
    return dbConn.dataSource.getConnection();
  }

}
