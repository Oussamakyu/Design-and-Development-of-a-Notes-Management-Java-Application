package com.ensah.ihm;

import com.ensah.bll.*;
import com.ensah.bo.Etudiant;
import com.ensah.dao.CadreAdministrateurDao;
import com.ensah.dao.DataBaseException;
import com.ensah.utils.ExcelImport;
import com.ensah.utils.FilterExcel;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class PrincipalProg {
    private static Logger LOGGER = Logger.getLogger(Main.class);

    public static void printMenu() {
        /**
         * Affiche le menu de l'application
         */

        System.out.println("1-	 ");
        System.out.println("2-	 ");
        System.out.println("3-	 ");
        System.out.println("4-	 ");
        System.out.println("5-   ");
        System.out.println("0-	Sortir ");
    }

    public static void main(String[] args) throws BusinessLogicException, DataBaseException, ImportException {
        String logo = "\n" +
                " __   ___  __  ___    __           __   ___  __           __  ___  ___  __  \n" +
                "/ _` |__  /__`  |  | /  \\ |\\ |    |  \\ |__  /__`    |\\ | /  \\  |  |__  /__` \n" +
                "\\__> |___ .__/  |  | \\__/ | \\|    |__/ |___ .__/    | \\| \\__/  |  |___ .__/ \n" +
                "                                                                            \n";
        System.out.println(logo);
        //création des instances de la classe qui gèrent la logique métier
        NotesManager notesManager = new NotesManager();
        StudentManager studentManager = new StudentManager();
        CadreAdministrateurDao cadreAdministrateurDao = new CadreAdministrateurDao(); // to remove later
//        try {
//            if (!DBInstaller.checkIfAlreadyInstalled()) {
//                DBInstaller.createDataBaseTables();
//                LOGGER.info("La base de données est crée correctement");
//            }
//        } catch (Exception ex) {
//            System.err.println("Erreur lors de la création de la base de données, consultez le fichier log.txt pour plus de détails");
//            System.exit(-1);
//        }



//        ==========================================================================================
//        reading the admin infos in the first implementation // Read the admin information from the properties file
//        CadreAdministrateur admin = readAdminInfo();
//
//        // Save the admin and compte information to the database
//        saveAdminAndCompte(admin);

//        ==========================================================================================
        Scanner sc = new Scanner(System.in);
        boolean connect = false;
        while(!connect) {
            System.out.println("Entrer votre login : ");
            String login = sc.nextLine();
            System.out.println("Entrer votre mot de passe : ");
            String password = sc.nextLine();
            AdminsManager admMan = new AdminsManager();
            connect = admMan.connectAdmin(login, password);
            if(connect)
                System.out.println("Welcome mr "+ cadreAdministrateurDao.getCadreAdministrateurById(cadreAdministrateurDao.getAdminIdByEmail(login)).getNom());
        }

        while (connect) {
            //afficher le menu
            printMenu();
            //lire le choix
            System.out.println("Saisir le numéro de votre choix: ");
            int choix = sc.nextInt();
            sc.nextLine();

        switch (choix) {
            case 1:
                String Path = "C:\\Users\\asus\\Desktop\\Java\\Project\\Design-and-Development-of-a-Notes-Management-Java-Application\\App\\database\\nouveauEtudiant.xlsx";
                ExcelImport excelImport = new ExcelImport(Path);
                FilterExcel test = new FilterExcel();
                test.importStudent(excelImport.readExcel(excelImport.getSheet()));

                boolean checkStatus = true;
                for(Etudiant newEtudiant : test.getNewStudents()){
                    try{
                        studentManager.checkInscriptionEtudiant(newEtudiant);
                    }catch (BllException blex){
                        System.err.println(blex.getMessage());
                        checkStatus = false;
                        break;
                    }catch (DataBaseException dbex){
                        System.err.print(dbex);
                        checkStatus = false;
                        break;
                    }
                }
                for(int i=0;i<test.getOldStudents().size();i+=2){
                    try{
                        studentManager.checkReInscriptionEtudiant((Etudiant) test.getOldStudents().get(i),Long.parseLong(String.valueOf(test.getOldStudents().get(i+1))));
                    }catch (BllException ex){
                        System.err.println(ex.getMessage());
                        checkStatus = false;
                        break;
                    }catch (DataBaseException dbex){
                        System.err.print(dbex);
                        checkStatus = false;
                        break;
                    }
                }
                if(checkStatus) {
                    for (Etudiant newEtudiant : test.getNewStudents()) {
                        try{
                            studentManager.inscriptionEtudiant(newEtudiant);
                        }catch (DataBaseException dbex){
                            System.err.print(dbex);
                        }
                    }
                    for(int i=0;i<test.getOldStudents().size();i+=2){
                        try{
                            studentManager.reInscriptionEtudiant((Etudiant) test.getOldStudents().get(i),Long.parseLong(String.valueOf(test.getOldStudents().get(i+1))));
                        }catch (DataBaseException dbex){
                            System.err.print(dbex);
                        }
                    }
                }
            case 2:
                try {
                    if(notesManager.verifierExistanceEnregistrements()){
                        System.out.println("Are you sure that you want to modify deliberations?(No to stop)");
                    }
                    String answer = sc.nextLine();
                    if(answer.equals("No"))
                        return;
                    notesManager.insererNotesFinales();
                    System.out.println("Notes finales inserted successfully.");
                } catch (IOException | SQLException | DataBaseException e) {
                    e.printStackTrace();
                }
            case 3 :
                

        }

        }
        }
    }

