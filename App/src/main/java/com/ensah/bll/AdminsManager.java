package com.ensah.bll;

import com.ensah.bo.CadreAdministrateur;
import com.ensah.bo.Compte;
import com.ensah.dao.CadreAdministrateurDao;
import com.ensah.dao.DataBaseException;
import com.ensah.utils.GeneratePassword;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AdminsManager {



    public boolean connectAdmin(String login, String password) throws DataBaseException {
        CadreAdministrateurDao adminDao = new CadreAdministrateurDao();
        if(adminDao.verifyLogin(login , password))
            return true;
        return false;
    }


}

