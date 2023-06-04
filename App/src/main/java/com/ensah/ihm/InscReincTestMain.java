package com.ensah.ihm;

import com.ensah.bll.BllException;
import com.ensah.bll.ImportException;
import com.ensah.bll.StudentManager;
import com.ensah.bo.Etudiant;
import com.ensah.dao.DBConnection;
import com.ensah.dao.DataBaseException;
import com.ensah.utils.ExcelImport;
import com.ensah.utils.FilterExcel;

import java.sql.Connection;


public class InscReincTestMain {
    public static void main(String[] args) throws DataBaseException, ImportException, BllException {
        // Conection a la base de données
        Connection con = DBConnection.getInstance();
        if (con == null){
            System.out.println("connexion non établi");
        }

        // Test De la fonction importation des données des etudiant (inscription/reinscription)
        String Path = "C:\\Users\\Housni Achbouq\\Desktop\\Design-and-Development-of-a-Notes-Management-Java-Application\\App\\database\\nouveauEtudiant.xlsx";
        ExcelImport excelImport = new ExcelImport(Path);

        // SHOW EXCEL DATA
        //// System.out.println(excelImport.readExcel(excelImport.getSheet()));


        FilterExcel test = new FilterExcel();
        test.importStudent(excelImport.readExcel(excelImport.getSheet()));
//        System.out.println(test.getNewStudents());
//        System.out.println(test.getOldStudents());

        //Tester la fonction d'inscription

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


        //studentManager.inscriptionEtudiant(test.getNewStudents().get(1));

        //System.out.println(((Etudiant) test.getOldStudents().get(0)).getIdUtilisateur());
        //studentManager.reInscriptionEtudiant(((Etudiant) test.getOldStudents().get(0)),Long.parseLong(String.valueOf(test.getOldStudents().get(1))));




    }
}