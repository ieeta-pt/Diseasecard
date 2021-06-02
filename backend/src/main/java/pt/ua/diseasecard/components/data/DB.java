package pt.ua.diseasecard.components.data;

import pt.ua.diseasecard.service.DataManagementService;

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

    /*
        Method to connect to DB. It tries 10 times until gives up.
     */
    public void connect() {

        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                this.connection = null;
            }
        }

        int count = 0;
        while (this.connection == null && count < 10)
        {
            try {
                connection = DriverManager.getConnection(connectionString);
                statement = connection.createStatement();
            } catch (SQLException e) {
                count++;
                Logger.getLogger(DB.class.getName()).log(Level.INFO, "[Diseasecard][DB] Unable to connect to " + database);
            }
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
            Logger.getLogger(DB.class.getName()).log(Level.INFO, "[DB] Unable to close " + database + " connection\n\t" + e.toString());
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
