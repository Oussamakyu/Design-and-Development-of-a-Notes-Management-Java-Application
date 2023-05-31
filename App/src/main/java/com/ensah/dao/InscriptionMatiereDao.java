package com.ensah.dao;

import org.apache.log4j.Logger;
import com.ensah.bo.Element;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InscriptionMatiereDao {
    private Logger logger = Logger.getLogger(InscriptionModuleDao.class);
    public void ajouter(long pIdInscription, Element pMatiere) throws DataBaseException {
        try{
            Connection con = DBConnection.getInstance();
            String sqlInscrLevel = "INSERT INTO inscriptionmatiere(idInscription , idMatiere,coefficient, noteFinale, noteSN, noteSR) VALUES (?,?,?,0,0,0)";
            PreparedStatement stmLvl = con.prepareStatement(sqlInscrLevel);
            stmLvl.setLong(1,pIdInscription);
            stmLvl.setLong(2,pMatiere.getIdMatiere());
            stmLvl.setDouble(3,pMatiere.getCurrentCoefficient());
            stmLvl.executeUpdate();
        }catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }

    }
}
