package com.ensah.dao;

import com.ensah.bo.Etudiant;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RattDao {
    private Logger logger = Logger.getLogger(EtudiantDao.class);
    public void insertRattStudents(long idNiveau , List<Etudiant> a,long idModule) throws DataBaseException {
        Connection con = DBConnection.getInstance();
        String sql = " INSERT into `Ratt` (idNiveau,idEtudiant,idModule) VALUES (?,?,?) ;";
        try(PreparedStatement stm = con.prepareStatement(sql)){
            stm.setLong(1,idNiveau);
            stm.setLong(3,idModule);
            for ( Etudiant i : a){
                stm.setLong(2,i.getIdUtilisateur());
                stm.executeUpdate();
            }
            logger.info("Les étudiants sont inséré dans la table rattrapage avec succès.");
        }catch (SQLException ex) {
            logger.error("Erreur de ", ex);
            throw new DataBaseException(ex);
        }
    }

    public List<Long> getRattStudents(long idNiveau, long idModule) throws DataBaseException {
        List<Long> rattStudents = new ArrayList<>();
        Connection con = DBConnection.getInstance();
        String sql = "SELECT idEtudiant FROM `Ratt` WHERE idNiveau = ? AND idModule = ?";
        try (PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setLong(1, idNiveau);
            stm.setLong(2, idModule);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                long idEtudiant = rs.getLong("idEtudiant");
                rattStudents.add(idEtudiant);
            }
        } catch (SQLException ex) {
            logger.error("Erreur lors de la récupération des étudiants de rattrapage", ex);
            throw new DataBaseException(ex);
        }
        return rattStudents;
    }

}
