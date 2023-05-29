package com.ensah.dao;

import org.apache.log4j.Logger;
import com.ensah.bo.Niveau;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InscriptionModuleDao {
    private Logger logger = Logger.getLogger(EtudiantDao.class);

    public void inscrire(Niveau n) throws DataBaseException {
        Connection con = DBConnection.getInstance();
        String sqlInscrMod = "INSERT INTO InscriptionModule(idInscription, idModule) VALUES (?, ?);";
        String sqlModule = "SELECT * FROM Module WHERE idNiveau = ?;";
        String sqlInscription = "SELECT idInscription FROM inscriptionannuelle WHERE idNiveau = ?;";

        try (PreparedStatement stmModule = con.prepareStatement(sqlModule);
             PreparedStatement stmInscription = con.prepareStatement(sqlInscription);
             PreparedStatement stmInscrMod = con.prepareStatement(sqlInscrMod)) {

            stmModule.setLong(1, n.getIdNiveau());
            ResultSet rsModule = stmModule.executeQuery();

            while (rsModule.next()) {
                long idModule = rsModule.getLong("idModule");

                stmInscription.setLong(1, n.getIdNiveau());
                ResultSet rsInscription = stmInscription.executeQuery();


                long idInscription = rsInscription.getLong("idInscription");

                stmInscrMod.setLong(1, idInscription);
                stmInscrMod.setLong(2, idModule);
                stmInscrMod.executeUpdate();

            }
        } catch (SQLException ex) {
            logger.error("Erreur de ", ex);
            throw new DataBaseException(ex);
        }
    }


}
