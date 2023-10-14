package edu.unimelb.swen90007.mes.datamapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final ThreadLocal<Connection> current = new ThreadLocal<>();
    private static final Logger logger = LogManager.getLogger(DBConnection.class);
    private static Connection connection;

    private DBConnection() {}

    public static void setCurrent() throws SQLException {
        Connection threadConnection = DriverManager.getConnection(
                System.getProperty("jdbc.url"),
                System.getProperty("jdbc.user"),
                System.getProperty("jdbc.password")
        );
        current.set(threadConnection);
    }

    public static Connection getConnection() throws SQLException {
        if (current.get() != null) return current.get();
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

    public static Connection getCurrent() throws SQLException {
        if (current.get() == null)
            setCurrent();
        return current.get();
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
