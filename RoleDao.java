package org.example.dao;

import org.apache.log4j.Logger;
import org.example.bo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// @Author Rim Lfellous

public class RoleDao {
    private Logger logger = Logger.getLogger(this.getClass());

    public Role findRoleById(long roleId) throws DataBaseException {
        Role role = null;

        try {
            Connection connection = DBConnection.getInstance();
            String sqlQuery = "SELECT * FROM role WHERE idRole = ?";
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setLong(1, roleId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                role = new Role();
                role.setIdRole(resultSet.getLong("idRole"));
                role.setNomRole(resultSet.getString("nomRole"));
            }

            resultSet.close();
            statement.close();
            return role;
        } catch (SQLException var8) {
            this.logger.error("Erreur lors de la recherche du rôle par ID : ", var8);
            throw new DataBaseException(var8);
        }
    }

}
