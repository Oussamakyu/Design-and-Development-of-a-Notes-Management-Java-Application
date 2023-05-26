package com.ensah.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnect {
    private static String dbUrl;
    private static String login;
    private static String password;
    private static String driver;
    private static Connection connection;

    private DBConnect() throws Exception {
        Properties dbProperties = DbPropertiesLoader.loadPoperties("conf.properties");
        dbUrl = dbProperties.getProperty("db.url");
        login = dbProperties.getProperty("db.login");
        password = dbProperties.getProperty("db.password");
        connection = DriverManager.getConnection(dbUrl, login, password);
    }

    public static Connection getInstance() throws Exception {
        if (connection == null) {
            new DBConnect();
        }

        return connection;
    }
}