package com.ensah.dao;

import com.ensah.bo.Etudiant;
import com.ensah.bo.Niveau;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InscriptionAnnuelleDao {

    private Logger logger = Logger.getLogger(EtudiantDao.class);

    public void inscrire(Etudiant e , Niveau n) throws DataBaseException {
        Connection con = DBConnection.getInstance();
        String sqlInscrLevel = "INSERT INTO InscriptionAnnuelle(idEtudiant , idNiveau) VALUES (?,?)";

        try(PreparedStatement stmLvl = con.prepareStatement(sqlInscrLevel)){

            stmLvl.setLong(1,e.getIdUtilisateur());
            stmLvl.setLong(2,n.getIdNiveau());
            stmLvl.executeUpdate();
        }catch (SQLException ex) {
            logger.error("Erreur de ", ex);
            throw new DataBaseException(ex);
        }

        }
    }

