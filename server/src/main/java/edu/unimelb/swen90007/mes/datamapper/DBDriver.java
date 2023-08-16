package edu.unimelb.swen90007.mes.datamapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBDriver {

    private static Connection connection;
    private static final Logger logger = LogManager.getLogger(DBDriver.class);

    public static Connection getConnection() {
        if (connection == null) {
            Properties prop = new Properties();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("db.properties");
            try {
                prop.load(inputStream);
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(
                        prop.getProperty("url"),
                        prop.getProperty("user"),
                        prop.getProperty("password")
                );
            } catch (Exception e) {
                logger.error("Error connecting to database: " + e.getMessage());
            }
        }
        return connection;
    }

}
