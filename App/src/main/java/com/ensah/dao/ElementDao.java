package com.ensah.dao;

import com.ensah.bo.Element;
import com.ensah.bo.Module;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ElementDao {
    private Connection connection;
    private Logger logger = Logger.getLogger(NiveauDao.class);


    public boolean createElement(Element element) throws DataBaseException {
        String query = "INSERT INTO element (idMatiere, idModule) VALUES (?, ?);";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, element.getIdMatiere());
            statement.setLong(2, element.getModule().getIdModule());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        }
    }

    public boolean updateElement(Element element) throws DataBaseException {
        String query = "UPDATE element SET  code=?, currentCoefficient=? ,nom=?,idModule = ? WHERE idMatiere = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,element.getCode());
            statement.setDouble(2,element.getCurrentCoefficient());
            statement.setString(3,element.getNom());
            statement.setLong(1, element.getModule().getIdModule());
            statement.setLong(2, element.getIdMatiere());
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        }
    }

    public boolean deleteElement(long matiereId) throws DataBaseException {
        String query = "DELETE FROM element WHERE idMatiere = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, matiereId);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        }
    }

    public List<Element> getAllElements() throws DataBaseException {
        List<Element> elements = new ArrayList<>();
        String query = "SELECT * FROM element;";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long matiereId = resultSet.getLong("idMatiere");
                long moduleId = resultSet.getLong("idModule");
                String code = resultSet.getString("code");
                String name = resultSet.getString("nom");
                double currentCoefficient = resultSet.getDouble("currentCoefficient");

                Element element = new Element(matiereId,name,code,currentCoefficient,new Module(moduleId));
                //we will get the name of the module using the moduleId
                elements.add(element);
            }
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        }
        return elements;
    }

    public boolean associateElementsToModule(long moduleId, List<Element> elements) throws DataBaseException {
        String query = "INSERT INTO element (idMatiere, code, currentCoefficient, nom, idModule) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);

            for (Element element : elements) {
                statement.setLong(1, element.getIdMatiere());
                statement.setString(2, element.getCode());
                statement.setDouble(3, element.getCurrentCoefficient());
                statement.setString(4, element.getNom());
                statement.setLong(5, moduleId);
                statement.addBatch();
            }

            int[] rowsInserted = statement.executeBatch();
            return rowsInserted.length == elements.size();
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        } catch (DataBaseException e) {
            throw new RuntimeException(e);
        }
    }

    public Element findElementById(long elementId) {
        String query = "SELECT * FROM element WHERE idMatiere = ?";
        ModuleDao module = new ModuleDao();
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, elementId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                long idMatiere = resultSet.getLong("idMatiere");
                String code = resultSet.getString("code");
                double currentCoefficient = resultSet.getDouble("currentCoefficient");
                String nom = resultSet.getString("nom");
                long idModule = resultSet.getLong("idModule");

                // Retrieve the associated module
                Module m = module.findModule(idModule);

                return new Element(idMatiere, code, nom, currentCoefficient, m);
            }

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (DataBaseException e) {
            throw new RuntimeException(e);
        }
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
                int elementId1 = resultSet.getInt(1); // Retrieve first element ID

                if (resultSet.next()) {
                    int elementId2 = resultSet.getInt(1); // Retrieve second element ID

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
                int elementId1 = resultSet.getInt(1); // Retrieve first element ID

                if (resultSet.next()) {
                    int elementId2 = resultSet.getInt(1); // Retrieve second element ID

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







}

