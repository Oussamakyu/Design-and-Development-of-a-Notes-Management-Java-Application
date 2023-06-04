package com.ensah.dao;

import com.ensah.bo.InscriptionMatiere;
import org.apache.log4j.Logger;
import com.ensah.bo.Element;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InscriptionMatiereDao {
    private Logger logger = Logger.getLogger(InscriptionModuleDao.class);

    public void ajouter(long pIdInscription, Element pMatiere) throws DataBaseException {
        try {
            Connection con = DBConnection.getInstance();
            String sqlInscrLevel = "INSERT INTO inscriptionmatiere(idInscription , idMatiere,coefficient, noteFinale, noteSN, noteSR) VALUES (?,?,?,0,0,0)";
            PreparedStatement stmLvl = con.prepareStatement(sqlInscrLevel);
            stmLvl.setLong(1, pIdInscription);
            stmLvl.setLong(2, pMatiere.getIdMatiere());
            stmLvl.setDouble(3, pMatiere.getCurrentCoefficient());
            stmLvl.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }

    }

    public InscriptionMatiere getInscriptionMatiere(long pIdInsc, String pElementNom) throws DataBaseException {
        InscriptionMatiere inscriptionMatiere = new InscriptionMatiere();
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement stm = c.prepareStatement("SELECT i.* FROM inscriptionmatiere i,element e WHERE i.idMatiere=e.idMatiere AND e.nom=? AND i.idInscription=?;");
            stm.setString(1, pElementNom);
            stm.setLong(2, pIdInsc);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                inscriptionMatiere.setIdInscriptionMatiere(rs.getLong("idInscriptionMatiere"));
                inscriptionMatiere.setCoefficient(rs.getDouble("coefficient"));
                inscriptionMatiere.setNoteFinale(rs.getDouble("noteFinale"));
                inscriptionMatiere.setNoteSN(rs.getDouble("noteSN"));
                inscriptionMatiere.setNoteSR(rs.getDouble("noteSR"));
                inscriptionMatiere.setPlusInfos(rs.getString("plusInfos"));
                inscriptionMatiere.setValidation(rs.getString("validation"));

            }

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }
        return inscriptionMatiere;
    }
}
