package com.ensah.dao;

import org.apache.log4j.Logger;
import com.ensah.bo.Element;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModuleDao {

    private Logger logger = Logger.getLogger(ModuleDao.class);
    public List<Element> getElements(long pModuleId) throws DataBaseException{
        List<Element> modulesId = new ArrayList<>();
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement idstm = c.prepareStatement("SELECT idMatiere,currentCoefficient FROM element WHERE idModule=?");
            idstm.setLong(1, pModuleId);
            ResultSet rs = idstm.executeQuery();
            while (rs.next()) {
                Element element=new Element();
                element.setIdMatiere(rs.getLong("idMatiere"));
                element.setCurrentCoefficient(rs.getLong("currentCoefficient"));
                modulesId.add(element);
            }

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }

        return modulesId;
    }
}
