package com.ensah.dao;

import com.ensah.bo.Filiere;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FiliereDao {
    private Logger logger = Logger.getLogger(NiveauDao.class);
    public boolean createFiliere(Filiere filiere) throws DataBaseException {
        String query = "INSERT INTO filiere (anneeFinaccreditation,anneeaccreditation,codeFiliere,titreFiliere) VALUES (?,?,?,?);";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, filiere.getAnneeFinaccreditation());
            statement.setInt(2,filiere.getAnneeaccreditation());
            statement.setString(3,filiere.getCodeFiliere());
            statement.setString(4,filiere.getTitreFiliere());
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

    public boolean updateFiliere(Filiere filiere) throws DataBaseException {
        String query = "UPDATE filiere SET anneeFinaccreditation = ?,anneeaccreditation=? ,codeFiliere = ?,titreFiliere=? WHERE idFiliere = ?";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, filiere.getAnneeFinaccreditation());
            statement.setInt(2,filiere.getAnneeaccreditation());
            statement.setString(3,filiere.getCodeFiliere());
            statement.setString(4,filiere.getTitreFiliere());
            statement.setLong(5, filiere.getIdFiliere());
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        } catch (DataBaseException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        }
    }

    public boolean deleteFiliere(long filiereId) throws DataBaseException {
        String query = "DELETE FROM filiere WHERE idFiliere = ?";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, filiereId);
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

    public Filiere findFiliere(long filiereId) throws DataBaseException {
        String query = "SELECT * FROM filiere WHERE idFiliere = ?";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, filiereId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int anneeFinAccreditation = resultSet.getInt("anneeFinaccreditation");
                int anneeAccreditation = resultSet.getInt("anneeaccreditation");
                String codeFiliere = resultSet.getString("codeFiliere");
                String titreFiliere = resultSet.getString("titreFiliere");
                Filiere filiere = new Filiere(filiereId,titreFiliere,codeFiliere,anneeAccreditation,anneeFinAccreditation);
                filiere.setIdFiliere(filiereId);
                return filiere;
            } else {
                return null;
            }
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        } catch (DataBaseException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        }
    }

    public List<Filiere> getAllFiliere() throws DataBaseException {
        String query = "SELECT * FROM filiere ";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            List<Filiere> fls=new ArrayList<>();
            while (resultSet.next()) {
                long filiereId = resultSet.getLong("idFiliere");
                int anneeFinAccreditation = resultSet.getInt("anneeFinaccreditation");
                int anneeAccreditation = resultSet.getInt("anneeaccreditation");
                String codeFiliere = resultSet.getString("codeFiliere");
                String titreFiliere = resultSet.getString("titreFiliere");
                Filiere filiere = new Filiere(filiereId, titreFiliere, codeFiliere, anneeAccreditation, anneeFinAccreditation);
                filiere.setIdFiliere(filiereId);
                fls.add(filiere);
            }
            return fls;
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        } catch (DataBaseException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        }
    }

}

