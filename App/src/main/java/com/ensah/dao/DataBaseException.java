package com.ensah.dao;


import javax.swing.*;

public class DataBaseException extends Exception{
    public DataBaseException(){
        super("Erreur base de données");
    }

    public DataBaseException(Throwable ex){
        super(ex);


    }
}
