package edu.unimelb.swen90007.mes.datamapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private Connection connection;
    private static final Logger logger = LogManager.getLogger(DBConnection.class);

    public DBConnection() {
        Properties prop = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("db.properties");
        try {
            prop.load(inputStream);
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(
                    prop.getProperty("url"),
                    prop.getProperty("user"),
                    prop.getProperty("password")
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
