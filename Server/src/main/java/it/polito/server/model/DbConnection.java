package it.polito.server.model;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DbConnection {
  private static DbConnection dbConn;
  private DataSource dataSource;

  private DbConnection(){
    try {
      InitialContext cxt = new InitialContext();
      this.dataSource = (DataSource) cxt.lookup( "java:/comp/env/jdbc/postgres" );
      if(this.dataSource == null){
        throw new RuntimeException();
      }
    } catch (NamingException e) {
      throw new RuntimeException();
    }
  }

  public static DbConnection getInstance(){
    if(dbConn == null){
      dbConn = new DbConnection();
    }
    return dbConn;
  }

  public Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

}
