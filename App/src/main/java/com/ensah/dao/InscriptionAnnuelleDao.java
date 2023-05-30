package com.ensah.dao;

import com.ensah.bo.Etudiant;
import com.ensah.bo.Niveau;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InscriptionAnnuelleDao {

    private Logger logger = Logger.getLogger(EtudiantDao.class);

    public void inscrire(Etudiant e, Niveau n) throws DataBaseException {
        Connection con = DBConnection.getInstance();
        String sqlInscrLevel = "INSERT INTO InscriptionAnnuelle(idEtudiant , idNiveau) VALUES (?,?)";

        try (PreparedStatement stmLvl = con.prepareStatement(sqlInscrLevel)) {

            stmLvl.setLong(1, e.getIdUtilisateur());
            stmLvl.setLong(2, n.getIdNiveau());
            stmLvl.executeUpdate();
        } catch (SQLException ex) {
            logger.error("Erreur de ", ex);
            throw new DataBaseException(ex);
        }

    }


    public boolean validationModule(Etudiant e,long moy) throws DataBaseException {
        Connection con = DBConnection.getInstance();
        String sql = " Select idNiveau from InscriptionAnnuelle where idEtudiant = ?;";
        try(PreparedStatement stm = con.prepareStatement(sql)){
            stm.setLong(1,e.getIdUtilisateur());
            long x = stm.executeUpdate();
            if(x == 1 || x== 2){
                if(moy>=10)
                    return true;
                return false;
            } else {
                if(moy>=12)
                    return true;
                return false;
            }
        }catch (SQLException ex) {
            logger.error("Erreur de ", ex);
            throw new DataBaseException(ex);
        }
    }



}