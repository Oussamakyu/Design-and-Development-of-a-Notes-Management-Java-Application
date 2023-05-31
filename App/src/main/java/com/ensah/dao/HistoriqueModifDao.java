package com.ensah.dao;

import org.apache.log4j.Logger;
import com.ensah.bo.Etudiant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HistoriqueModifDao {
    private Logger logger = Logger.getLogger(HistoriqueModifDao.class);
    public void add(Etudiant pEtudiant) throws DataBaseException{
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement idstm = c.prepareStatement("INSERT INTO modifEtudiantHistori (nom,prenom,cne,idEtudiant) VALUES (?,?,?,?)");
            idstm.setString(1, pEtudiant.getNom());
            idstm.setString(2, pEtudiant.getPrenom());
            idstm.setString(3, pEtudiant.getCne());
            idstm.setLong(4, pEtudiant.getIdUtilisateur());
            idstm.executeUpdate();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }
    }
}
