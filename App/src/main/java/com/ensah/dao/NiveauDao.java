package com.ensah.dao;

import com.ensah.bo.Etudiant;
import com.ensah.bo.Filiere;
import org.apache.log4j.Logger;
import com.ensah.bo.Module;
import com.ensah.bo.Niveau;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NiveauDao {

    private Logger logger = Logger.getLogger(NiveauDao.class);
    private FiliereDao filiereDao = new FiliereDao();

    public Niveau getNiveau(long pNiveauId) throws DataBaseException {
        Niveau niveau = null;
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement idstm = c.prepareStatement("SELECT * FROM niveau WHERE idNiveau=?");
            idstm.setLong(1, pNiveauId);
            ResultSet rs = idstm.executeQuery();
            if (rs.next()) {
                niveau = new Niveau(rs.getLong(1), rs.getString(2), rs.getString(3));
            }

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }
        return niveau;
    }

    public long getNextNiveau(long pNiveauId) throws DataBaseException {
        long idNextNiveau = 0;
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement idstm = c.prepareStatement("SELECT idNextNiveau FROM niveau WHERE idNiveau=?");
            idstm.setLong(1, pNiveauId);
            ResultSet rs = idstm.executeQuery();
            if (rs.next()) {
                idNextNiveau = rs.getLong(1);
            }

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }
        return idNextNiveau;
    }

    public List<Module> getModules(long pNiveauId) throws DataBaseException {
        List<Module> modulesId = new ArrayList<>();
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement idstm = c.prepareStatement("SELECT idModule FROM module WHERE idNiveau=?");
            idstm.setLong(1, pNiveauId);
            ResultSet rs = idstm.executeQuery();
            while (rs.next()) {
                Module module = new Module();
                module.setIdModule(rs.getLong("idModule"));
                modulesId.add(module);
            }

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }

        return modulesId;
    }

    public List<Module> getModulesByAlias(String pNiveauAlias) throws DataBaseException {
        List<Module> modulesTitre = new ArrayList<>();
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement idstm = c.prepareStatement("SELECT m.titre FROM module m,niveau n WHERE n.idNiveau=m.idNiveau AND n.alias=?");
            idstm.setString(1, pNiveauAlias);
            ResultSet rs = idstm.executeQuery();
            while (rs.next()) {
                Module module = new Module();
                module.setTitre(rs.getString("titre"));
                modulesTitre.add(module);
            }

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }
        return modulesTitre;
    }

    public List<Etudiant> getStudentsByAlias(String pNiveauAlias) throws DataBaseException {
        List<Etudiant> studentsInfos = new ArrayList<>();
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement idstm = c.prepareStatement("SELECT i.idEtudiant,e.cne,u.nom,u.prenom,i.idInscription FROM inscriptionannuelle i,etudiant e,utilisateur u,niveau n WHERE i.idEtudiant=e.idEtudiant AND e.idEtudiant=u.idUtilisateur AND n.idNiveau=i.idNiveau AND n.alias=? AND i.annee=(SELECT max(annee) FROM inscriptionannuelle);");
            idstm.setString(1, pNiveauAlias);
            ResultSet rs = idstm.executeQuery();
            while (rs.next()) {
                Etudiant etudiant = new Etudiant();
                etudiant.setIdUtilisateur(rs.getLong("i.idEtudiant"));
                etudiant.setCne(rs.getString("e.cne"));
                etudiant.setNom(rs.getString("u.nom"));
                etudiant.setPrenom(rs.getString("u.prenom"));
                studentsInfos.add(etudiant);
            }

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }
        return studentsInfos;
    }

    public boolean createNiveau(Niveau niveau) throws DataBaseException {
        String query = "INSERT INTO niveau ( alias, titre) VALUES ( ?, ?)";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, niveau.getAlias());
            statement.setString(2, niveau.getTitre());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        } catch (DataBaseException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean updateNiveau(Niveau niveau,long idNextNiveau) throws DataBaseException {
        String query = "UPDATE niveau SET alias = ?, titre = ?, idNextNiveau = ? WHERE idNiveau = ?";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, niveau.getAlias());
            statement.setString(2, niveau.getTitre());
            statement.setLong(3,idNextNiveau);
            statement.setLong(4, niveau.getIdNiveau());
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


    public boolean deleteNiveau(long niveauId) throws DataBaseException {
        String query = "DELETE FROM niveau WHERE idNiveau = ?";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, niveauId);
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

    public Niveau findNiveauById(long niveauId) throws DataBaseException {
        String query = "SELECT * FROM niveau WHERE idNiveau = ?";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, niveauId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                long idNiveau = resultSet.getLong("idNiveau");
                String alias = resultSet.getString("alias");
                String titre = resultSet.getString("titre");
                long idFiliere = resultSet.getLong("idFiliere");
                long idNextNiveau = resultSet.getLong("idNextNiveau");
                return new Niveau(idNiveau, alias, titre, new Filiere(idFiliere),idNextNiveau);
            }

            return null;
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        } catch (DataBaseException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        }
    }

    public boolean associateClassesToFiliere(long filiereId, List<Niveau> niveaux) throws DataBaseException {
        String query = "UPDATE niveau SET idFiliere = ? WHERE idNiveau = ?";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);

            for (Niveau niveau : niveaux) {
                statement.setLong(1, filiereId);
                statement.setLong(2, getNiveauIdByAlias(niveau.getAlias()));
                statement.addBatch();
            }

            int[] rowsUpdated = statement.executeBatch();
            for(Niveau i : niveaux){
                logger.info("La classe "+i.getAlias()+" a été ajouté à la filière "+filiereDao.findFiliere(filiereId).getTitreFiliere());
            }
            return rowsUpdated.length == niveaux.size();
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        } catch (DataBaseException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        }
    }

    public Long getNiveauIdByAlias(String alias) throws DataBaseException {
        String query = "SELECT idNiveau FROM niveau WHERE alias = ?";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, alias);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong("idNiveau");
            }

            return null;
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        } catch (DataBaseException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        }
    }

    public long getFiliereIDByAlias(String AliasNiveau) throws DataBaseException {
        String query = "SELECT idFiliere FROM niveau WHERE alias = ?";
        try {
            Connection connection = DBConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, AliasNiveau);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("idFiliere");
            }
        } catch (SQLException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        } catch (DataBaseException e) {
            logger.error("Erreur de ", e);
            throw new DataBaseException(e);
        }
        return 0;
    }

    public String getAliasByModuleTitre(String moduleTitre) throws DataBaseException {
        String alias = null;
        String sql = "SELECT n.alias FROM niveau n INNER JOIN module m ON m.idNiveau = n.idNiveau WHERE m.titre = ?";
        try {Connection c = DBConnection.getInstance();
            PreparedStatement statement = c.prepareStatement(sql); {
                statement.setString(1, moduleTitre);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        alias = resultSet.getString("alias");
                    }
                }}
        } catch (SQLException e) {
            throw new DataBaseException("Failed to retrieve alias from the database", e);
        }
        return alias;
    }

    private Niveau createNiveauFromResultSet(ResultSet resultSet) throws SQLException {
        Niveau niveau = new Niveau();
        niveau.setIdNiveau(resultSet.getLong("idNiveau"));
        niveau.setAlias(resultSet.getString("alias"));
        niveau.setTitre(resultSet.getString("titre"));
        return niveau;
    }
}





}


