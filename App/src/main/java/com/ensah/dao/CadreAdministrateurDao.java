package com.ensah.dao;



import com.ensah.bo.CadreAdministrateur;
import com.ensah.bo.Compte;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CadreAdministrateurDao {
    private Logger logger = Logger.getLogger(InscriptionAnnuelleDao.class);

    public void saveCadreAdministrateur(CadreAdministrateur cadreAdministrateur) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Get a database connection
            connection = DBConnection.getInstance();

            // Create the SQL query
            String query = "INSERT INTO cadreadministrateur (idCadreAdmin, name, email) VALUES (?, ?, ?)";

            // Create a prepared statement
            statement = connection.prepareStatement(query);

            // Set the parameter values
            statement.setLong(1, cadreAdministrateur.getIdUtilisateur());
            statement.setString(2, cadreAdministrateur.getNom());
            statement.setString(3, cadreAdministrateur.getEmail());

            // Execute the query
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DataBaseException e) {
            throw new RuntimeException(e);
        }
    }

    public CadreAdministrateur getCadreAdministrateurById(long idCadreAdmin) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        CadreAdministrateur cadreAdministrateur = null;

        try {
            // Get a database connection
            connection = DBConnection.getInstance();

            // Create the SQL query
            String query = "SELECT * FROM cadreadministrateur WHERE idCadreAdmin = ?";

            // Create a prepared statement
            statement = connection.prepareStatement(query);

            // Set the parameter value
            statement.setLong(1, idCadreAdmin);

            // Execute the query
            resultSet = statement.executeQuery();

            // Retrieve the data from the result set
            if (resultSet.next()) {
                cadreAdministrateur = new CadreAdministrateur();
                cadreAdministrateur.setIdUtilisateur(resultSet.getLong("idCadreAdmin"));
                cadreAdministrateur.setNom(resultSet.getString("name"));
                cadreAdministrateur.setEmail(resultSet.getString("email"));
                // Set other attributes as needed
            }
        } catch (SQLException | DataBaseException e) {
            e.printStackTrace();
        }

        return cadreAdministrateur;
    }

    public List<CadreAdministrateur> getAllCadreAdministrateurs() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<CadreAdministrateur> cadreAdministrateurs = new ArrayList<>();

        try {
            // Get a database connection
            connection = DBConnection.getInstance();

            // Create the SQL query
            String query = "SELECT * FROM cadreadministrateur";

            // Create a prepared statement
            statement = connection.prepareStatement(query);

            // Execute the query
            resultSet = statement.executeQuery();

            // Retrieve the data from the result set
            while (resultSet.next()) {
                CadreAdministrateur cadreAdministrateur = new CadreAdministrateur();
                cadreAdministrateur.setIdUtilisateur(resultSet.getLong("idCadreAdmin"));
                cadreAdministrateur.setNom(resultSet.getString("name"));
                cadreAdministrateur.setEmail(resultSet.getString("email"));
                // Set other attributes as needed
                cadreAdministrateurs.add(cadreAdministrateur);
            }
        } catch (SQLException | DataBaseException e) {
            e.printStackTrace();
        }

        return cadreAdministrateurs;
    }

    public void updateCadreAdministrateur(CadreAdministrateur cadreAdministrateur) {
        // Implementation to update the cadreAdministrateur object in the database
    }

    public void deleteCadreAdministrateur(int idCadreAdmin) {
        // Implementation to delete the cadreAdministrateur object from the database
    }

    public void saveCompte(Compte compte) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Get a database connection
            connection = DBConnection.getInstance();

            // Create the SQL query
            String query = "INSERT INTO compte (idAdministrateur, login, password) VALUES (?, ?, ?)";

            // Create a prepared statement
            statement = connection.prepareStatement(query);

            // Set the parameter values
            statement.setLong(1, compte.getIdCompte());
            statement.setString(2, compte.getLogin());
            statement.setString(3, compte.getPassword());

            // Execute the query
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DataBaseException e) {
            throw new RuntimeException(e);
        }
    }

    public Compte getCompteByLogin(String login) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Compte compte = null;

        try {
            // Get a database connection
            connection = DBConnection.getInstance();

            // Create the SQL query
            String query = "SELECT * FROM compte WHERE login = ?";

            // Create a prepared statement
            statement = connection.prepareStatement(query);

            // Set the parameter value
            statement.setString(1, login);

            // Execute the query
            resultSet = statement.executeQuery();

            // Retrieve the data from the result set
            if (resultSet.next()) {
                compte = new Compte();
                compte.setIdCompte(resultSet.getLong("idCompte"));
                compte.setIdCompte(resultSet.getLong("idAdministrateur"));
                compte.setLogin(resultSet.getString("login"));
                compte.setPassword(resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DataBaseException e) {
            throw new RuntimeException(e);
        }
        return compte;
    }

    public long getAdminIdByEmail(String email) throws  DataBaseException {
        long adminId = -1; // Default value if admin ID is not found

        Connection connection = DBConnection.getInstance(); // Implement your own method to get a database connection
        String query = "SELECT idAdmin FROM admin WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                adminId = resultSet.getLong("idAdmin");
            }
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }
        return adminId;
    }

    public boolean verifyCadreAdministrateur(String firstName, String lastName, String email, String telephone)
            throws SQLException, DataBaseException {
        boolean exists = false;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnection.getInstance(); // Implement your own method to get a database connection

            String query = "SELECT COUNT(*) AS count FROM cadreadministrateur WHERE Prenom = ? AND Nom = ? AND Email = ? AND Telephone = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, telephone);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                exists = count > 0;
            }
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }

        return exists;
    }

    public boolean verifyLogin(String login, String password) throws DataBaseException {
        boolean validLogin = false;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnection.getInstance();

            String query = "SELECT COUNT(*) AS count FROM compte WHERE login = ? AND Password = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, login);
            statement.setString(2, password);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                validLogin = count > 0;
            }
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }

        return validLogin;
    }


}


