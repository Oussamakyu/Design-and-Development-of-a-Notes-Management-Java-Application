package org.example.dao;

// @Author Rim Lfellous

import org.example.bo.Niveau;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class NiveauDao {
    private Logger logger = Logger.getLogger(this.getClass());

    public NiveauDao() {
    }

    public String getAliasByModuleTitre(String moduleTitre) throws DataBaseException {
        String alias = null;
        String sql = "SELECT n.alias FROM niveau n INNER JOIN module m ON m.idNiveau = n.idNiveau WHERE m.titre = ?";
        try {Connection c = DBConnection.getInstance();
            PreparedStatement statement = c.prepareStatement(sql); {
                statement.setString(1, moduleTitre);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        alias = resultSet.getString("alias");
                    }
                }}
        } catch (SQLException e) {
            throw new DataBaseException("Failed to retrieve alias from the database", e);
        }
        return alias;
    }

    private Niveau createNiveauFromResultSet(ResultSet resultSet) throws SQLException {
        Niveau niveau = new Niveau();
        niveau.setIdNiveau(resultSet.getLong("idNiveau"));
        niveau.setAlias(resultSet.getString("alias"));
        niveau.setTitre(resultSet.getString("titre"));
        return niveau;
    }
}

