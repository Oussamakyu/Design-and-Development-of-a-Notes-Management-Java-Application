package com.ensah;

import java.util.List;

public interface reregRegStudents {
    //Trouvez les ids des étudiants ayant le type INSCRIPTION dans le fichier csv:
    List<String> registrationIds();

    //Trouvez les ids des étudiants ayant le type REINSCRIPTION:
    List<String> reregistrationsIds();
    //check if the id already exist on the database:
    boolean checkId(String id);
    // Add a new student to the database

    boolean logicLevel(String id); //the id here means Student's id , but we will retrieve level's id to compare
    //the next function to compare the cne,nom,prenom ; it returns a list [1 or 0,cne,nom,prenom]
    //0 if it not the same data , 1 if not
    List<String> verifyData(String cne,String nom,String prenom);
    //Sauvegarder:
    void addStudent(String cne, String name, String surname, int level);

    // Update the data of an existing student:
    void updateStudent(String cne, String name, String surname, Integer newLevel);
    //j'ai choisi la classe Integer pour donner la valeur null par défault;
}
