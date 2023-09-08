package edu.unimelb.swen90007.mes.datamapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    private static final Logger logger = LogManager.getLogger(DBConnection.class);
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("Class Not Located: " + e.getMessage());
        }

        if (connection == null) {
            try {
                connection = DriverManager.getConnection(
                        System.getProperty("jdbc.url"),
                        System.getProperty("jdbc.user"),
                        System.getProperty("jdbc.password")
                );
            } catch (SQLException e) {
                logger.error(e.getMessage());
                throw e;
            }
        }

        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
