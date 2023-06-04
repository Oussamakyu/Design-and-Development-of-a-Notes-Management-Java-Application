package com.ensah.ihm;

import com.ensah.bll.NotesManager;
import com.ensah.dao.DataBaseException;
import com.ensah.utils.ExcelDelibReader;
import com.ensah.bll.NotesManager.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class ReaderDelibTest {

    public static void main(String[] args) {
        try {
            NotesManager notesManager = new NotesManager();
            if(notesManager.verifierExistanceEnregistrements()){
                System.out.println("Are you sure that you want to modify deliberations?(No to stop)");
            }
            Scanner sc = new Scanner(System.in);
            String answer = sc.nextLine();
            if(answer.equals("No"))
                return;
            notesManager.insererNotesFinales();
            System.out.println("Notes finales inserted successfully.");
        } catch (IOException | SQLException | DataBaseException e) {
            e.printStackTrace();
        }
    }
}

