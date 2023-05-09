package com.ensah;

public interface collecteNotes {
    //Generate an excel file from the DB:
    void generateExcelFile();
    //Calculer la moyenne d'un étudiant:
    double studentMoyenne(String studentId);
    //
    String validation(double moyenne,String level);
    //you can get the student level from the list returned by the method searchStudent() from the
    // StudentDatabase class
    // Saisir les notes:
    void saisirNote(double note);
}
