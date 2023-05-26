package com.ensah.ihm;

import com.ensah.ConnectDB;
import com.ensah.dao.DBConnect;

import java.sql.Connection;

public class main {
    public static void main(String[] args) throws Exception {
        Connection con = DBConnect.getInstance();
        if (con == null){
            System.out.println("connexion non établi");
        }


    }
}
