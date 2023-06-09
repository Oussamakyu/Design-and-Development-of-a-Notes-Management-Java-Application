package com.ensah.ihm;

import com.ensah.bll.*;
import com.ensah.bo.*;
import com.ensah.bo.Module;
import com.ensah.dao.CadreAdministrateurDao;
import com.ensah.dao.DataBaseException;
import com.ensah.utils.ExcelImport;
import com.ensah.utils.FilterExcel;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    public static void main(String[] args) throws BusinessLogicException, DataBaseException, ImportException {
        StructureManager structureManager = new StructureManager();
        String logo = "\n" +
                " ___           _   _         _                \n" +
                "(  _`\\        ( ) ( )       ( )_              \n" +
                "| (_(_)______ | `\\| |   _   | ,_)   __    ___ \n" +
                "|  _)_(______)| , ` | /'_`\\ | |   /'__`\\/',__)\n" +
                "| (_( )       | |`\\ |( (_) )| |_ (  ___/\\__, \\\n" +
                "(____/'       (_) (_)`\\___/'`\\__)`\\____)(____/\n" +
                "                                              \n" +
                "                                              \n";
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
                System.out.println("hey");
                break;
            case 4 :
                System.out.println("Modifier/Supprimer/Créer les éléments, les modules, les classes, et les filières : 1 ");
                System.out.println("Associer des modules à une classe (niveau) : 2 ");
                System.out.println("Associer des éléments à un module :          3 ");
                System.out.println("Associer des classes à une filière :         4 ");
                System.out.println("Consulter les modules d’une classe :         5 ");
                System.out.println("Affecter un coordonnateur à la filière :     6 ");
                System.out.println("Saisir le numéro de votre choix: ");
                Scanner sc2 = new Scanner(System.in);
                int choix2 = sc2.nextInt();
                sc2.nextLine();
                switch (choix2){
                    case 1 :
                        System.out.println("---------------------------------------");
                        System.out.println("Modifier/Supprimer/Créer les éléments : 1");
                        System.out.println("Modifier/Supprimer/Créer les modules :  2");
                        System.out.println("Modifier/Supprimer/Créer les classes :  3");
                        System.out.println("Modifier/Supprimer/Créer les filières : 4");
                        System.out.println("      Saisir le numéro de votre choix: ");
                        Scanner sc3 = new Scanner(System.in);
                        int choix3 = sc3.nextInt();
                        sc3.nextLine();
                        switch (choix3) {
                            case 1:
                                System.out.println("---------------------------------------");
                                System.out.println("Modifier des éléments :  1");
                                System.out.println("Supprimer des éléments : 2");
                                System.out.println("Créer des éléments :     3");
                                Scanner sc4 = new Scanner(System.in);
                                int choix4 = sc4.nextInt();
                                sc4.nextLine();
                                switch (choix4) {
                                    case 1:
                                        //On affiche les éléments
                                        System.out.println("Entrer l'id de l'élément à modifier : ");
                                        long idEle = sc4.nextLong();
                                        sc4.nextLine();
                                        Element element = structureManager.findElementById(idEle);
                                        System.out.println("Entrer le code d'élément à modifier , tapez entrer directement sinon ");
                                        String codeOut = sc4.nextLine();
                                        String code = element.getCode();
                                        if (!codeOut.equals(""))
                                            code = codeOut;
                                        System.out.println("Entrer le currentCoefficient à modifier , tapez 0 sinon ");
                                        double currCoeffO = sc4.nextDouble();
                                        sc4.nextLine();
                                        double currCoeff = element.getCurrentCoefficient();
                                        if (currCoeff != 0)
                                            currCoeff = currCoeffO;
                                        System.out.println("Entrer le nom d'élément à modifier , tapez entrer directement sinon ");
                                        String nom = sc4.nextLine();
                                        String nom1 = element.getNom();
                                        if (nom.equals(""))
                                            nom = nom1;
                                        structureManager.updateElement(new Element(idEle, nom, code, currCoeff));
                                        System.out.println("Opération effectuée avec succès ");
                                        break;
                                    case 2:
                                        //On affiche les éléments
                                        System.out.println("Entrer l'id de l'élément à supprimer : ");
                                        long idElement = sc4.nextLong();
                                        sc4.nextLine();
                                        structureManager.deleteElement(idElement);
                                        System.out.println("Opération effectuée avec succès");
                                        break;
                                    case 3:
                                        System.out.println("Entrer le nom d'élément : ");
                                        String nomM = sc4.nextLine();
                                        structureManager.createElement(new Element(nomM));
                                        System.out.println("Opération effectuée avec succès");
                                        break;
                                    case 0:
                                        System.out.println("Quitter le programme");
                                        break;

                                }
                                break;
                            case 2:
                                System.out.println("---------------------------------------");
                                System.out.println("Modifier des modules :  1");
                                System.out.println("Supprimer des modules : 2");
                                System.out.println("Créer des modules :     3");
                                Scanner sc5 = new Scanner(System.in);
                                int choix5 = sc5.nextInt();
                                sc5.nextLine();
                                switch (choix5) {
                                    case 1 :
                                        //On affiche les module
                                        System.out.println("Entrer le titre du module : ");
                                        String titreModule = sc5.nextLine();
                                        if(structureManager.getIdModule(titreModule)==0){
                                            System.out.println("Il n'ya aucun module portant ce titre");
                                            break;
                                        }
                                        System.out.println("Entrer le nouveau titre du module , tapez entrer directement si vous ne voulez pas modifier le titre");
                                        String titreM = sc5.nextLine();
                                        System.out.println("Entrer le code ,  0 si vous ne voulez pas le modifier ");
                                        String codeModule = sc5.nextLine();
                                        if(codeModule.equals(""))
                                            codeModule= structureManager.findModule(structureManager.getIdModule(titreModule)).getTitre();
                                        if(titreM.equals(""))
                                            titreM=titreModule;
                                        structureManager.updateModule(new Module(structureManager.getIdModule(titreModule),titreM,codeModule));
                                        System.out.println("Opération effectué avec succès ");
                                        break;
                                    case 2 :
                                        System.out.println("Entrer le titre du module à supprimer : ");
                                        String titreModuleS = sc5.nextLine();
                                        structureManager.deleteModule(structureManager.getIdModule(titreModuleS));
                                        System.out.println("Opération effectué avec succès ");
                                        break;
                                    case 3 :
                                        System.out.println("Entrer le titre du module à créer : ");
                                        String titreModuleC = sc5.nextLine();
                                        structureManager.createModule(new Module(titreModuleC));
                                        System.out.println("Opération effectué avec succès ");
                                        break;

                                }
                                break;
                            case 3 :
                                System.out.println("---------------------------------------");
                                System.out.println("Modifier des niveaux :  1");
                                System.out.println("Supprimer des niveaux : 2");
                                System.out.println("Créer des niveaux :     3");
                                Scanner sc6 = new Scanner(System.in);
                                int choix6 = sc6.nextInt();
                                sc6.nextLine();
                                switch (choix6) {
                                    case 1 :
                                        System.out.println("Entrer l'Alias  du niveau : ");
                                        String aliasNiveau = sc6.nextLine();
                                        long idNiveau = structureManager.getIdNiveau(aliasNiveau);
                                        Niveau niveau = structureManager.findNiveauById(idNiveau);
                                        System.out.println("Entrer le nouveau Alias , tapez entrer directement sinon :");
                                        String nAliasNiveau = sc6.nextLine();
                                        if(nAliasNiveau.equals(""))
                                            nAliasNiveau=aliasNiveau;
                                        System.out.println("Entrer le nouveau titre , tapez entrer directement sinon : ");
                                        String nouvTitre = sc6.nextLine();
                                        if(nouvTitre.equals(""))
                                            nouvTitre=niveau.getTitre();
                                        System.out.println("Entrer l'id du niveau suivant : 0 si vous ne voulez pas le changer ");
                                        long idNextLevel = sc6.nextLong();
                                        sc6.nextLine();
                                        if(idNextLevel==0)
                                            idNextLevel=niveau.getIdNextNiveau();
                                        structureManager.updateNiveau(new Niveau(idNiveau,nAliasNiveau,nouvTitre),idNextLevel);
                                        System.out.println("Opération effectué avec succès ");
                                        break;
                                    case 2 :
                                        System.out.println("Entrer l'Alias  du niveau à supprimer : ");
                                        String aliasNiveauS = sc6.nextLine();
                                        long idNiveauS = structureManager.getIdNiveau(aliasNiveauS);
                                        structureManager.deleteNiveau(idNiveauS);
                                        System.out.println("Opération effectué avec succès");
                                        break;
                                    case 3 :
                                        System.out.println("Entrer l'Alias  du niveau à créer : ");
                                        String aliasNiveauC = sc6.nextLine();
                                        System.out.println("Entrer le titre du niveau à créer : ");
                                        String titreNiveauC = sc6.nextLine();
                                        structureManager.createNiveau(new Niveau(aliasNiveauC,titreNiveauC));
                                        System.out.println("Opération effectué avec succès");
                                        break;

                                }
                                break;
                            case 4 :
                                System.out.println("---------------------------------------");
                                System.out.println("Modifier des filières :  1");
                                System.out.println("Supprimer des filières : 2");
                                System.out.println("Créer des filières :     3");
                                Scanner sc7 = new Scanner(System.in);
                                int choix7 = sc7.nextInt();
                                sc7.nextLine();
                                switch (choix7) {
                                    case 1:
                                        //On affiche les filière ici
                                        System.out.println("Entrez le numéro de la filière à modifier : ");
                                        long idFiliere = sc7.nextLong();
                                        sc7.nextLine();
                                        Filiere f = structureManager.findFiliere(idFiliere);

                                        System.out.println("Entrez la nouvelle année d'accréditation, 0 sinon : ");
                                        int nouvelleAnneeAccreditation = sc7.nextInt();
                                        sc7.nextLine();
                                        if(nouvelleAnneeAccreditation==0)
                                            nouvelleAnneeAccreditation= f.getAnneeaccreditation();

                                        System.out.println("Entrez la nouvelle année de fin d'accréditation, 0 sinon : ");
                                        int nouvelleAnneeFinAccreditation = sc7.nextInt();
                                        sc7.nextLine();
                                        if(nouvelleAnneeFinAccreditation==0)
                                            nouvelleAnneeFinAccreditation=f.getAnneeFinaccreditation();

                                        System.out.println("Entrez le nouveau code de la filière, tapez entrer directement sinon : ");
                                        String nouveauCodeFiliere = sc7.nextLine();
                                        if(nouveauCodeFiliere.equals(""))
                                            nouveauCodeFiliere=f.getCodeFiliere();

                                        System.out.println("Entrez le nouveau titre de la filière , tapez entrer directement sinon: ");
                                        String nouveauTitreFiliere = sc7.nextLine();
                                        if(nouveauTitreFiliere.equals(""))
                                            nouveauTitreFiliere=f.getTitreFiliere();

                                        Filiere filiere = new Filiere(idFiliere);
                                        filiere.setAnneeaccreditation(nouvelleAnneeAccreditation);
                                        filiere.setAnneeFinaccreditation(nouvelleAnneeFinAccreditation);
                                        filiere.setCodeFiliere(nouveauCodeFiliere);
                                        filiere.setTitreFiliere(nouveauTitreFiliere);

                                        structureManager.updateFiliere(filiere);
                                        System.out.println("Opération effectuée avec succès");
                                        break;
                                    case 2 :
                                        //On affiche les filière ici
                                        System.out.println("Entrez le numéro de la filière à supprimer : ");
                                        long idFiliereS = sc7.nextLong();
                                        sc7.nextLine();
                                        structureManager.deleteFiliere(idFiliereS);
                                        System.out.println("Opération effectuée avec succès");
                                        break;
                                    case 3 :

                                        System.out.println("Entrez l'année d'accréditation : ");
                                        int anneeAccreditation = sc7.nextInt();
                                        sc7.nextLine();

                                        System.out.println("Entrez l'année de fin d'accréditation : ");
                                        int anneeFinAccreditation = sc7.nextInt();
                                        sc7.nextLine();

                                        System.out.println("Entrez le code de la filière : ");
                                        String codeFiliere = sc7.nextLine();

                                        System.out.println("Entrez le titre de la filière : ");
                                        String titreFiliere = sc7.nextLine();

                                        Filiere filiereN = new Filiere();
                                        filiereN.setAnneeaccreditation(anneeAccreditation);
                                        filiereN.setAnneeFinaccreditation(anneeFinAccreditation);
                                        filiereN.setCodeFiliere(codeFiliere);
                                        filiereN.setTitreFiliere(titreFiliere);

                                        structureManager.createFiliere(filiereN);
                                        System.out.println("Filière créée avec succès");
                                        break;


                                }



                                }
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
                                System.out.println("Module n'est pas ajouté ");
                        }

                        break;
                    case 3 :
                        //On affiche les modules
                        System.out.println("Entrer l'id du module dont vous voulez associer des éléments : ");
                        long idMo = sc2.nextLong();
                        sc2.nextLine();
                        List<Element> elements = new ArrayList<>();
                        //On affiche les éléments
                        for(int i =0 ;i <2;i++){
                            System.out.println("Entrer le nom de l'élément à ajouter : ");
                            String nomEl = sc2.nextLine();
                            Element el = new Element(nomEl);
                            elements.add(el);
                        }
                        structureManager.associateElementsToModule(idMo,elements);
                        System.out.println("Les éléments sont associés au module avec succès !");
                        break;
                    case 4 :
                        //On affiche les classes :
                        System.out.println("Entrer le numéro de filière dont vous voulez associer les classes : ");//id
                        long idF = sc2.nextLong();
                        sc2.nextLine();
                        System.out.println("Combien de classes voulez-vous associer à cette filière ? ");
                        int nomb = Integer.parseInt(sc2.nextLine());
                        List<Niveau> niveaux = new ArrayList<>();
                        //Ici on affiche les modules
                        for(int j = 0 ; j<nomb;j++){
                            System.out.println("Entrer le nom de la classe dont vous voulez associer à la filière : ");
                            String nomCl1 = sc2.nextLine();
                            System.out.println("Entre l'alias de cette classe : ");
                            String AlCl = sc2.nextLine();
                            System.out.println("Est-ce-que cette classe existe déjà (tapez 1 is oui) : ");
                            int nombr = Integer.parseInt(sc2.nextLine());
                            if(nombr!=1)
                                structureManager.createNiveau(new Niveau(AlCl,nomCl1));
                            niveaux.add(new Niveau(AlCl,nomCl1));
                        }
                        structureManager.associateClassesToFiliere(idF,niveaux);
                        System.out.println("Les classes ont été associées avec succès ");
                        break;
                    case 5 :
                        System.out.println("Entrer l'id de le classe dont vous voulez consultez les modules : ");
                        long idCla = sc2.nextLong();
                        sc2.nextLine();
                        List<Module> modules = structureManager.getModulesByClass(idCla);
                        for(Module i : modules){
                            System.out.println(i.toString());
                            System.out.println("=================================");
                        }
                        break;
                    case 6 :
                        //On affiche ici les filières
                        System.out.println("Entrer l'id de la filière : ");
                        long idFil = sc2.nextLong();
                        sc2.nextLine();
                        //On affiche ici la liste des enseignants
                        System.out.println("Entrer l'id de l'enseignant dont vous voulez associer à la filiére : ");
                        long idCoo = sc2.nextLong();
                        sc2.nextLine();
                        structureManager.assignCoordinatorToFiliere(idFil,idCoo);
                        System.out.println("Done");
                        break;
                    case 7:
                        break;
                }

            case 0 :
                System.out.println("Bye!");
                System.exit(0);


        }

        }
        }
    }

