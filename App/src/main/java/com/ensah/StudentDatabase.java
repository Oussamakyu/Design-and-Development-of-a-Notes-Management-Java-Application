package com.ensah;

import java.util.List;

public interface StudentDatabase {

    /** Second Part: **/
    void updateStudentData(Student student);
    // Search students by a given criteria:
    Student searchStudent(double StudentId);

    // Retrieve the list of all students in a given class
    List<Student> getStudentsOfClass(String classId);

   //Save actions to the journal of the app
    void exportStudentsToCsv(String filePath);
    // Afficher les étudiants d'une classe:
    List<String> AfficheStudentsNames();
}
