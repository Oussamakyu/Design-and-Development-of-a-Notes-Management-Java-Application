package com.ensah.ihm;

import com.ensah.bll.*;
import com.ensah.bo.*;
import com.ensah.bo.Module;
import com.ensah.dao.CadreAdministrateurDao;
import com.ensah.dao.DataBaseException;
import com.ensah.utils.ConsoleColors;
import com.ensah.utils.ExcelImport;
import com.ensah.utils.FilterExcel;
import dnl.utils.text.table.TextTable;
import org.apache.log4j.Logger;


import javax.sound.midi.Soundbank;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrincipalProg {
    private static Logger LOGGER = Logger.getLogger(PrincipalProg.class);
    public static void printMenu() {
        /**
         * Affiche le menu de l'application
         */

        System.out.println(ConsoleColors.YELLOW+"Inscription/Reinscription des étudiants	:              1 ");
        System.out.println("Ajouter des délibérations depuis le fichier excel :    2 ");
        System.out.println("Exporter le fichier des délibérations :                3 ");
        System.out.println("Gérer la structure et les éléments pédagogiques :      4 ");
        System.out.println("Gestion des modifications, Recherche et Consultation : 5  ");
        System.out.println("0-	Sortir "+ConsoleColors.RESET);
    }

    public static void main(String[] args) throws BusinessLogicException, DataBaseException, ImportException, IOException {
        StructureManager structureManager = new StructureManager();
        String logo = "\n" + ConsoleColors.PURPLE_BOLD_BRIGHT+
                " ___           _   _         _                \n" +
                "(  _`\\        ( ) ( )       ( )_              \n" +
                "| (_(_)______ | `\\| |   _   | ,_)   __    ___ \n" +
                "|  _)_(______)| , ` | /'_`\\ | |   /'__`\\/',__)\n" +
                "| (_( )       | |`\\ |( (_) )| |_ (  ___/\\__, \\\n" +
                "(____/'       (_) (_)`\\___/'`\\__)`\\____)(____/\n" +
                "                                              \n" +
                "                                              \n"+ConsoleColors.RESET;
        System.out.println(logo);
        //création des instances de la classe qui gèrent la logique métier
        NotesManager notesManager = new NotesManager();
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
        boolean sw2 = true;
        while(!connect) {
            System.out.println(ConsoleColors.CYAN_BOLD+"Entrer votre login : ");
            String login = sc.nextLine();
            System.out.println("Entrer votre mot de passe : "+ConsoleColors.RESET);
            String password = sc.nextLine();
            AdminsManager admMan = new AdminsManager();
            connect = admMan.connectAdmin(login, password);
            if(connect)
                System.out.println("========================Welcome======================= " ); // to retrieve name later
        }

        while (connect) {
            //afficher le menu
            printMenu();
            //lire le choix
            System.out.println(ConsoleColors.PURPLE+"Saisir le numéro de votre choix: ");
            int choix = sc.nextInt();
            sc.nextLine();

        switch (choix) {
            case 1:
                System.out.println("Entrer le chemin du fichier excel: ");
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
                DeliberationManager deliberationManager = new DeliberationManager();
                System.out.println("Entrer l'alias du niveau");
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
                System.out.println("---------------------------------------");
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
                while (sw2){
                switch (choix2) {
                    case 1:
                        System.out.println("---------------------------------------");
                        System.out.println("Modifier/Supprimer/Créer les éléments : 1");
                        System.out.println("Modifier/Supprimer/Créer les modules :  2");
                        System.out.println("Modifier/Supprimer/Créer les classes :  3");
                        System.out.println("Modifier/Supprimer/Créer les filières : 4");
                        System.out.println("Quitter l'application                 : 0");
                        System.out.println("      Saisir le numéro de votre choix: ");
                        Scanner sc3 = new Scanner(System.in);
                        int choix3 = sc3.nextInt();
                        sc3.nextLine();
                        switch (choix3) {
                            case 0:
                                System.out.println("Bye!");
                                System.exit(0);
                            case 1:
                                System.out.println("---------------------------------------");
                                System.out.println("Modifier des éléments :  1");
                                System.out.println("Supprimer des éléments : 2");
                                System.out.println("Créer des éléments :     3");
                                Scanner sc4 = new Scanner(System.in);
                                int choix4 = sc4.nextInt();
                                sc4.nextLine();
                                switch (choix4) {
                                    case 0:
                                        System.out.println("Bye!");
                                        System.exit(0);
                                    case 1:
                                        List<Element> elements = null;
                                        elements = structureManager.getAllElements();

                                        Object[][] data = new Object[elements.size()][4];
                                        for (int i = 0; i < elements.size(); i++) {
                                            data[i][0] = elements.get(i).getIdMatiere();
                                            data[i][1] = elements.get(i).getNom();
                                            data[i][2] = elements.get(i).getCode();
                                            data[i][3] = elements.get(i).getCurrentCoefficient();
                                        }
                                        String[] columnNames = {"Numéro ", "Nom", "Code", "CurrentCoefficient"};
                                        TextTable tt = new TextTable(columnNames, data);
                                        tt.printTable();
                                        System.out.println("Entrer numéro d'élément à modifier : ");
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
                                        List<Element> allElements = null;
                                        allElements = structureManager.getAllElements();

                                        Object[][] dataEl = new Object[allElements.size()][4];
                                        for (int i = 0; i < allElements.size(); i++) {
                                            dataEl[i][0] = allElements.get(i).getIdMatiere();
                                            dataEl[i][1] = allElements.get(i).getNom();
                                            dataEl[i][2] = allElements.get(i).getCode();
                                            dataEl[i][3] = allElements.get(i).getCurrentCoefficient();
                                        }
                                        String[] columnnames = {"Numéro ", "Nom", "Code", "CurrentCoefficient"};
                                        TextTable ttt = new TextTable(columnnames, dataEl);
                                        ttt.printTable();
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
                                    case 1:
                                        List<Niveau> nvx = null;
                                        nvx = structureManager.getAllLevels();
                                        Object[][] dataL = new Object[nvx.size()][4];
                                        for (int i = 0; i < nvx.size(); i++) {
                                            dataL[i][0] = nvx.get(i).getIdNiveau();
                                            dataL[i][1] = nvx.get(i).getTitre();
                                            dataL[i][2] = nvx.get(i).getAlias();
                                            dataL[i][3] = nvx.get(i).getIdNextNiveau();
                                        }
                                        String[] columnNamesL = {"Numéro ", "Titre", "Alias", "Id du niveau suivant"};
                                        TextTable ttL = new TextTable(columnNamesL, dataL);
                                        ttL.printTable();
                                        System.out.println("Vous voulez modifier les modules de quelle classe?Entrer son numéro ");
                                        long idClasse = sc5.nextLong();
                                        sc5.nextLine();
                                        List<Module> modules = null;
                                        modules = structureManager.getModulesByClass(idClasse);
                                        Object[][] data = new Object[modules.size()][3];
                                        for (int i = 0; i < modules.size(); i++) {
                                            data[i][0] = modules.get(i).getIdModule();
                                            data[i][1] = modules.get(i).getTitre();
                                            data[i][2] = modules.get(i).getCode();
                                        }
                                        String[] columnNames = {"Numéro ", "Titre", "Code"};
                                        TextTable tt = new TextTable(columnNames, data);
                                        tt.printTable();
                                        System.out.println("Entrer le titre du module : ");
                                        String titreModule = sc5.nextLine();
                                        if (structureManager.getIdModule(titreModule) == 0) {
                                            System.out.println("Il n'ya aucun module portant ce titre");
                                            break;
                                        }
                                        System.out.println("Entrer le nouveau titre du module , tapez entrer directement si vous ne voulez pas modifier le titre");
                                        String titreM = sc5.nextLine();
                                        System.out.println("Entrer le code ,  0 si vous ne voulez pas le modifier ");
                                        String codeModule = sc5.nextLine();
                                        if (codeModule.equals(""))
                                            codeModule = structureManager.findModule(structureManager.getIdModule(titreModule)).getTitre();
                                        if (titreM.equals(""))
                                            titreM = titreModule;
                                        structureManager.updateModule(new Module(structureManager.getIdModule(titreModule), titreM, codeModule));
                                        System.out.println("Opération effectué avec succès ");
                                        break;
                                    case 2:
                                        System.out.println("Entrer le titre du module à supprimer : ");
                                        String titreModuleS = sc5.nextLine();
                                        structureManager.deleteModule(structureManager.getIdModule(titreModuleS));
                                        System.out.println("Opération effectué avec succès ");
                                        break;
                                    case 3:
                                        System.out.println("Entrer le titre du module à créer : ");
                                        String titreModuleC = sc5.nextLine();
                                        structureManager.createModule(new Module(titreModuleC));
                                        System.out.println("Opération effectué avec succès ");
                                        break;

                                }
                                break;
                            case 3:
                                System.out.println("---------------------------------------");
                                System.out.println("Modifier des niveaux :  1");
                                System.out.println("Supprimer des niveaux : 2");
                                System.out.println("Créer des niveaux :     3");
                                Scanner sc6 = new Scanner(System.in);
                                int choix6 = sc6.nextInt();
                                sc6.nextLine();
                                switch (choix6) {
                                    case 1:
                                        List<Niveau> nvx = null;
                                        nvx = structureManager.getAllLevels();
                                        Object[][] dataL = new Object[nvx.size()][4];
                                        for (int i = 0; i < nvx.size(); i++) {
                                            dataL[i][0] = nvx.get(i).getIdNiveau();
                                            dataL[i][1] = nvx.get(i).getTitre();
                                            dataL[i][2] = nvx.get(i).getAlias();
                                            dataL[i][3] = nvx.get(i).getIdNextNiveau();
                                        }
                                        String[] columnNamesL = {"Numéro ", "Titre", "Alias", "Id du niveau suivant"};
                                        TextTable ttL = new TextTable(columnNamesL, dataL);
                                        ttL.printTable();
                                        System.out.println("Entrer l'Alias  du niveau : ");
                                        String aliasNiveau = sc6.nextLine();
                                        long idNiveau = structureManager.getIdNiveau(aliasNiveau);
                                        Niveau niveau = structureManager.findNiveauById(idNiveau);
                                        System.out.println("Entrer le nouveau Alias , tapez entrer directement sinon :");
                                        String nAliasNiveau = sc6.nextLine();
                                        if (nAliasNiveau.equals(""))
                                            nAliasNiveau = aliasNiveau;
                                        System.out.println("Entrer le nouveau titre , tapez entrer directement sinon : ");
                                        String nouvTitre = sc6.nextLine();
                                        if (nouvTitre.equals(""))
                                            nouvTitre = niveau.getTitre();
                                        System.out.println("Entrer l'id du niveau suivant : 0 si vous ne voulez pas le changer ");
                                        long idNextLevel = sc6.nextLong();
                                        sc6.nextLine();
                                        if (idNextLevel == 0)
                                            idNextLevel = niveau.getIdNextNiveau();
                                        structureManager.updateNiveau(new Niveau(idNiveau, nAliasNiveau, nouvTitre), idNextLevel);
                                        System.out.println("Opération effectué avec succès ");
                                        break;
                                    case 2:
                                        List<Niveau> nvx2 = null;
                                        nvx2 = structureManager.getAllLevels();
                                        Object[][] datasL = new Object[nvx2.size()][4];
                                        for (int i = 0; i < nvx2.size(); i++) {
                                            datasL[i][0] = nvx2.get(i).getIdNiveau();
                                            datasL[i][1] = nvx2.get(i).getTitre();
                                            datasL[i][2] = nvx2.get(i).getAlias();
                                            datasL[i][3] = nvx2.get(i).getIdNextNiveau();
                                        }
                                        String[] columnsNamesL = {"Numéro ", "Titre", "Alias", "Id du niveau suivant"};
                                        TextTable tttL = new TextTable(columnsNamesL, datasL);
                                        tttL.printTable();
                                        System.out.println("Entrer l'Alias  du niveau à supprimer : ");
                                        String aliasNiveauS = sc6.nextLine();
                                        long idNiveauS = structureManager.getIdNiveau(aliasNiveauS);
                                        structureManager.deleteNiveau(idNiveauS);
                                        System.out.println("Opération effectué avec succès");
                                        break;
                                    case 3:
                                        System.out.println("Entrer l'Alias  du niveau à créer : ");
                                        String aliasNiveauC = sc6.nextLine();
                                        System.out.println("Entrer le titre du niveau à créer : ");
                                        String titreNiveauC = sc6.nextLine();
                                        structureManager.createNiveau(new Niveau(aliasNiveauC, titreNiveauC));
                                        System.out.println("Opération effectué avec succès");
                                        break;

                                }
                                break;
                            case 4:
                                System.out.println("---------------------------------------");
                                System.out.println("Modifier des filières :  1");
                                System.out.println("Supprimer des filières : 2");
                                System.out.println("Créer des filières :     3");
                                Scanner sc7 = new Scanner(System.in);
                                int choix7 = sc7.nextInt();
                                sc7.nextLine();
                                switch (choix7) {
                                    case 1:
                                        List<Filiere> filieres = null;
                                        filieres = structureManager.getAllFiliere();

                                        Object[][] data = new Object[filieres.size()][4];
                                        for (int i = 0; i < filieres.size(); i++) {
                                            data[i][0] = filieres.get(i).getIdFiliere();
                                            data[i][1] = filieres.get(i).getTitreFiliere();
                                            data[i][2] = filieres.get(i).getAnneeaccreditation();
                                            data[i][3] = filieres.get(i).getCodeFiliere();
                                        }
                                        String[] columnNames = {"Numéro ", "Titre", "Anneeaccreditation", "Code"};
                                        TextTable tt = new TextTable(columnNames, data);
                                        tt.printTable();
                                        System.out.println("Entrez le numéro de la filière à modifier : ");
                                        long idFiliere = sc7.nextLong();
                                        sc7.nextLine();
                                        Filiere f = structureManager.findFiliere(idFiliere);

                                        System.out.println("Entrez la nouvelle année d'accréditation, 0 sinon : ");
                                        int nouvelleAnneeAccreditation = sc7.nextInt();
                                        sc7.nextLine();
                                        if (nouvelleAnneeAccreditation == 0)
                                            nouvelleAnneeAccreditation = f.getAnneeaccreditation();

                                        System.out.println("Entrez la nouvelle année de fin d'accréditation, 0 sinon : ");
                                        int nouvelleAnneeFinAccreditation = sc7.nextInt();
                                        sc7.nextLine();
                                        if (nouvelleAnneeFinAccreditation == 0)
                                            nouvelleAnneeFinAccreditation = f.getAnneeFinaccreditation();

                                        System.out.println("Entrez le nouveau code de la filière, tapez entrer directement sinon : ");
                                        String nouveauCodeFiliere = sc7.nextLine();
                                        if (nouveauCodeFiliere.equals(""))
                                            nouveauCodeFiliere = f.getCodeFiliere();

                                        System.out.println("Entrez le nouveau titre de la filière , tapez entrer directement sinon: ");
                                        String nouveauTitreFiliere = sc7.nextLine();
                                        if (nouveauTitreFiliere.equals(""))
                                            nouveauTitreFiliere = f.getTitreFiliere();

                                        Filiere filiere = new Filiere(idFiliere);
                                        filiere.setAnneeaccreditation(nouvelleAnneeAccreditation);
                                        filiere.setAnneeFinaccreditation(nouvelleAnneeFinAccreditation);
                                        filiere.setCodeFiliere(nouveauCodeFiliere);
                                        filiere.setTitreFiliere(nouveauTitreFiliere);

                                        structureManager.updateFiliere(filiere);
                                        System.out.println("Opération effectuée avec succès");
                                        break;
                                    case 2:
                                        List<Filiere> fiilieres = null;
                                        fiilieres = structureManager.getAllFiliere();

                                        Object[][] datas = new Object[fiilieres.size()][4];
                                        for (int i = 0; i < fiilieres.size(); i++) {
                                            datas[i][0] = fiilieres.get(i).getIdFiliere();
                                            datas[i][1] = fiilieres.get(i).getTitreFiliere();
                                            datas[i][2] = fiilieres.get(i).getAnneeaccreditation();
                                            datas[i][3] = fiilieres.get(i).getCodeFiliere();
                                        }
                                        String[] columnsNames = {"Numéro ", "Titre", "Anneeaccreditation", "Code"};
                                        TextTable ttt = new TextTable(columnsNames, datas);
                                        ttt.printTable();
                                        System.out.println("Entrez le numéro de la filière à supprimer : ");
                                        long idFiliereS = sc7.nextLong();
                                        sc7.nextLine();
                                        structureManager.deleteFiliere(idFiliereS);
                                        System.out.println("Opération effectuée avec succès");
                                        break;
                                    case 3:

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
                    case 2:
                        List<Niveau> nvex = null;
                        nvex = structureManager.getAllLevels();
                        Object[][] dataeL = new Object[nvex.size()][4];
                        for (int i = 0; i < nvex.size(); i++) {
                            dataeL[i][0] = nvex.get(i).getIdNiveau();
                            dataeL[i][1] = nvex.get(i).getTitre();
                            dataeL[i][2] = nvex.get(i).getAlias();
                            dataeL[i][3] = nvex.get(i).getIdNextNiveau();
                        }
                        String[] columneNamesL = {"Numéro ", "Titre", "Alias", "Id du niveau suivant"};
                        TextTable tteL = new TextTable(columneNamesL, dataeL);
                        tteL.printTable();
                        System.out.println("Entrer l'Alias' de la classe : ");
                        String nomCl = sc2.nextLine();
                        System.out.println("Combien de modules voulez-vous associer à cette classe ? ");
                        int num = Integer.parseInt(sc2.nextLine());

                        for (int i = 0; i < num; i++) {
                            System.out.println("Entrer le titre du module à ajouter : ");
                            String titre = sc2.nextLine();
                            System.out.println(titre);
                            boolean x = structureManager.associateModuleWithNiveau(structureManager.getIdModule(titre), structureManager.getIdNiveau(nomCl));
                            if (x)
                                System.out.println("Ajouté avec succès");
                            else
                                System.out.println("Module n'est pas ajouté ");
                        }

                        break;
                    case 3:
                        List<Niveau> nvx = null;
                        nvx = structureManager.getAllLevels();
                        Object[][] dataL = new Object[nvx.size()][4];
                        for (int i = 0; i < nvx.size(); i++) {
                            dataL[i][0] = nvx.get(i).getIdNiveau();
                            dataL[i][1] = nvx.get(i).getTitre();
                            dataL[i][2] = nvx.get(i).getAlias();
                            dataL[i][3] = nvx.get(i).getIdNextNiveau();
                        }
                        String[] columnNamesL = {"Numéro ", "Titre", "Alias", "Id du niveau suivant"};
                        TextTable ttL = new TextTable(columnNamesL, dataL);
                        ttL.printTable();
                        System.out.println("Vous voulez modifier les modules de quelle classe?Entrer son numéro ");
                        long idClasse = sc2.nextLong();
                        sc2.nextLine();
                        List<Module> modules = null;
                        modules = structureManager.getModulesByClass(idClasse);
                        Object[][] data = new Object[modules.size()][3];
                        for (int i = 0; i < modules.size(); i++) {
                            data[i][0] = modules.get(i).getIdModule();
                            data[i][1] = modules.get(i).getTitre();
                            data[i][2] = modules.get(i).getCode();
                        }
                        String[] columnNames = {"Numéro ", "Titre", "Code"};
                        TextTable tt = new TextTable(columnNames, data);
                        tt.printTable();
                        System.out.println("Entrer l'id du module dont vous voulez associer des éléments : ");
                        long idMo = sc2.nextLong();
                        sc2.nextLine();
                        List<Element> elements = new ArrayList<>();
                        List<Element> elemennts = null;
                        elemennts = structureManager.getAllElements();

                        Object[][] datta = new Object[elemennts.size()][4];
                        for (int i = 0; i < elemennts.size(); i++) {
                            datta[i][0] = elemennts.get(i).getIdMatiere();
                            datta[i][1] = elemennts.get(i).getNom();
                            datta[i][2] = elemennts.get(i).getCode();
                            datta[i][3] = elemennts.get(i).getCurrentCoefficient();
                        }
                        String[] columnNamees = {"Numéro ", "Nom", "Code", "CurrentCoefficient"};
                        TextTable tyt = new TextTable(columnNamees, datta);
                        tyt.printTable();
                        for (int i = 0; i < 2; i++) {
                            System.out.println("Entrer le nom de l'élément à ajouter : ");
                            String nomEl = sc2.nextLine();
                            Element el = new Element(nomEl);
                            elemennts.add(el);
                        }
                        structureManager.associateElementsToModule(idMo, elemennts);
                        System.out.println("Les éléments sont associés au module avec succès !");
                        break;
                    case 4:
                        List<Filiere> filieres = null;
                        filieres = structureManager.getAllFiliere();

                        Object[][] datia = new Object[filieres.size()][4];
                        for (int i = 0; i < filieres.size(); i++) {
                            datia[i][0] = filieres.get(i).getIdFiliere();
                            datia[i][1] = filieres.get(i).getTitreFiliere();
                            datia[i][2] = filieres.get(i).getAnneeaccreditation();
                            datia[i][3] = filieres.get(i).getCodeFiliere();
                        }
                        String[] columnskNames = {"Numéro ", "Titre", "Anneeaccreditation", "Code"};
                        TextTable ttts = new TextTable(columnskNames, datia);
                        ttts.printTable();
                        System.out.println("Entrer le numéro de filière dont vous voulez associer les classes : ");
                        long idF = sc2.nextLong();
                        sc2.nextLine();
                        System.out.println("Combien de classes voulez-vous associer à cette filière ? ");
                        int nomb = Integer.parseInt(sc2.nextLine());
                        List<Niveau> niveaux = new ArrayList<>();
                        Object[][] oxData = new Object[niveaux.size()][4];
                        for (int i = 0; i < niveaux.size(); i++) {
                            oxData[i][0] = niveaux.get(i).getIdNiveau();
                            oxData[i][1] = niveaux.get(i).getTitre();
                            oxData[i][2] = niveaux.get(i).getAlias();
                            oxData[i][3] = niveaux.get(i).getIdNextNiveau();
                        }
                        String[] columnppxNamesL = {"Numéro ", "Titre", "Alias", "Id du niveau suivant"};
                        TextTable tppxtL = new TextTable(columnppxNamesL, oxData);
                        tppxtL.printTable();
                        for (int j = 0; j < nomb; j++) {
                            System.out.println("Entrer le nom de la classe dont vous voulez associer à la filière : ");
                            String nomCl1 = sc2.nextLine();
                            System.out.println("Entre l'alias de cette classe : ");
                            String AlCl = sc2.nextLine();
                            System.out.println("Est-ce-que cette classe existe déjà (tapez 1 is oui) : ");
                            int nombr = Integer.parseInt(sc2.nextLine());
                            if (nombr != 1)
                                structureManager.createNiveau(new Niveau(AlCl, nomCl1));
                            niveaux.add(new Niveau(AlCl, nomCl1));
                        }
                        structureManager.associateClassesToFiliere(idF, niveaux);
                        System.out.println("Les classes ont été associées avec succès ");
                        break;
                    case 5:
                        List<Niveau> nivx = null;
                        nivx = structureManager.getAllLevels();
                        Object[][] oData = new Object[nivx.size()][4];
                        for (int i = 0; i < nivx.size(); i++) {
                            oData[i][0] = nivx.get(i).getIdNiveau();
                            oData[i][1] = nivx.get(i).getTitre();
                            oData[i][2] = nivx.get(i).getAlias();
                            oData[i][3] = nivx.get(i).getIdNextNiveau();
                        }
                        String[] columnppNamesL = {"Numéro ", "Titre", "Alias", "Id du niveau suivant"};
                        TextTable tpptL = new TextTable(columnppNamesL, oData);
                        tpptL.printTable();
                        System.out.println("Entrer le numéro de le classe dont vous voulez consultez les modules : ");
                        long idCla = sc2.nextLong();
                        sc2.nextLine();
                        List<Module> modulpes = structureManager.getModulesByClass(idCla);
                        Object[][] daaata = new Object[modulpes.size()][3];
                        for (int i = 0; i < modulpes.size(); i++) {
                            daaata[i][0] = modulpes.get(i).getIdModule();
                            daaata[i][1] = modulpes.get(i).getTitre();
                            daaata[i][2] = modulpes.get(i).getCode();
                        }
                        String[] columnaaNames = {"Numéro ", "Titre", "Code"};
                        TextTable taat = new TextTable(columnaaNames, daaata);
                        taat.printTable();
                        break;
                    case 6:
                        List<Filiere> filierres = null;
                        filierres = structureManager.getAllFiliere();

                        Object[][] daaaata = new Object[filierres.size()][4];
                        for (int i = 0; i < filierres.size(); i++) {
                            daaaata[i][0] = filierres.get(i).getIdFiliere();
                            daaaata[i][1] = filierres.get(i).getTitreFiliere();
                            daaaata[i][2] = filierres.get(i).getAnneeaccreditation();
                            daaaata[i][3] = filierres.get(i).getCodeFiliere();
                        }
                        String[] cooolumnNames = {"Numéro ", "Titre", "Anneeaccreditation", "Code"};
                        TextTable toot = new TextTable(cooolumnNames, daaaata);
                        toot.printTable();
                        System.out.println("Entrer le numéro de la filière : ");
                        long idFil = sc2.nextLong();
                        sc2.nextLine();
                        //On affiche ici la liste des enseignants
                        System.out.println("Entrer l'id de l'enseignant dont vous voulez associer à la filiére : ");
                        long idCoo = sc2.nextLong();
                        sc2.nextLine();
                        structureManager.assignCoordinatorToFiliere(idFil, idCoo);
                        System.out.println("Done");
                        break;
                    case 7:
                        break;
                }
                }

            case 0 :
                System.out.println("Bye!");
                System.exit(0);


        }

        }
        }
    }

