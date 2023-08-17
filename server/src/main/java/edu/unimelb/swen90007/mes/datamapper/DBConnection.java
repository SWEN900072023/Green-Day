package edu.unimelb.swen90007.mes.datamapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private Connection connection;
    private static final Logger logger = LogManager.getLogger(DBConnection.class);

    public DBConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(
                    System.getProperty("jdbc.uri"),
                    System.getProperty("jdbc.user"),
                    System.getProperty("jdbc.password")
            );
        } catch (Exception e) {
            logger.error("Error connecting to database: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void closeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
    }

}
