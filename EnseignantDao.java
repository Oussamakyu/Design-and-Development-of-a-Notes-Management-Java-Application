package org.example.dao;

// @Author Rim Lfellous

import org.example.bo.Enseignant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class EnseignantDao extends UtilisateurDao {
    private Logger logger = Logger.getLogger(this.getClass());

    public EnseignantDao() {
    }

    public void AddEnseignant(Enseignant pEnseignat) throws DataBaseException {
        try {
            Connection c = DBConnection.getInstance();
            String sqlInsert = "INSERT INTO ENSEIGNANT VALUES (?)";
            PreparedStatement stm = c.prepareStatement(sqlInsert);
            stm.setString(1, pEnseignat.getSpecialite());
            stm.executeUpdate();
            this.AddUtilisateur(pEnseignat);
        } catch (SQLException var6) {
            this.logger.error("Erreur à cause de :", var6);
            throw new DataBaseException(var6);
        }
    }


    }

