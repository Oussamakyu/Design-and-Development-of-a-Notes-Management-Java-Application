package com.ensah.dao;

import org.apache.log4j.Logger;
import com.ensah.bo.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilisateurDao {
    private Logger logger = Logger.getLogger(UtilisateurDao.class);

    public Utilisateur findById(long pIdUtilisateur) throws DataBaseException{
        Utilisateur utilisateur = null;
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement idstm = c.prepareStatement("SELECT * FROM utilisateur WHERE idUtilisateur=?");
            idstm.setLong(1, pIdUtilisateur);
            ResultSet rs = idstm.executeQuery();
            if (rs.next()) {
                utilisateur = new Utilisateur(rs.getLong(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9));
            }

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }
        return utilisateur;
    }
    public void add(Utilisateur newUtilisateur) throws DataBaseException{
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement idstm = c.prepareStatement("INSERT INTO utilisateur VALUES(?,?,?,?,?,?,?,?,?)");
            idstm.setLong(1, newUtilisateur.getIdUtilisateur());
            idstm.setString(2, newUtilisateur.getCin());
            idstm.setString(3, newUtilisateur.getEmail());
            idstm.setString(4, newUtilisateur.getNom());
            idstm.setString(5, newUtilisateur.getNomArabe());
            idstm.setString(6, newUtilisateur.getPhoto());
            idstm.setString(7, newUtilisateur.getPrenom());
            idstm.setString(8, newUtilisateur.getPrenomArabe());
            idstm.setString(9, newUtilisateur.getTelephone());
            idstm.executeUpdate();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }

    }

    public void modify(Utilisateur pUtilisateur) throws DataBaseException{
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement idstm = c.prepareStatement("UPDATE utilisateur SET cin=?, email=?, nom=?, nomArabe=?, photo=?, prenom=?, prenomArabe=?, telephone=? WHERE idUtilisateur=?");
            idstm.setString(1, pUtilisateur.getCin());
            idstm.setString(2, pUtilisateur.getEmail());
            idstm.setString(3, pUtilisateur.getNom());
            idstm.setString(4, pUtilisateur.getNomArabe());
            idstm.setString(5, pUtilisateur.getPhoto());
            idstm.setString(6, pUtilisateur.getPrenom());
            idstm.setString(7, pUtilisateur.getPrenomArabe());
            idstm.setString(8, pUtilisateur.getTelephone());
            idstm.setLong(9, pUtilisateur.getIdUtilisateur());
            idstm.executeUpdate();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }


    }
}
