package org.example.dao;

// @Author Rim Lfellous

import org.example.bo.Utilisateur;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;


public class UtilisateurDao {
    private Logger logger = Logger.getLogger(this.getClass());

    public UtilisateurDao() {
    }







    public void AddUtilisateur(Utilisateur pUtilisateur) throws DataBaseException {
        try {
            Connection c = DBConnection.getInstance();
            String sqlInsert = "INSERT INTO utilisateurs (idUtilisateur, nom, prenom, cin, email, telephone, nom_arabe, prenom_arabe, photo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stm = c.prepareStatement(sqlInsert);
            stm.setLong(1, pUtilisateur.getIdUtilisateur());
            stm.setString(2, pUtilisateur.getNom());
            stm.setString(3, pUtilisateur.getPrenom());
            stm.setString(4, pUtilisateur.getCin());
            stm.setString(5, pUtilisateur.getEmail());
            stm.setString(6, pUtilisateur.getTelephone());
            stm.setString(7, pUtilisateur.getNomArabe());
            stm.setString(8, pUtilisateur.getPrenomArabe());
            stm.setString(9, pUtilisateur.getPhoto());
            stm.executeUpdate();
        } catch (SQLException var5) {
            this.logger.error("Erreur à cause de : ", var5);
            throw new DataBaseException(var5);
        }
    }



    public Utilisateur SearchById(Long ID) throws DataBaseException {
        List<Utilisateur> list = new ArrayList();

        try {
            Connection c = DBConnection.getInstance();
            String sqlInsert = "SELECT * FROM UTILISATEUR WHERE IdUtilisateur =?";
            PreparedStatement stm = c.prepareStatement(sqlInsert);
            stm.setLong(1, ID);
            ResultSet rs = stm.executeQuery();

            while(rs.next()) {
                list.add(this.resultToUtilisateur(rs));
            }

            rs.close();
            return list.isEmpty() ? null : (Utilisateur)list.get(0);
        } catch (SQLException var7) {
            this.logger.error("Erreur à cause de : ", var7);
            throw new DataBaseException(var7);
        }
    }

    public List<Utilisateur> FindAll() throws DataBaseException {
        List<Utilisateur> list = new ArrayList();

        try {
            Connection c = DBConnection.getInstance();
            String sqlInsert = "SELECT * FROM UTILISATEUR";
            PreparedStatement stm = c.prepareStatement(sqlInsert);
            ResultSet rs = stm.executeQuery();
            Utilisateur pUtilisateur = null;

            while(rs.next()) {
                list.add(this.resultToUtilisateur(rs));
            }

            rs.close();
            return list;
        } catch (SQLException var7) {
            this.logger.error("Erreur à cause de : ", var7);
            throw new DataBaseException(var7);
        }
    }

    public Utilisateur resultToUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUtilisateur(rs.getLong("IdUtilisateur"));
        utilisateur.setNom(rs.getString("Nom"));
        utilisateur.setPrenom(rs.getString("Prenom"));
        utilisateur.setCin(rs.getString("cin"));
        utilisateur.setTelephone(rs.getString("Telephone"));
        utilisateur.setNomArabe(rs.getString("NomArabe"));
        utilisateur.setPrenomArabe(rs.getString("PrenomArabe"));
        utilisateur.setEmail(rs.getString("Email"));
        utilisateur.setPhoto(rs.getString("Photo"));
        return utilisateur;
    }
}
