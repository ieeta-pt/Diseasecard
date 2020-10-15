package pt.ua.diseasecard.components.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {

    private Connection connection;
    private Statement statement;
    private String connectionString = "";
    private String database = "";
    private String build = "";

    public DB() {
    }

    public DB(String database) {
        this.database = database;
    }

    public DB(String database, String connectionString) {
        this.database = database;
        this.connectionString = connectionString;
    }


    public void connect() {
        try {
            connection = DriverManager.getConnection(connectionString);
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("[Diseasecard][DB] Unable to connect to " + database + "\n\t" + e.toString());
        }
    }

    public void close() {
        try {
            if (!statement.isClosed()) {
                statement.close();
            }
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("[DB] Unable to close " + database + " connection\n\t" + e.toString());
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
