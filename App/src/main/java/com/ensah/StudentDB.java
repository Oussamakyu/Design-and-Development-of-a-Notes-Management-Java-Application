package com.ensah;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;

public class StudentDB implements StudentDatabase {
    @Override
    public void updateStudentData(Student student) {
        //String cne, String name, String surname, Integer newLevel
        // Établir la connexion à la base de données
        Connection con = ConnectDB.getConnection();
        if (con == null) {
            return;
        }
        if (student.getIdStudent() > 0) {
            String query = "UPDATE student SET cne=?, name=?, surname=?, newLevel=? WHERE id_student=?;";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                // We wrote the previous line on try parameter so that it closes the connector automatically
                //instead if he will catch an exception in the middle of the code the connector will stay opened
                preparedStatement.setString(1, student.getCne());
                preparedStatement.setString(2, student.getName());
                preparedStatement.setString(3, student.getSurname());
                preparedStatement.setInt(4, student.getLevel());
                preparedStatement.setDouble(5, student.getIdStudent());
                preparedStatement.executeUpdate();

            } catch (SQLException se) {
                se.printStackTrace();
            } finally {
                try {
                    con.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            //Student not found
            return;
        }

    }

    @Override
    public Student searchStudent(double studentId) {
        Student student = null;
        try {
            // Établir la connexion à la base de données
            Connection connection = ConnectDB.getConnection();
            // Préparer la requête SQL pour rechercher un étudiant par son ID
            String query = "SELECT id_student, cne, name, surname, id_current_level, type FROM Student WHERE id_student = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, studentId);
            // Exécuter la requête SQL
            ResultSet resultSet = preparedStatement.executeQuery();
            // Parcourir les résultats de la requête et créer un objet Student correspondant
            if (resultSet.next()) {
                int id = resultSet.getInt("id_student");
                String cne = resultSet.getString("cne");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                int currentLevel = resultSet.getInt("id_current_level");
                boolean type = resultSet.getBoolean("type");
                student = new Student(id, cne, name, surname, currentLevel, type);
            }
            // Fermer les ressources
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return student;
    }


    @Override
    public List<Student> getStudentsOfClass(String classId) {
        StudentDB st = null;
        List<Student> students = new ArrayList<>();
        List<Double> studentIds = new ArrayList<>();
        // Établir la connexion à la base de données
        try (Connection con = ConnectDB.getConnection()) {
            // Préparer la requête SQL pour récupérer les noms de tous les étudiants
            String query = "SELECT id_student FROM class";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    do {
                        double id_student = resultSet.getDouble("id_student");
                        studentIds.add(id_student);
                    } while (resultSet.next());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(double i : studentIds){
            Student student = st.searchStudent(i);
            students.add(student);
       }
        return students;
    }


    @Override
    public void exportStudentsToCsv(String filePath) {
        try {
            // Établir la connexion à la base de données
            Connection connection = ConnectDB.getConnection();

            // Récupérer les données de la table "Student"
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id_student, cne, name, surname, id_current_level, type FROM Student");

            // Écrire les données dans le fichier CSV
            FileWriter fileWriter = new FileWriter(filePath);
            while (resultSet.next()) {
                String id = resultSet.getString("id_student");
                String cne = resultSet.getString("cne");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String id_current_level = resultSet.getString("id_current_level");
                String type = resultSet.getString("type");

                fileWriter.append(id)
                        .append(",")
                        .append(cne)
                        .append(",")
                        .append(name)
                        .append(",")
                        .append(surname)
                        .append(",")
                        .append(id_current_level)
                        .append(",")
                        .append(type)
                        .append("\n");
            }
            fileWriter.flush();
            fileWriter.close();

            System.out.println("Exportation des étudiants vers le fichier CSV réussie.");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<String> AfficheStudentsNames() {
        List<String> studentNames = new ArrayList<>();
        // Établir la connexion à la base de données
        try (Connection con = ConnectDB.getConnection()) {
            // Préparer la requête SQL pour récupérer les noms de tous les étudiants
            String query = "SELECT name FROM Student";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    do {
                        String name = resultSet.getString("name");
                        studentNames.add(name);
                    } while (resultSet.next());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentNames;
    }
}
