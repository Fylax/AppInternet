package it.polito.server.model;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DbConnection {
  private static DbConnection dbConn;
  private DataSource dataSource;

  private DbConnection() {
    try {
      Context initialContext = new InitialContext();
      Context environmentContext = (Context) initialContext.lookup("java:comp/env");

      this.dataSource = (DataSource) environmentContext.lookup("jdbc/db");
      if (this.dataSource == null) {
        throw new RuntimeException();
      }
    } catch (NamingException e) {
      throw new RuntimeException();
    }
  }

  static Connection getConnection() throws SQLException {
    if (dbConn == null) {
      dbConn = new DbConnection();
    }
    return dbConn.dataSource.getConnection();
  }

}
