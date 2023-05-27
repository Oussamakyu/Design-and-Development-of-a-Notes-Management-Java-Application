package com.ensah.dao;
import com.ensah.bo.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.Date;


import org.apache.log4j.Logger;



public class EtudiantDao {
    private Logger logger = Logger.getLogger(EtudiantDao.class);
    public static String convertToArabicName(String name) {
        // Normalize the name by removing diacritical marks
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);

        // Replace non-Arabic characters with their Arabic equivalents
        String converted = normalized.replaceAll("[^\\p{InArabic}]+", "");

        return converted;
    }

    public void inscrire(Etudiant e) throws DataBaseException {
        Connection con = DBConnection.getInstance();

        String sqlInsertEtudiant = "INSERT INTO Etudiant (cne, dateNaissance, idEtudiant) VALUES (?, ?, ?);";
        String sqlInsertUtilisateur = "INSERT INTO Utilisateur (idUtilisateur, cin, email, nom, nomArabe, photo, prenom, prenomArabe, telephone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement stmEtudiant = con.prepareStatement(sqlInsertEtudiant);
             PreparedStatement stmUtilisateur = con.prepareStatement(sqlInsertUtilisateur)) {

            // Insert statement for Etudiant table
            stmEtudiant.setString(1, e.getCne());
            stmEtudiant.setDate(2, new java.sql.Date(e.getDateNaissance().getTime()));
            stmEtudiant.setLong(3, e.getIdUtilisateur());
            stmEtudiant.executeUpdate();

            // Insert statement for Utilisateur table
            stmUtilisateur.setLong(1, e.getIdUtilisateur());
            stmUtilisateur.setString(2, e.getCin());
            stmUtilisateur.setString(3, e.getEmail());
            stmUtilisateur.setString(4, e.getNom());
            stmUtilisateur.setString(5, convertToArabicName(e.getNom()));
            stmUtilisateur.setString(6, null);
            stmUtilisateur.setString(7, e.getPrenom());
            stmUtilisateur.setString(8, convertToArabicName(e.getPrenom()));
            stmUtilisateur.setString(9, e.getTelephone());
            stmUtilisateur.executeUpdate();
            logger.info("l'inscription de l'étudiant  "+e.getNom()+" a été effectué avec succès ");

        } catch (SQLException ex) {
            logger.error("Erreur de ", ex);
            throw new DataBaseException(ex);
        }
    }




    public void reInscrire(Etudiant e) throws DataBaseException {
        Connection con = DBConnection.getInstance();
        String sqlUpdateUtilisateur = "UPDATE Utilisateur SET nom = ?, prenom = ?, cin = ?, email = ?, telephone = ?, nomArabe = ?, prenomArabe = ?, photo = ? WHERE idUtilisateur = ?;";
        String sqlUpdateEtudiant = "UPDATE Etudiant SET cne = ?, dateNaissance = ? WHERE idEtudiant = ?;";

        try (PreparedStatement stmUtilisateur = con.prepareStatement(sqlUpdateUtilisateur);
             PreparedStatement stmEtudiant = con.prepareStatement(sqlUpdateEtudiant)) {

            stmUtilisateur.setString(1, e.getNom());
            stmUtilisateur.setString(2, e.getPrenom());
            stmUtilisateur.setString(3, e.getCin());
            stmUtilisateur.setString(4, e.getEmail());
            stmUtilisateur.setString(5, e.getTelephone());
            stmUtilisateur.setString(6, convertToArabicName(e.getNom()));
            stmUtilisateur.setString(7, convertToArabicName(e.getPrenom()));
            stmUtilisateur.setString(8, null); // Assuming photo is of type VARCHAR(255) in the Utilisateur table
            stmUtilisateur.setLong(9, e.getIdUtilisateur());
            stmUtilisateur.executeUpdate();

            stmEtudiant.setString(1, e.getCne());
            stmEtudiant.setDate(2,  (java.sql.Date) e.getDateNaissance());
            stmEtudiant.setLong(3, e.getIdUtilisateur());
            stmEtudiant.executeUpdate();
            logger.info("la réinscription de l'étudiant  "+e.getNom()+" a été effectué avec succès ");

        } catch (SQLException ex) {
            logger.error("Erreur de ", ex);
            throw new DataBaseException(ex);
        }
    }

    public boolean etuExist(Etudiant e) throws DataBaseException {
        Connection con = DBConnection.getInstance();
        String sqlSelect = "SELECT * from Etudiant where IdEtudiant = ?;";
        Boolean exist = false;
        try(PreparedStatement stm = con.prepareStatement(sqlSelect)){
            stm.setLong(1,e.getIdUtilisateur());
            ResultSet resultSet = stm.executeQuery();
            if(resultSet.next())
                exist=true;
        }catch(SQLException ex){
            logger.error("Erreur de ", ex);
            throw new DataBaseException(ex);
        }
        return exist ;

    }

    public boolean checkLevel(Long newLevel , Etudiant e) throws DataBaseException {
        Connection con =  DBConnection.getInstance();
        //à compléter plus tard
//        --
//                ALTER TABLE `inscriptionannuelle`
//        ADD PRIMARY KEY (`idInscription`),
//                ADD KEY `FKge2xwqtfeqnojw9no8og6vbqn` (`idEtudiant`),
//        ADD KEY `FK9lrdmhkam481adiwotdpqo8w` (`idNiveau`);
        return true;
    }









}
