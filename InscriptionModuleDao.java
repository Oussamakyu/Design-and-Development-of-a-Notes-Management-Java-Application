package org.example.dao;

// @Author Rim Lfellous

import org.example.bo.Etudiant;
import org.example.bo.InscriptionModule;
import org.example.bo.Module;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class InscriptionModuleDao {
    private Logger logger = Logger.getLogger(this.getClass());

    public InscriptionModuleDao() {
    }

    public void update() throws DataBaseException, SQLException {
        InscriptionModule inscriptionModule = new InscriptionModule();

        try {
            Connection connection = DBConnection.getInstance();
            String sqlUpdate = "UPDATE InscriptionModule SET noteSN = ?, noteSR = ?, noteFinale = ?, validation = ?, plusInfos = ? WHERE idInscriptionModule = ?";
            PreparedStatement statement = connection.prepareStatement(sqlUpdate);
            statement.setLong(1, inscriptionModule.getIdInscriptionModule());
            statement.setDouble(2, inscriptionModule.getNoteSN());
            statement.setDouble(3, inscriptionModule.getNoteSR());
            statement.setDouble(4, inscriptionModule.getNoteFinale());
            statement.setString(5, inscriptionModule.getValidation());
            statement.setString(6, inscriptionModule.getPlusInfos());
            statement.executeUpdate();
        } catch (SQLException var5) {
            this.logger.error("Erreur lors de la mise à jour de l'inscription du module : ", var5);
            throw new DataBaseException(var5);
        }
    }

    public void inscriptionEtudiantModule(Etudiant etudiant, Module module) throws DataBaseException {
        try {
            Connection connection = DBConnection.getInstance();
            String sqlInsertModule = "INSERT INTO Module (code, titre, idNiveau) VALUES (?, ?, ?)";
            PreparedStatement statementModule = connection.prepareStatement(sqlInsertModule, 1);
            statementModule.setString(1, module.getCode());
            statementModule.setString(2, module.getTitre());
            statementModule.setLong(3, module.getNiveau().getIdNiveau());
            statementModule.executeUpdate();
            ResultSet generatedModuleKeys = statementModule.getGeneratedKeys();
            Long generatedModuleId = -1L;
            if (generatedModuleKeys.next()) {
                generatedModuleId = generatedModuleKeys.getLong(1);
                module.setIdModule(generatedModuleId);
            }

            statementModule.close();
            String sqlInsertInscription = "INSERT INTO Inscription (idEtudiant, idModule) VALUES (?, ?)";
            PreparedStatement statementInscription = connection.prepareStatement(sqlInsertInscription);
            statementInscription.setLong(1, etudiant.getIdUtilisateur());
            statementInscription.setLong(2, module.getIdModule());
            statementInscription.executeUpdate();
            statementInscription.close();
            connection.close();
        } catch (SQLException var10) {
            this.logger.error("Erreur lors de la mise à jour de l'inscription du module : ", var10);
            throw new DataBaseException(var10);
        }
    }
}
