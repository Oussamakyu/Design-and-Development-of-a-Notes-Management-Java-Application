package com.ensah.dao;




import com.ensah.bo.InscriptionMatiere;
import com.ensah.bo.InscriptionModule;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public InscriptionModule getInscriptionModule(long pIdInsc, String pModuleNom) throws DataBaseException{
        InscriptionModule inscriptionModule = new InscriptionModule();
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement stm = c.prepareStatement("SELECT i.* FROM inscriptionmodule i,module m WHERE i.idModule=m.idModule AND m.titre=? AND i.idInscription=?;");
            stm.setString(1, pModuleNom);
            stm.setLong(2, pIdInsc);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                inscriptionModule.setIdInscriptionModule(rs.getLong("idInscriptionModule"));
                inscriptionModule.setNoteFinale(rs.getDouble("noteFinale"));
                inscriptionModule.setNoteSN(rs.getDouble("noteSN"));
                inscriptionModule.setNoteSR(rs.getDouble("noteSR"));
                inscriptionModule.setPlusInfos(rs.getString("plusInfos"));
                inscriptionModule.setValidation(rs.getString("validation"));

            }

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }
        return inscriptionModule;
    }



}
