package it.polito.server.controller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

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
