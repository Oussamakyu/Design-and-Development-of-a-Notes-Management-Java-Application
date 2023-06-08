package org.example.dao;

// @Author Rim Lfellous

import org.example.bo.Element;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

public class ElementDao {
    private Logger logger = Logger.getLogger(this.getClass());

    public ElementDao() {
    }

    private Element createElementFromResultSet(ResultSet resultSet) throws SQLException {
        Element element = new Element();
        element.setCode(resultSet.getString("code"));
        element.setCurrentCoefficient(resultSet.getDouble("currentCoefficient"));
        element.setNom(resultSet.getString("nom"));
        return element;
    }

    public List<Element> getElementsByModule(String moduleTitre) throws DataBaseException {
        List<Element> elements = new ArrayList<>();
        String sql = "SELECT * FROM element WHERE idModule = (SELECT idModule FROM module WHERE titre = ?)";

        try {Connection c = DBConnection.getInstance();
            PreparedStatement statement = c.prepareStatement(sql); {
                statement.setString(1, moduleTitre);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        long id = resultSet.getLong("idMatiere");
                        String titre = resultSet.getString("nom");

                        Element element = createElementFromResultSet(resultSet);

                        elements.add(element);
                    }
                }}
        } catch (SQLException e) {
            throw new DataBaseException("Failed to retrieve elements from the database", e);
        }
        return elements;
    }


    public static void insertDataintoDatabase(String element1, String element2, long ID, double e1, double e2, Connection con) throws SQLException {
        String selectQuery = "SELECT idMatiere FROM element WHERE nom = ? OR nom = ?";
        String updateQuery = "UPDATE inscriptionMatiere ima " +
                "JOIN inscriptionAnnuelle ia ON ia.idInscription = ima.idInscription " +
                "JOIN element el ON el.idMatiere = ima.idMatiere " +
                "JOIN Etudiant e ON e.idEtudiant = ia.idEtudiant " +
                "SET ima.NoteSN = CASE " +
                "  WHEN el.nom = ? THEN ? " +
                "  WHEN el.nom = ? THEN ? " +
                "  ELSE ima.NoteSN " +
                "END " +
                "WHERE e.idEtudiant = ? AND (el.nom = ? OR el.nom = ?)";

        try (PreparedStatement selectStatement = con.prepareStatement(selectQuery);
             PreparedStatement updateStatement = con.prepareStatement(updateQuery)) {

            // Retrieve the element IDs
            selectStatement.setString(1, element1);
            selectStatement.setString(2, element2);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {


                    // Update the inscriptionMatiere table for element1
                    updateStatement.setString(1, element1);
                    updateStatement.setDouble(2, e1);
                    updateStatement.setString(3, element2);
                    updateStatement.setDouble(4, e2);
                    updateStatement.setLong(5, ID);
                    updateStatement.setString(6, element1);
                    updateStatement.setString(7, element2);
                    updateStatement.executeUpdate();

                    // Update the inscriptionMatiere table for element2
                    updateStatement.setString(1, element1);
                    updateStatement.setDouble(2, e1);
                    updateStatement.setString(3, element2);
                    updateStatement.setDouble(4, e2);
                    updateStatement.setLong(5, ID);
                    updateStatement.setString(6, element1);
                    updateStatement.setString(7, element2);
                    updateStatement.executeUpdate();
                }
            }
        }

    public void insertDataintoDatabaseSR(String element1, String element2, long ID, double e1, double e2, Connection con) throws SQLException {
        String selectQuery = "SELECT idMatiere FROM element WHERE nom = ? OR nom = ?";
        String updateQuery = "UPDATE inscriptionMatiere ima " +
                "JOIN inscriptionAnnuelle ia ON ia.idInscription = ima.idInscription " +
                "JOIN element el ON el.idMatiere = ima.idMatiere " +
                "JOIN Etudiant e ON e.idEtudiant = ia.idEtudiant " +
                "SET ima.NoteSR =  CASE " +
                "  WHEN el.nom = ? THEN ? " +
                "  WHEN el.nom = ? THEN ? " +
                "  ELSE ima.NoteSN " +
                "END " +
                "WHERE e.idEtudiant = ? AND (el.nom = ? OR el.nom = ?)";

        try (PreparedStatement selectStatement = con.prepareStatement(selectQuery);
             PreparedStatement updateStatement = con.prepareStatement(updateQuery)) {

            // Retrieve the element IDs
            selectStatement.setString(1, element1);
            selectStatement.setString(2, element2);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {

                    // Update the inscriptionMatiere table for element1
                    updateStatement.setString(1, element1);
                    updateStatement.setDouble(2, e1);
                    updateStatement.setString(3, element2);
                    updateStatement.setDouble(4, e2);
                    updateStatement.setLong(5, ID);
                    updateStatement.setString(6, element1);
                    updateStatement.setString(7, element2);
                    updateStatement.executeUpdate();

                    // Update the inscriptionMatiere table for element2
                    updateStatement.setString(1, element1);
                    updateStatement.setDouble(2, e1);
                    updateStatement.setString(3, element2);
                    updateStatement.setDouble(4, e2);
                    updateStatement.setLong(5, ID);
                    updateStatement.setString(6, element1);
                    updateStatement.setString(7, element2);
                    updateStatement.executeUpdate();
                }
            }
        }


 public String getValidationResultFromDB(String alias, long ID, Connection con) throws SQLException {
        String validationResult = null;
        String selectQuery = "SELECT NoteSN FROM inscriptionMatiere " +
                "JOIN InscriptionAnnuelle ia ON ia.idInscription = inscriptionMatiere.idInscription " +
                "WHERE ia.idEtudiant = ?";
        String updateQuery = "UPDATE inscriptionMatiere " +
                "JOIN InscriptionAnnuelle ia ON ia.idInscription = inscriptionMatiere.idInscription " +
                "SET inscriptionMatiere.Validation = ? " +
                "WHERE ia.idEtudiant = ?";


        try (PreparedStatement selectStatement = con.prepareStatement(selectQuery);
             PreparedStatement updateStatement = con.prepareStatement(updateQuery)) {
            selectStatement.setLong(1, ID);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                double noteSN = resultSet.getDouble("NoteSN");
                validationResult = getValidationResults(alias, noteSN);
            }

            updateStatement.setString(1, validationResult != null ? validationResult : "NULL");
            updateStatement.setLong(2, ID);
            updateStatement.executeUpdate();
        }

        return validationResult;
    }

    public String getValidationResultDB(String alias, long ID, Connection con) throws SQLException {
        String validationResult = null;
        String selectQuery = "SELECT NoteSR FROM inscriptionMatiere " +
                "JOIN InscriptionAnnuelle ia ON ia.idInscription = inscriptionMatiere.idInscription " +
                "WHERE ia.idEtudiant = ?";
        String updateQuery = "UPDATE inscriptionMatiere " +
                "JOIN InscriptionAnnuelle ia ON ia.idInscription = inscriptionMatiere.idInscription " +
                "SET inscriptionMatiere.Validation = ? " +
                "WHERE ia.idEtudiant = ?";

        try (PreparedStatement selectStatement = con.prepareStatement(selectQuery);
             PreparedStatement updateStatement = con.prepareStatement(updateQuery)) {
            selectStatement.setLong(1, ID);
            ResultSet resultSet = selectStatement.executeQuery();

        if (resultSet.next()) {
            double noteSR = resultSet.getDouble("NoteSR");
            validationResult = getValidationResult(alias, noteSR);
        }

        updateStatement.setString(1, validationResult != null ? validationResult : "NULL");
        updateStatement.setLong(2, ID);
        updateStatement.executeUpdate();
    }

        return validationResult;
    }


    private String getValidationResults(String alias, double note) {
        String valide = "V";
        if (alias.equalsIgnoreCase("CP1") || alias.equalsIgnoreCase("CP2")) {
                if (note < 10) {
                    valide = "R";
                }
            } else {
                if (note < 12) {
                    valide = "R";
                }
            }

            return valide;
        }

    private String getValidationResult(String alias, double note) {
        String valide = "V";

        if (alias.equalsIgnoreCase("CP1") || alias.equalsIgnoreCase("CP2")) {
            if (note < 10) {
                valide = "R";
            }
        } else {
            if (note < 12) {
                valide = "R";
            }
        }

        return valide;
    }

}



