package com.ensah.ihm;

import com.ensah.bll.NotesManager;
import com.ensah.dao.DBInstaller;
import org.apache.log4j.Logger;

import java.util.Properties;

public class Main {
    private static Logger LOGGER = Logger.getLogger(Main.class);
    public static void main(String[] args) throws Exception {
        Properties config = new Properties();
        NotesManager notesManager = new NotesManager();
        try {
            //On vérifie que la base de données n'est pas encore crée
            if (!DBInstaller.checkIfAlreadyInstalled()) {
                //DBInstaller.installDataBase();
                //Créer la base de données
                DBInstaller.createDataBaseTables();
                LOGGER.info("La base de données est crée correctement");
            }
        } catch (Exception ex) {
            //Dans le cas d'une erreur dans la création des tables on affiche un message d'erreur
            System.err.println("Erreur lors de la création de la base de données, consultez le fichier log.txt pour plus de détails");
            //On arrete l'application ici avec un code d'erreur
            System.exit(-1);
        }

    }
}
