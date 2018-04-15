package it.polito.server.controller;

import it.polito.server.model.DbConnection;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
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
  }
    @Override
  public void contextDestroyed(ServletContextEvent sce) {

  }
}
