package com.ensah.dao;

import com.ensah.bo.Etudiant;
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
    public Niveau getNiveau(long pNiveauId) throws DataBaseException{
        Niveau niveau = null;
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement idstm = c.prepareStatement("SELECT * FROM niveau WHERE idNiveau=?");
            idstm.setLong(1, pNiveauId);
            ResultSet rs = idstm.executeQuery();
            if (rs.next()) {
                niveau = new Niveau(rs.getLong(1),rs.getString(2),rs.getString(3));
            }

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }
        return niveau;
    }

    public long getNextNiveau(long pNiveauId) throws DataBaseException{
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

    public List<Module> getModules(long pNiveauId) throws DataBaseException{
        List<Module> modulesId = new ArrayList<>();
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement idstm = c.prepareStatement("SELECT idModule FROM module WHERE idNiveau=?");
            idstm.setLong(1, pNiveauId);
            ResultSet rs = idstm.executeQuery();
            while (rs.next()) {
                Module module=new Module();
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


}
