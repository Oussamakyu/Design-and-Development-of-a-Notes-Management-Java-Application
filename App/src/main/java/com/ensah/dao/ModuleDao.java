package com.ensah.dao;

import com.ensah.bo.Niveau;
import org.apache.log4j.Logger;
import com.ensah.bo.Element;
import com.ensah.bo.Module;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModuleDao {

    private Logger logger = Logger.getLogger(ModuleDao.class);
    public List<Element> getElements(long pModuleId) throws DataBaseException{
        List<Element> modulesId = new ArrayList<>();
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement idstm = c.prepareStatement("SELECT idMatiere,currentCoefficient FROM element WHERE idModule=?");
            idstm.setLong(1, pModuleId);
            ResultSet rs = idstm.executeQuery();
            while (rs.next()) {
                Element element=new Element();
                element.setIdMatiere(rs.getLong("idMatiere"));
                element.setCurrentCoefficient(rs.getLong("currentCoefficient"));
                modulesId.add(element);
            }

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }

        return modulesId;
    }

    public List<Element> getElementsByName(String pModuleName) throws DataBaseException{
        List<Element> modulesId = new ArrayList<>();
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement idstm = c.prepareStatement("SELECT idMatiere,currentCoefficient,e.nom FROM element e,module m WHERE e.idModule=m.idModule AND m.titre=?");
            idstm.setString(1, pModuleName);
            ResultSet rs = idstm.executeQuery();
            while (rs.next()) {
                Element element=new Element();
                element.setIdMatiere(rs.getLong("idMatiere"));
                element.setCurrentCoefficient(rs.getLong("currentCoefficient"));
                element.setNom(rs.getString("nom"));
                modulesId.add(element);
            }

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }

        return modulesId;
    }

    public boolean createModule(Module m) throws DataBaseException {
        String query = "INSERT INTO module(idModule,code,titre,idNiveau) VALUES (?,?,?,?);";
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement statement = c.prepareStatement(query);
            statement.setLong(1,m.getIdModule());
            statement.setString(2,m.getCode());
            statement.setString(3,m.getTitre());
            statement.setLong(4,m.getNiveau().getIdNiveau());
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

    public boolean updateModule(Module module) throws DataBaseException {
        String query = "UPDATE module SET title = ?, code = ? WHERE idModule = ?";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, module.getTitre());
            statement.setString(2, module.getCode());
            statement.setLong(3, module.getIdModule());
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


    public boolean deleteModule(long moduleId) throws DataBaseException {
        String query = "DELETE FROM module WHERE idModule = ?";
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement statement = c.prepareStatement(query);
            statement.setLong(1, moduleId);
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

    public List<Module> getAllModules() throws DataBaseException {
        List<Module> modules = new ArrayList<>();
        String query = "SELECT * FROM module";
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement statement = c.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("idModule");
                String titre = resultSet.getString("titre");
                String code = resultSet.getString("code");
                long idNiveau = resultSet.getLong("idNiveau");
                Module module = new Module(id, titre,code,new Niveau(idNiveau));
                modules.add(module);
            }
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        } catch (DataBaseException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        }
        return modules;
    }

    public Module findModule(long moduleId) throws DataBaseException {
        NiveauDao niveauDao = new NiveauDao();
        String query = "SELECT * FROM module WHERE idModule = ?";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, moduleId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String code = resultSet.getString("code");
                String titre = resultSet.getString("titre");
                long niveauId = resultSet.getLong("idNiveau");
                Niveau niveau = niveauDao.findNiveauById(niveauId);

                Module module = new Module(moduleId,code, titre, niveau);
                module.setIdModule(moduleId);
                return module;
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

    public boolean associateModulesToNiveau(long niveauId, List<Module> modules) throws DataBaseException {
        String query = "INSERT INTO module ( code, titre, idNiveau) VALUES ( ?, ?, ?)";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);

            for (Module module : modules) {
                statement.setString(1, module.getTitre());
                statement.setString(2, module.getCode());
                statement.setLong(3, niveauId);
                statement.addBatch();
            }

            int[] rowsInserted = statement.executeBatch();
            return rowsInserted.length == modules.size();
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        } catch (DataBaseException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        }
    }




    public List<Module> getModulesByClass(long niveauId) throws DataBaseException {
        String query = "SELECT * FROM module WHERE idNiveau = ?";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, niveauId);
            ResultSet resultSet = statement.executeQuery();

            List<Module> modules = new ArrayList<>();
            while (resultSet.next()) {
                long moduleId = resultSet.getLong("idModule");
                String code = resultSet.getString("code");
                String titre = resultSet.getString("titre");
                long idNiveau = resultSet.getLong("idNiveau");
                Module module = new Module(moduleId, code, titre, new Niveau(idNiveau));
                modules.add(module);
            }

            return modules;
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        } catch (DataBaseException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        }
    }

    public boolean assignCoordinatorToFiliere(long filiereId, long coordinatorId) throws DataBaseException {
        String query = "INSERT INTO coordinator_filiere (idCoordinator, filiereId) VALUES (?, ?)";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);
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



