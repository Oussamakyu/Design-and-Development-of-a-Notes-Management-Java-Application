package org.example.dao;



import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
public class DBConnection {
    private Logger logger = Logger.getLogger(getClass());
    private static String dbUrl;
    private static String login;
    private static String password;
    private static String driver;

    /** pour y stocker l'objet de connexion */
    private static Connection connection;

    /**
     * Constructor
     *
     * @throws DataBaseException
     */
    DBConnection() throws DataBaseException {
        try {
            // Lire le fichier de configuration conf.propeties
            Properties dbProperties = DbPropertiesLoader.loadPoperties("conf.properties");
            dbUrl = dbProperties.getProperty("db.url");
            login = dbProperties.getProperty("db.login");
            password = dbProperties.getProperty("db.password");
            driver = dbProperties.getProperty("db.driver");

            // charger le pilote
            Class.forName(driver);

            // Créer une connexion à la base de données
            connection = DriverManager.getConnection(dbUrl, login, password);

            // Debug statement
            System.out.println("Database connection established successfully.");

        } catch (Exception ex) {
            // tracer cette erreur
            logger.error(ex);
            // raise the exception stack
            throw new DataBaseException(ex);
        }
    }

    /**
     * returns the unique instance of connection
     *
     * @return connection
     * @throws DataBaseException
     */
    public static Connection getInstance() throws DataBaseException {

        if (connection == null) {

            new DBConnection();

        }

        return connection;
    }

}


