package org.example.dao;

// @Author Rim Lfellous

import org.example.bo.Etudiant;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

    public class EtudiantDao {
        private Logger logger = Logger.getLogger(this.getClass());

        public EtudiantDao() {
        }


        public List<Etudiant> getStudentInfoByModuleTitre(String moduleTitre) throws DataBaseException {
            List<Etudiant> studentInfoList = new ArrayList<>();
            String sql = "SELECT e.idEtudiant, e.cne, u.nom, u.prenom " +
                    "FROM etudiant e " +
                    "JOIN inscriptionannuelle ia ON e.idEtudiant = ia.idEtudiant " +
                    "JOIN inscriptionmodule im ON ia.idInscription = im.idInscription " +
                    "JOIN module m ON im.idModule = m.idModule " +
                    "JOIN utilisateur u ON e.idEtudiant = u.idUtilisateur " +
                    "WHERE m.Titre = ?";

            try (Connection c = DBConnection.getInstance();
                 PreparedStatement statement = c.prepareStatement(sql)) {
                statement.setString(1, moduleTitre);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        long id = resultSet.getInt("idEtudiant");
                        String cne = resultSet.getString("cne");
                        String nom = resultSet.getString("nom");
                        String prenom = resultSet.getString("prenom");
                        Etudiant studentInfo = new Etudiant(id, cne, nom, prenom);
                        studentInfoList.add(studentInfo);
                    }
                }
            } catch (SQLException e) {
                throw new DataBaseException("Failed to retrieve student information from the database", e);
            }
            return studentInfoList;
        }

        public List<Etudiant> getStudentsByRatt(String moduleTitre) throws DataBaseException {
            List<Etudiant> studentInfoList = new ArrayList<>();

            String sql = "SELECT e.idEtudiant, e.cne, u.nom, u.prenom " +
                    "FROM etudiant e " +
                    "JOIN inscriptionannuelle ia ON e.idEtudiant = ia.idEtudiant " +
                    "JOIN inscriptionmodule im ON ia.idInscription = im.idInscription " +
                    "JOIN module m ON im.idModule = m.idModule " +
                    "JOIN utilisateur u ON e.idEtudiant = u.idUtilisateur " +
                    "WHERE m.Titre = ? AND im.Validation= 'R' ";

            try (Connection c = DBConnection.getInstance();
                 PreparedStatement statement = c.prepareStatement(sql)) {
                statement.setString(1, moduleTitre);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        long id = resultSet.getInt("idEtudiant");
                        String cne = resultSet.getString("cne");
                        String nom = resultSet.getString("nom");
                        String prenom = resultSet.getString("prenom");
                        Etudiant studentInfo = new Etudiant(id, cne, nom, prenom);

                        studentInfoList.add(studentInfo);
                    }
                }
            } catch (SQLException e) {
                throw new DataBaseException("Failed to retrieve student information from the database", e);
            }
            return studentInfoList;
        }


    }

