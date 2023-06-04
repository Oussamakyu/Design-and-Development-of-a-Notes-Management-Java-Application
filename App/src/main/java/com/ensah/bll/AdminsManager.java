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





    public static void saveAdminAndCompte(CadreAdministrateur admin) throws DataBaseException {
        CadreAdministrateurDao adminDao = new CadreAdministrateurDao();

        // Save the admin information
        adminDao.saveCadreAdministrateur(admin);

        // Retrieve the admin ID after saving
        Long adminId = adminDao.getAdminIdByEmail(admin.getEmail());

        // Create a compte object for the admin
        Compte compte = new Compte();
        compte.setIdCompte(adminId);
        compte.setLogin(admin.getEmail()); // Assuming the email is used as the login
        compte.setPassword(generateRandomPassword()); // Generate a random password

        // Save the compte information
        adminDao.saveCompte(compte);
    }

    public boolean connectAdmin(String login, String password) throws DataBaseException {
        CadreAdministrateurDao adminDao = new CadreAdministrateurDao();
        if(adminDao.verifyLogin(login , password))
            return true;
        return false;
    }

    private static String generateRandomPassword() {
        GeneratePassword g = new GeneratePassword(7);
        return g.getMdp();
    }
}

