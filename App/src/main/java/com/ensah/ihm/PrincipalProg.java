package com.ensah.ihm;

import com.ensah.bll.*;
import com.ensah.bo.Etudiant;
import com.ensah.dao.CadreAdministrateurDao;
import com.ensah.dao.DataBaseException;
import com.ensah.utils.ExcelImport;
import com.ensah.utils.FilterExcel;
import org.apache.log4j.Logger;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class PrincipalProg {
    private static Logger LOGGER = Logger.getLogger(Main.class);
    public static void printMenu() {
        /**
         * Affiche le menu de l'application
         */

        System.out.println("Ajouter un nouveau étudiant depuis le fichier excel	: 1 ");
        System.out.println("Ajouter des délibérations depuis le fichier excel : 2 ");
        System.out.println("Exporter le fichier des délibérations : 3 ");
        System.out.println("Gérer la structure et les éléments pédagogiques : 4 ");
        System.out.println("5-   ");
        System.out.println("0-	Sortir ");
    }

    public static void main(String[] args) throws BusinessLogicException, DataBaseException, ImportException, IOException {
        StructureManager structureManager = new StructureManager();
        String logo = "\n" +
                " __   ___  __  ___    __           __   ___  __           __  ___  ___  __  \n" +
                "/ _` |__  /__`  |  | /  \\ |\\ |    |  \\ |__  /__`    |\\ | /  \\  |  |__  /__` \n" +
                "\\__> |___ .__/  |  | \\__/ | \\|    |__/ |___ .__/    | \\| \\__/  |  |___ .__/ \n" +
                "                                                                            \n";
        System.out.println(logo);
        //création des instances de la classe qui gèrent la logique métier
        NotesManager notesManager = new NotesManager();
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
                System.out.println("Welcome mr l'admin " ); // to retrieve name later
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
                System.out.println("Entrer le chemain du fichier excel: ");
                String path = sc.nextLine();

                try{
                    ExcelImport excelImport = new ExcelImport(path);

                    FilterExcel test = new FilterExcel();
                    try{
                        test.importStudent(excelImport.readExcel(excelImport.getSheet()));
                    }catch (ImportException imex){
                        System.err.print(imex.getMessage());
                    }
                    StudentManager studentManager = new StudentManager();

                    boolean checkStatus = true;
                    for(Etudiant newEtudiant : test.getNewStudents()){
                        try{
                            studentManager.checkInscriptionEtudiant(newEtudiant);
                        }catch (BllException blex){
                            System.err.println(blex.getMessage());
                            checkStatus = false;
                            break;
                        }catch (DataBaseException dbex){
                            System.err.print(dbex.getMessage());
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
                            System.err.print(dbex.getMessage());
                            checkStatus = false;
                            break;
                        }
                    }
                    if(checkStatus) {
                        for (Etudiant newEtudiant : test.getNewStudents()) {
                            try{
                                studentManager.inscriptionEtudiant(newEtudiant);
                            }catch (DataBaseException dbex){
                                System.err.print(dbex.getMessage());
                            }
                        }
                        for(int i=0;i<test.getOldStudents().size();i+=2){
                            try{
                                studentManager.reInscriptionEtudiant((Etudiant) test.getOldStudents().get(i),Long.parseLong(String.valueOf(test.getOldStudents().get(i+1))));
                            }catch (DataBaseException dbex){
                                System.err.print(dbex.getMessage());
                            }
                        }
                    }
                }catch(ImportException ex){
                    System.err.print(ex.getMessage());
                }
                break;
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
                break;
            case 3 :
                //Question sur l'Alias
                DeliberationManager deliberationManager = new DeliberationManager();
                String niveauAlias = sc.nextLine();
                try{
                    deliberationManager.exportDeliberation(niveauAlias);
                }catch (BllException blex){
                    System.err.println(blex.getMessage());
                }catch (FileNotFoundException fex){
                    System.err.print(fex.getMessage());
                }

                break;
            case 4 :
                System.out.println("Modifier/Supprimer/Créer les éléments, les modules, les classes, et les filières : 1 ");
                System.out.println("Associer des modules à une classe (niveau) : 2 ");
                System.out.println("Associer des éléments à un module : 3 ");
                System.out.println("Associer des classes à une filière : 4 ");
                System.out.println("Consulter les modules d’une classe : 5 ");
                System.out.println("Affecter un coordonnateur à la filière : 6 ");
                System.out.println("Saisir le numéro de votre choix: ");
                Scanner sc2 = new Scanner(System.in);
                int choix2 = sc2.nextInt();
                sc2.nextLine();
                switch (choix2){
                    case 1 :
                        break;
                    case 2 :
                        System.out.println("Entrer le nom de la classe : ");
                        String nomCl = sc2.nextLine();
                        System.out.println("Combien de modules voulez-vous associer à cette classe ? ");
                        int num = Integer.parseInt(sc2.nextLine());

                        for (int i = 0; i < num; i++) {
                            System.out.println("Entrer le titre du module à ajouter : ");
                            String titre = sc2.nextLine();
                            System.out.println(titre);
                            boolean x = structureManager.associateModuleWithNiveau(structureManager.getIdModule(titre), structureManager.getIdNiveau(nomCl));
                            if(x)
                                System.out.println("Ajouté avec succès");
                            else
                                System.out.println("sir tn3s");
                            break;
                        }


                        break;



                }

            case 0 :
                System.out.println("Bye!");
                System.exit(0);


        }

        }
        }
    }

