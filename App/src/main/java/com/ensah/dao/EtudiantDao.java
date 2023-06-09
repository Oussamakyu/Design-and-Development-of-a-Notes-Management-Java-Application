package com.ensah.dao;
import com.ensah.bo.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;



import org.apache.log4j.Logger;



public class EtudiantDao {
    private Logger logger = Logger.getLogger(EtudiantDao.class);

    public static String convertToArabicName(String name) {
        // Normalize the name by removing diacritical marks
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);

        // Replace non-Arabic characters with their Arabic equivalents
        String converted = normalized.replaceAll("[^\\p{InArabic}]+", "");

        return converted;
    }
    public Etudiant findById(long pIdEtudiant) throws DataBaseException {
        Etudiant etudiant = null;
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement idstm = c.prepareStatement("SELECT u.idUtilisateur,e.dateNaissance,e.cne,u.nom,u.prenom FROM etudiant e, utilisateur u WHERE e.idEtudiant=u.idUtilisateur AND e.idEtudiant=?");
            idstm.setLong(1, pIdEtudiant);
            ResultSet rs = idstm.executeQuery();
            if (rs.next()) {
                etudiant = new Etudiant();
                etudiant.setIdUtilisateur(rs.getLong("u.idUtilisateur"));
                etudiant.setCne(rs.getString("e.cne"));
                etudiant.setDateNaissance(rs.getDate("e.dateNaissance"));
                etudiant.setNom(rs.getString("u.nom"));
                etudiant.setPrenom(rs.getString("u.prenom"));
            }

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DataBaseException(ex);
        }
        return etudiant;
    }

    public void inscrire(Etudiant e) throws DataBaseException {
        try {
            Connection con = DBConnection.getInstance();
            String sqlInsertEtudiant = "INSERT INTO Etudiant (cne, dateNaissance, idEtudiant) VALUES (?, ?, ?);";
            PreparedStatement stmEtudiant = con.prepareStatement(sqlInsertEtudiant);

            // Insert statement for Etudiant table
            stmEtudiant.setString(1, e.getCne());
            stmEtudiant.setDate(2, e.getDateNaissance());
            stmEtudiant.setLong(3, e.getIdUtilisateur());
            stmEtudiant.executeUpdate();


            logger.info("l'inscription de l'étudiant  " + e.getNom() + " a été effectué avec succès ");

        } catch (SQLException ex) {
            logger.error("Erreur de ", ex);
            throw new DataBaseException(ex);
        }
    }


    public void reInscrire(Etudiant e) throws DataBaseException {
        Connection con = DBConnection.getInstance();
        String sqlUpdateUtilisateur = "UPDATE Utilisateur SET nom = ?, prenom = ?, cin = ?, email = ?, telephone = ?, nomArabe = ?, prenomArabe = ?, photo = ? WHERE idUtilisateur = ?;";
        String sqlUpdateEtudiant = "UPDATE Etudiant SET cne = ?, dateNaissance = ? WHERE idEtudiant = ?;";

        try (PreparedStatement stmUtilisateur = con.prepareStatement(sqlUpdateUtilisateur);
             PreparedStatement stmEtudiant = con.prepareStatement(sqlUpdateEtudiant)) {

            stmUtilisateur.setString(1, e.getNom());
            stmUtilisateur.setString(2, e.getPrenom());
            stmUtilisateur.setString(3, e.getCin());
            stmUtilisateur.setString(4, e.getEmail());
            stmUtilisateur.setString(5, e.getTelephone());
            stmUtilisateur.setString(6, convertToArabicName(e.getNom()));
            stmUtilisateur.setString(7, convertToArabicName(e.getPrenom()));
            stmUtilisateur.setString(8, null); // Assuming photo is of type VARCHAR(255) in the Utilisateur table
            stmUtilisateur.setLong(9, e.getIdUtilisateur());
            stmUtilisateur.executeUpdate();

            stmEtudiant.setString(1, e.getCne());
            stmEtudiant.setDate(2, (java.sql.Date) e.getDateNaissance());
            stmEtudiant.setLong(3, e.getIdUtilisateur());
            stmEtudiant.executeUpdate();
            logger.info("la réinscription de l'étudiant  " + e.getNom() + " a été effectué avec succès ");

        } catch (SQLException ex) {
            logger.error("Erreur de ", ex);
            throw new DataBaseException(ex);
        }
    }

    public boolean etuExist(Etudiant e) throws DataBaseException {
        Connection con = DBConnection.getInstance();
        String sqlSelect = "SELECT * from Etudiant where IdEtudiant = ?;";
        Boolean exist = false;
        try (PreparedStatement stm = con.prepareStatement(sqlSelect)) {
            stm.setLong(1, e.getIdUtilisateur());
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next())
                exist = true;
        } catch (SQLException ex) {
            logger.error("Erreur de ", ex);
            throw new DataBaseException(ex);
        }
        return exist;

    }

    public void mofidy(Etudiant pEtudiant) throws DataBaseException{
        try {
            Connection con = DBConnection.getInstance();
            String sqlInsertEtudiant = "UPDATE Etudiant SET cne=?, dateNaissance=? WHERE idEtudiant=?;";
            PreparedStatement stmEtudiant = con.prepareStatement(sqlInsertEtudiant);

            // Insert statement for Etudiant table
            stmEtudiant.setString(1, pEtudiant.getCne());
            stmEtudiant.setDate(2, pEtudiant.getDateNaissance());
            stmEtudiant.setLong(3, pEtudiant.getIdUtilisateur());
            stmEtudiant.executeUpdate();


            logger.info("l'inscription de l'étudiant  " + pEtudiant.getNom() + " a été effectué avec succès ");

        } catch (SQLException ex) {
            logger.error("Erreur de ", ex);
            throw new DataBaseException(ex);
        }

    }

    public boolean checkLevel(Long newLevel, Etudiant e) throws DataBaseException {
        Connection con = DBConnection.getInstance();
        //à compléter plus tard
//        --
//                ALTER TABLE `inscriptionannuelle`
//        ADD PRIMARY KEY (`idInscription`),
//                ADD KEY `FKge2xwqtfeqnojw9no8og6vbqn` (`idEtudiant`),
//        ADD KEY `FK9lrdmhkam481adiwotdpqo8w` (`idNiveau`);
        return true;
    }

    public void modifyInfos(String CNE,String newCne, String nom, String prenom) throws DataBaseException {
        Connection con = DBConnection.getInstance();
        String query = "UPDATE Etudiant SET cne = ? WHERE idEtudiant = ?;";
        String query2 = "UPDATE Utilisateur SET nom = ?, prenom = ? WHERE idEtudiant = ?;";
        String query3 = "SELECT idEtudiant FROM Etudiant WHERE cne = ?;";

        try (PreparedStatement stmtUpdateEtudiant = con.prepareStatement(query);
             PreparedStatement stmtUpdateUtilisateur = con.prepareStatement(query2);
             PreparedStatement stmtSelectIdEtudiant = con.prepareStatement(query3)) {

            stmtSelectIdEtudiant.setString(1, newCne);
            ResultSet rs = stmtSelectIdEtudiant.executeQuery();
            Long idEtudiant = null;
            if (rs.next()) {
                idEtudiant = rs.getLong(1);

                stmtUpdateEtudiant.setString(1, CNE);
                stmtUpdateEtudiant.setLong(2, idEtudiant);
                stmtUpdateEtudiant.executeUpdate();

                stmtUpdateUtilisateur.setString(1, nom);
                stmtUpdateUtilisateur.setString(2, prenom);
                stmtUpdateUtilisateur.setLong(3, idEtudiant);
                stmtUpdateUtilisateur.executeUpdate();
            }
            logger.info("L'etudiant ayant " + idEtudiant + " comme id a modifié ses informations .");
        } catch (SQLException ex) {
            logger.error("Erreur de ", ex);
            throw new DataBaseException(ex);
        }
    }

    public void modifyLevel(Etudiant e, Niveau n) throws DataBaseException {
        Connection con = DBConnection.getInstance();
        String sql = "UPDATE set "; //à compléter plus tard
    }


    public Etudiant searchStudent(String cne) throws DataBaseException {
        Connection con = DBConnection.getInstance();
        String query = "SELECT * FROM Etudiant WHERE cne = ?;";
        String query2 = "SELECT idEtudiant FROM Etudiant WHERE cne = ?;";
        String query3 = "SELECT * FROM Utilisateur WHERE idUtilisateur = ?;";

        try (PreparedStatement stmtQuery = con.prepareStatement(query);
             PreparedStatement stmtQuery2 = con.prepareStatement(query2);
             PreparedStatement stmtQuery3 = con.prepareStatement(query3)) {

            stmtQuery.setString(1, cne);
            ResultSet rs = stmtQuery.executeQuery();

            if (rs.next()) {
                Etudiant etudiant = new Etudiant();
                etudiant.setCne(rs.getString("cne"));
                etudiant.setDateNaissance(rs.getDate("dateNaissance"));
                // Populate other student information as needed

                stmtQuery2.setString(1, cne);
                ResultSet rsIdEtudiant = stmtQuery2.executeQuery();

                if (rsIdEtudiant.next()) {
                    long idEtudiant = rsIdEtudiant.getLong("idEtudiant");
                    etudiant.setIdUtilisateur(idEtudiant);
                    stmtQuery3.setLong(1, idEtudiant);
                    ResultSet rsUtilisateur = stmtQuery3.executeQuery();

                    if (rsUtilisateur.next()) {
                        etudiant.setNom(rsUtilisateur.getString("nom"));
                        etudiant.setPrenom(rsUtilisateur.getString("prenom"));
                    }
                }

                return etudiant;
            }

        } catch (SQLException ex) {
            logger.error("Erreur de ", ex);
            throw new DataBaseException(ex);
        }

        return null;
    }


    public List<Etudiant> getStudentInfoByModuleTitre(String moduleTitre) throws DataBaseException {
        List<Etudiant> studentInfoList = new ArrayList<>();
        String sql = "SELECT e.idEtudiant, e.cne, u.nom, u.prenom " +
                "FROM etudiant e " +
                "JOIN inscriptionannuelle ia ON e.idEtudiant = ia.idEtudiant " +
                "JOIN inscriptionmodule im ON ia.idInscription = im.idInscription " +
                "JOIN module m ON im.idModule = m.idModule " +
                "JOIN utilisateur u ON e.idEtudiant = u.idUtilisateur " +
                "WHERE m.Titre = ?";

        try (Connection c = DBConnection.getInstance();
             PreparedStatement statement = c.prepareStatement(sql)) {
            statement.setString(1, moduleTitre);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    long id = resultSet.getInt("idEtudiant");
                    String cne = resultSet.getString("cne");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    Etudiant studentInfo = new Etudiant(id, cne, nom, prenom);
                    studentInfoList.add(studentInfo);
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException("Failed to retrieve student information from the database", e);
        }
        return studentInfoList;
    }

    public List<Etudiant> getStudentsByRatt(String moduleTitre) throws DataBaseException {
        List<Etudiant> studentInfoList = new ArrayList<>();

        String sql = "SELECT e.idEtudiant, e.cne, u.nom, u.prenom " +
                "FROM etudiant e " +
                "JOIN inscriptionannuelle ia ON e.idEtudiant = ia.idEtudiant " +
                "JOIN inscriptionmodule im ON ia.idInscription = im.idInscription " +
                "JOIN module m ON im.idModule = m.idModule " +
                "JOIN utilisateur u ON e.idEtudiant = u.idUtilisateur " +
                "WHERE m.Titre = ? AND im.Validation= 'R' ";

        try (Connection c = DBConnection.getInstance();
             PreparedStatement statement = c.prepareStatement(sql)) {
            statement.setString(1, moduleTitre);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    long id = resultSet.getInt("idEtudiant");
                    String cne = resultSet.getString("cne");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    Etudiant studentInfo = new Etudiant(id, cne, nom, prenom);

                    studentInfoList.add(studentInfo);
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException("Failed to retrieve student information from the database", e);
        }
        return studentInfoList;
    }




//à Ajouter : afficher les inscriptions annuelles d'un étudiant

//    public List<String> dileb() throws DataBaseException{
//
//    }
}








