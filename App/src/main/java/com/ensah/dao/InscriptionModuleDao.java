package com.ensah.dao;




import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class InscriptionModuleDao {
    private Logger logger = Logger.getLogger(InscriptionModuleDao.class);
    public void ajouter(long pIdInscription,long pIdModule) throws DataBaseException {
        try{
            Connection con = DBConnection.getInstance();
            String sqlInscrLevel = "INSERT INTO inscriptionmodule(idInscription , idModule, noteFinale, noteSN, noteSR) VALUES (?,?,0,0,0)";
            PreparedStatement stmLvl = con.prepareStatement(sqlInscrLevel);
            stmLvl.setLong(1,pIdInscription);
            stmLvl.setLong(2,pIdModule);
            stmLvl.executeUpdate();
        }catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }

    }
}
