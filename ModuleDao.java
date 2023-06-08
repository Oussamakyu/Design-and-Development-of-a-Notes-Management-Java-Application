package org.example.dao;

// Author Rim Lfellous

import org.apache.log4j.Logger;
import org.example.bo.Module;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModuleDao {
    private Logger logger = Logger.getLogger(getClass());



    public Module getModuleByTitre(String Titre) throws DataBaseException {
        String sql = "SELECT * FROM module WHERE titre = ?";
        try {Connection c = DBConnection.getInstance();
            PreparedStatement statement = c.prepareStatement(sql); {
                statement.setString(1, Titre);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return createModuleFromResultSet(resultSet);
                    }
                }}
        } catch (SQLException e) {
            throw new DataBaseException("Failed to retrieve module from the database", e);
        }
        return null;
    }




    public List<Module> getModulesByNiveauTitre(String niveauTitre) throws DataBaseException {
        List<Module> modules = new ArrayList<>();
        String sql = "SELECT m.* FROM module m INNER JOIN niveau n ON m.idNiveau = n.idNiveau WHERE n.titre = ?";

        try (Connection c = DBConnection.getInstance();
             PreparedStatement statement = c.prepareStatement(sql)) {

            statement.setString(1, niveauTitre);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Module module = createModuleFromResultSet(resultSet);
                modules.add(module);
            }
        } catch (SQLException e) {
            throw new DataBaseException("Error retrieving modules by niveau name.", e);
        }

        return modules;
    }

    public Module findModuleByTitre(String titre) throws DataBaseException {

        try {
            Connection c = DBConnection.getInstance();
            String sql = "SELECT * FROM module WHERE titre = ?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1, titre);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Module module = createModuleFromResultSet(rs);
                rs.close();
                return module;
            }
        }catch (SQLException e) {
            logger.error("Erreur à cause de : ",e);
            throw new DataBaseException("Failed to find module by titre in the database", e);
        }

        return null; // Module with the given titre not found
    }

    private Module createModuleFromResultSet(ResultSet resultSet) throws SQLException {
        Module module = new Module();
        module.setIdModule(resultSet.getLong("idModule"));
        module.setCode(resultSet.getString("code"));
        module.setTitre(resultSet.getString("titre"));
        // Set other properties of Module as needed
        return module;
    }




    public static void insertDataIntoDatabase(String ModuleTitre, long ID, double moyenne, String validation, Connection con) throws SQLException {
        String selectQuery = "SELECT idModule FROM Module WHERE titre = ?";
        String updateQuery = "UPDATE InscriptionModule im " +
                "JOIN inscriptionAnnuelle ia ON ia.idInscription = im.idInscription " +
                "JOIN Etudiant e ON ia.idEtudiant = e.idEtudiant " +
                "SET im.NoteSN = ?, im.Validation = ? " +
                "WHERE ia.idEtudiant = ? AND im.idModule = ?";

        try (PreparedStatement selectStatement = con.prepareStatement(selectQuery);
             PreparedStatement updateStatement = con.prepareStatement(updateQuery)) {

            // Retrieve the module ID
            selectStatement.setString(1, ModuleTitre);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int moduleId = resultSet.getInt("idModule");

                // Update the InscriptionModule table
                updateStatement.setDouble(1, moyenne);
                updateStatement.setString(2, validation);
                updateStatement.setLong(3, ID);
                updateStatement.setInt(4, moduleId);
                updateStatement.executeUpdate();
            }
        }
    }

    public static void insertDataIntoDatabaseSR(String ModuleTitre, long ID, double moyenne, String validation, Connection con) throws SQLException {
        String selectQuery = "SELECT idModule FROM Module WHERE titre = ?";
        String updateQuery = "UPDATE InscriptionModule im " +
                "JOIN inscriptionAnnuelle ia ON ia.idInscription = im.idInscription " +
                "JOIN Etudiant e ON ia.idEtudiant = e.idEtudiant " +
                "SET im.NoteSR = ?, im.Validation = ? " +
                "WHERE ia.idEtudiant = ? AND im.idModule = ?";

        try (PreparedStatement selectStatement = con.prepareStatement(selectQuery);
             PreparedStatement updateStatement = con.prepareStatement(updateQuery)) {

            // Retrieve the module ID
            selectStatement.setString(1, ModuleTitre);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int moduleId = resultSet.getInt("idModule");

                // Update the InscriptionModule table
                updateStatement.setDouble(1, moyenne);
                updateStatement.setString(2, validation);
                updateStatement.setLong(3, ID);
                updateStatement.setInt(4, moduleId);
                updateStatement.executeUpdate();
            }
        }
    }






}