package it.polito.server.model.dao.postgres;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

class PostgresConnection {
  private static PostgresConnection dbConn;
  private DataSource dataSource;

  private PostgresConnection() {
    try {
      Context initialContext = new InitialContext();
      Context environmentContext = (Context) initialContext.lookup("java:comp/env");

      this.dataSource = (DataSource) environmentContext.lookup("jdbc/postgres");
      if (this.dataSource == null) {
        throw new RuntimeException();
      }
    } catch (NamingException e) {
      throw new RuntimeException();
    }
  }

  static Connection getConnection() throws SQLException {
    if (dbConn == null) {
      dbConn = new PostgresConnection();
    }
    return dbConn.dataSource.getConnection();
  }

}
