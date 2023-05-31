package com.ensah.dao;

import org.apache.log4j.Logger;
import com.ensah.bo.Etudiant;
import com.ensah.bo.InscriptionAnnuelle;
import com.ensah.bo.Niveau;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class InscriptionAnnuelleDao {

    private Logger logger = Logger.getLogger(InscriptionAnnuelleDao.class);

    public long ajouter(Etudiant pEtudiant , Niveau n,String pType) throws DataBaseException {
        long idLastInscAnn = -1;
        try{
            Connection con = DBConnection.getInstance();
            String sqlInscrLevel = "INSERT INTO InscriptionAnnuelle(idEtudiant , idNiveau, annee , etat, rang, type) VALUES (?,?,?,0,0,?)";
            PreparedStatement stmLvl = con.prepareStatement(sqlInscrLevel);
            stmLvl.setLong(1,pEtudiant.getIdUtilisateur());
            stmLvl.setLong(2,n.getIdNiveau());
            stmLvl.setInt(3, Year.now().getValue());
            stmLvl.setString(4, pType);
            stmLvl.executeUpdate();

            String sqlLastInscId = "SELECT idInscription FROM InscriptionAnnuelle ORDER BY idInscription DESC LIMIT 1";
            PreparedStatement stmId = con.prepareStatement(sqlLastInscId);
            ResultSet rs = stmId.executeQuery();
            if(rs.next()){
                idLastInscAnn = rs.getInt(1);
            }
        }catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }
        return idLastInscAnn;

    }

    public List<InscriptionAnnuelle> getAllInscriptionAnn(Etudiant pEtudiant) throws DataBaseException{
        List<InscriptionAnnuelle> inscriptionAnnList =new ArrayList<>();
        try{
            Connection c = DBConnection.getInstance();
            PreparedStatement stm = c.prepareStatement("SELECT * FROM inscriptionannuelle WHERE idEtudiant=? ORDER BY idInscription DESC");
            stm.setLong(1,pEtudiant.getIdUtilisateur());
            ResultSet rs = stm.executeQuery();

            while(rs.next()){
                InscriptionAnnuelle inscriptionAnn = new InscriptionAnnuelle();
                inscriptionAnn.setIdInscription(rs.getLong(1));
                inscriptionAnn.setAnnee(rs.getInt(2));
                inscriptionAnn.setEtat(rs.getInt(3));
                inscriptionAnn.setMention(rs.getString(4));
                inscriptionAnn.setPlusInfos(rs.getString(5));
                inscriptionAnn.setRang(rs.getInt(6));
                inscriptionAnn.setType(rs.getString(7));
                inscriptionAnn.setValidation(rs.getString(8));
                inscriptionAnn.setEtudiant(pEtudiant);
                inscriptionAnn.setNiveau(new Niveau(rs.getLong(10)));

                inscriptionAnnList.add(inscriptionAnn);

            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DataBaseException(e);
        }
        return inscriptionAnnList;
    }


    public boolean validationModule(Etudiant e,long moy) throws DataBaseException {
        Connection con = DBConnection.getInstance();
        String sql = " Select idNiveau from InscriptionAnnuelle where idEtudiant = ?;";
        try(PreparedStatement stm = con.prepareStatement(sql)){
            stm.setLong(1,e.getIdUtilisateur());
            long x = stm.executeUpdate();
            if(x == 1 || x== 2){
                if(moy>=10)
                    return true;
                return false;
            } else {
                if(moy>=12)
                    return true;
                return false;
            }
        }catch (SQLException ex) {
            logger.error("Erreur de ", ex);
            throw new DataBaseException(ex);
        }
    }



}