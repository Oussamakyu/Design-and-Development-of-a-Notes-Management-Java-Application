package com.ensah.dao;


import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


public class DBConnection {

    private Logger logger = Logger.getLogger(DBConnection.class);
    private static Connection connection;
    private static String dbUrl;
    private static String login;
    private static String password;

    private DBConnection() throws DataBaseException {
        try {
            // Load configuration from conf.properties file
            Properties dbProperties = DbPropertiesLoader.loadProperties("conf.properties");
            dbUrl = dbProperties.getProperty("db.url");
            login = dbProperties.getProperty("db.login");
            password = dbProperties.getProperty("db.password");


            // Create a connection to the database
            connection = DriverManager.getConnection(dbUrl, login, password);

        } catch (Exception e) {
            logger.error(e);
            throw new DataBaseException(e);
        }
    }

    public static Connection getInstance() throws DataBaseException {
        if (connection == null) {
            try {
                new DBConnection();
            } catch (DataBaseException e) {
                // Handle the exception appropriately (e.g., log, throw, etc.)
                throw e;
            }
        }

        return connection;
    }
}