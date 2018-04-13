package it.polito.clientDb;

import java.sql.*;

public class CreateDb {

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
            statement.addBatch("DROP TABLE IF EXISTS positions;");
            statement.addBatch("DROP TABLE IF EXISTS users;");
            statement.addBatch("DROP TYPE IF EXISTS user_status;");
            statement.addBatch("CREATE TYPE user_status AS ENUM ('banned', 'approved', 'awaiting');");

            statement.addBatch("CREATE TABLE users (" +
                    "uid SERIAL PRIMARY KEY," +
                    "username VARCHAR(100) NOT NULL UNIQUE," +
                    "secret CHAR(60) NOT NULL," +
                    "email VARCHAR(100) NOT NULL UNIQUE," +
                    "status user_status NOT NULL DEFAULT 'awaiting');");

            statement.addBatch("CREATE TABLE positions (" +
                    "pos_id BIGSERIAL PRIMARY KEY," +
                    "t_stamp timestamp NOT NULL," +
                    "location point NOT NULL," +
                    "user_id INTEGER NOT NULL REFERENCES users(uid))");

            statement.addBatch("INSERT INTO users VALUES(DEFAULT, " +
                    "'corrado'," +
                    "'$2a$10$dBIKrfUFTozThRxKL5bH8uZC9xOwdepJI5EXLIL0ZCz2N.BIUygi6'," +
                    "'prova@hotmail.it'," +
                    "'approved')");
            statement.addBatch("INSERT INTO users VALUES(DEFAULT, " +
                    "'elena'," +
                    "'$2a$10$sKW4LyhqK3D/ZEZlyJaMYuWT8Hi/EXw4wYw.V1DOQqxkxK/kHaxIO'," +
                    "'prova1@hotmail.it'," +
                    "'approved')");
            statement.addBatch("INSERT INTO users VALUES(DEFAULT, " +
                    "'ciccio'," +
                    "'$2a$10$zAbz3Qd3nN.sTsinoy6qYO6f6K.cGYA6lzIaiE2mgNyjhETF/SwK2'," +
                    "'prova2@hotmail.it'," +
                    "'approved')");
            statement.executeBatch();

        } catch (java.sql.BatchUpdateException eb) {
            throw eb.getNextException();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
