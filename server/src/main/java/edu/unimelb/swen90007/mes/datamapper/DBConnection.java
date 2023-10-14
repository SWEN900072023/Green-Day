package edu.unimelb.swen90007.mes.datamapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final ThreadLocal<Connection> current = new ThreadLocal<>();
    private static final Logger logger = LogManager.getLogger(DBConnection.class);

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
        if (current.get() == null)
            setCurrent();
        return current.get();
    }

    public static void closeConnection() {
        if (current.get() != null) {
            try {
                current.get().close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
