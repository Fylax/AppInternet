package it.polito.clientDb;

import java.sql.*;

public class CreateDb {

  public static void main(String[] args) {
    try{
      Class.forName("org.postgresql.Driver");
    }catch (ClassNotFoundException e){
      System.exit(-1);
    }
    try {
      Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db", "root", "pass");
      createDatabase(con);
      con.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  private static void createDatabase(Connection c) throws SQLException{
    Statement s= null;
    try {
      String query="CREATE TYPE status AS ENUM ('banned', 'approved', 'awaiting');" +
              "CREATE TABLE user (" +
              "id SERIAL PRIMARY KEY," +
              "username VARCHAR(100)," +
              "secret CHAR(60)," +
              "email VARCHAR(100)," +
              "status status);";
      s = c.createStatement();
      s.executeQuery(query);
    } finally {
      try { if (s!=null) s.close();}
      catch(Exception e){
        System.out.println(e.getMessage());
        e.printStackTrace();
      }
    }
  }

}
