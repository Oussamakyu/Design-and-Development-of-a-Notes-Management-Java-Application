package com.ensah;

import java.util.List;

public interface StudentDatabase {

    /** Second Part: **/
    void updateStudentData(String cne,String name,String surname,Integer newLevel);
    // Search students by a given criteria:
    List<Student> searchStudent();

    // Retrieve the list of all students in a given class
    List<Student> getStudentsOfClass(String classId);

   //Save actions to the journal of the app
    void saveAction();
    // Export the list of all students to a file in CSV format
    void exportStudentsToCsv(String filePath);
    // Afficher les étudiants d'une classe:
    List<String> AfficheStudentsNames();
}
