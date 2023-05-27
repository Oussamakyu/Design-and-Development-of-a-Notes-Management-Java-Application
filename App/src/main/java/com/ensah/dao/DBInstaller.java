package com.ensah.dao;

import com.ensah.utils.FileManager;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.sql.*;
import java.util.Properties;


public class DBInstaller {

    private static Logger LOGGER = Logger.getLogger(DBInstaller.class);

    public static boolean installDataBase() throws DataBaseException, IOException {
        Properties dbProperties = DbPropertiesLoader.loadProperties("conf.properties");
        String url = dbProperties.getProperty("db.url");
        String username = dbProperties.getProperty("db.login");
        String password = dbProperties.getProperty("db.password");
        String databaseName = dbProperties.getProperty("db.name");

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            String sql = "CREATE DATABASE " + databaseName;
            statement.executeUpdate(sql);
            System.out.println("Database created successfully");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void createDataBaseTables() throws DataBaseException {

        try {
            Connection con = DBConnection.getInstance();


            String sql = """
                                
                                        
                              
                             
                    """;
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
        }catch (Exception ex){
            LOGGER.error(ex);
            throw  new DataBaseException(ex);
        }

    }

    public static boolean checkIfAlreadyInstalled() throws IOException {
        String userHomeDirectory = System.getProperty("user.home");
        Properties dbProperties = DbPropertiesLoader.loadProperties("conf.properties");
        String url = dbProperties.getProperty("db.url");
        String username = dbProperties.getProperty("db.login");
        String password = dbProperties.getProperty("db.password");
        String databaseName = dbProperties.getProperty("db.name");

        String databaseFile = userHomeDirectory + "\\" + databaseName + ".mv.db";
        System.out.println(databaseName);

        // Check if the database file exists
        boolean databaseExists = FileManager.fileExists(databaseFile);
        if (!databaseExists) {
            return false;
        }

        // Check if the tables are created
        try (Connection connection = DriverManager.getConnection(url + databaseName, username, password);
             Statement statement = connection.createStatement()) {

            // Check if the table exists
            ResultSet resultSet = connection.getMetaData().getTables(null, null, "your_table_name", null);
            boolean tableExists = resultSet.next();
            resultSet.close();

            if (!tableExists) {
                return false;
            }

            // Add more table checks if needed

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //

}

