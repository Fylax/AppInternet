package it.polito.clientDb;

import java.sql.*;

public class CreateDb {

  public static void main(String[] args) {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
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

  private static void createDatabase(Connection c) throws SQLException {
    try (Statement statement = c.createStatement()) {
      statement.addBatch("DROP TYPE IF EXISTS user_status;");
      statement.addBatch("DROP TABLE IF EXISTS users;");
      statement.addBatch("CREATE TYPE user_status AS ENUM ('banned', 'approved', 'awaiting');");
      statement.addBatch("CREATE TABLE users (" +
                         "id SERIAL PRIMARY KEY," +
                         "username VARCHAR(100) NOT NULL UNIQUE," +
                         "secret CHAR(60) NOT NULL," +
                         "email VARCHAR(100) NOT NULL UNIQUE," +
                         "status user_status NOT NULL DEFAULT 'awaiting');");
      statement.executeBatch();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }

}
