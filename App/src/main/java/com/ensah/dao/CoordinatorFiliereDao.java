package com.ensah.dao;

import com.ensah.bo.Filiere;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorFiliereDao {
    private Logger logger = Logger.getLogger(ModuleDao.class);
    public boolean createCoordinatorFiliere(long coordinatorId, long filiereId) throws DataBaseException {
        // Check if the coordinatorId exists in the enseignant table
        String checkQuery = "SELECT COUNT(*) FROM enseignant WHERE idEnseignant = ?";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setLong(1, coordinatorId);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            if (count == 0) {
                return false;
            }
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        } catch (DataBaseException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        }

        // Proceed with creating the record in coordinator_filiere table
        String insertQuery = "INSERT INTO coordinator_filiere (idCoordinator, filiereId) VALUES (?, ?)";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setLong(1, coordinatorId);
            statement.setLong(2, filiereId);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        } catch (DataBaseException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        }
    }


    public boolean deleteCoordinatorFiliere(long coordinatorId, long filiereId) throws DataBaseException {
        String query = "DELETE FROM coordinator_filiere WHERE idCoordinator = ? AND filiereId = ?";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, coordinatorId);
            statement.setLong(2, filiereId);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        } catch (DataBaseException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        }
    }

    public List<Filiere> getFilieresForCoordinator(long coordinatorId) throws DataBaseException {
        List<Filiere> filieres = new ArrayList<>();
        String query = "SELECT f.* FROM filiere f JOIN coordinator_filiere cf ON f.idFiliere = cf.filiereId WHERE cf.idCoordinator = ?";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, coordinatorId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long filiereId = resultSet.getLong("idFiliere");
                String titreFiliere = resultSet.getString("titreFiliere");
                // Retrieve other filière attributes

                // Create Filiere object
                Filiere filiere = new Filiere(filiereId, titreFiliere);
                // Set other filière attributes

                filieres.add(filiere);
            }
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        } catch (DataBaseException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        }
        return filieres;
    }
}

