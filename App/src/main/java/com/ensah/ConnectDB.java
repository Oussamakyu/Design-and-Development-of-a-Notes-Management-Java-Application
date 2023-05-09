package com.ensah;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Oussama BENABBOU
 * this class isn't complete yet
 *
 * **/

public class ConnectDB {
    private static final String HOST="localhost";
    private static final int PORT= 3306; //mysql port
    private static final String DB_NAME="java_app_db";
    private static final String USERNAME="root";
    private static final String PASSWORD="password";

    private static Connection connection;

    public static Connection getConnection(){
        try {
            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DB_NAME), USERNAME, PASSWORD);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

}
