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



}

