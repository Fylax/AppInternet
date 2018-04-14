package it.polito.server.controller;

import it.polito.server.model.DbConnection;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebListener
public class ServerContextListener implements ServletContextListener {
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      throw new ExceptionInInitializerError();
    }
    try {
      Connection conn = DbConnection.getInstance().getConnection();
      try (Statement statement = conn.createStatement()) {
        String s = "SELECT * FROM users";
        ResultSet res = statement.executeQuery(s);
        while(res.next()){
          //TODO: popoliamo qui la mappa degli utenti?
        }
        res.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      //throw new ExceptionInInitializerError();
    }
  }
    @Override
  public void contextDestroyed(ServletContextEvent sce) {

  }
}
