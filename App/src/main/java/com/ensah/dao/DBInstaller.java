package com.ensah.dao;

import com.ensah.utils.FileManager;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.sql.*;
import java.util.Properties;


public class DBInstaller {

    private static Logger LOGGER = Logger.getLogger(DBInstaller.class);

    public static boolean installDataBase() throws DataBaseException, IOException {
        Properties dbProperties = DbPropertiesLoader.loadProperties("conf.properties");
        String url = dbProperties.getProperty("db.url");
        String username = dbProperties.getProperty("db.login");
        String password = dbProperties.getProperty("db.password");
        String databaseName = dbProperties.getProperty("db.name");

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            String sql = "CREATE DATABASE " + databaseName;
            statement.executeUpdate(sql);
            System.out.println("Database created successfully");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void createDataBaseTables() throws DataBaseException {

        try {
            Connection con = DBConnection.getInstance();


            String sql = """
                                -- phpMyAdmin SQL Dump
                                                           -- version 5.2.0
                                                           -- https://www.phpmyadmin.net/
                                                           --
                                                           -- Hôte : 127.0.0.1
                                                           -- Généré le : sam. 13 mai 2023 à 00:57
                                                           -- Version du serveur : 10.4.27-MariaDB
                                                           -- Version de PHP : 8.0.25
                                                           
                                                           SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
                                                           START TRANSACTION;
                                                           SET time_zone = "+00:00";
                                                           
                                                           
                                                           /*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
                                                           /*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
                                                           /*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
                                                           /*!40101 SET NAMES utf8mb4 */;
                                                           
                                                           --
                                                           -- Base de données : `gs_notes_database`
                                                           --
                                                           
                                                           -- --------------------------------------------------------
                                                           
                                                           --
                                                           -- Structure de la table `cadreadministrateur`
                                                           --
                                                           
                                                           CREATE TABLE `cadreadministrateur` (
                                                             `grade` varchar(255) DEFAULT NULL,
                                                             `idCardreAdmin` bigint(20) NOT NULL
                                                           ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
                                                           
                                                           --
                                                           -- Déchargement des données de la table `cadreadministrateur`
                                                           --
                                                           
                                                           INSERT INTO `cadreadministrateur` (`grade`, `idCardreAdmin`) VALUES
                                                           ('gradeeee', 8),
                                                           ('Admin 2eme grade', 12);
                                                           
                                                           -- --------------------------------------------------------
                                                           
                                                           --
                                                           -- Structure de la table `compte`
                                                           --
                                                           
                                                           CREATE TABLE `compte` (
                                                             `idCompte` bigint(20) NOT NULL,
                                                             `accepteDemandeAutorisationAbsence` bit(1) NOT NULL,
                                                             `accountNonExpired` bit(1) NOT NULL,
                                                             `accountNonLocked` bit(1) NOT NULL,
                                                             `afficheEmail` bit(1) NOT NULL,
                                                             `affichePhoto` bit(1) NOT NULL,
                                                             `credentialsNonExpired` bit(1) NOT NULL,
                                                             `disconnectAccount` bit(1) NOT NULL,
                                                             `enabled` bit(1) NOT NULL,
                                                             `login` varchar(255) DEFAULT NULL,
                                                             `password` varchar(255) DEFAULT NULL,
                                                             `idUtilisateur` bigint(20) DEFAULT NULL,
                                                             `idRole` bigint(20) DEFAULT NULL
                                                           ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
                                                           
                                                           --
                                                           -- Déchargement des données de la table `compte`
                                                           --
                                                           
                                                           INSERT INTO `compte` (`idCompte`, `accepteDemandeAutorisationAbsence`, `accountNonExpired`, `accountNonLocked`, `afficheEmail`, `affichePhoto`, `credentialsNonExpired`, `disconnectAccount`, `enabled`, `login`, `password`, `idUtilisateur`, `idRole`) VALUES
                                                           (8, b'0', b'1', b'1', b'0', b'0', b'1', b'0', b'1', 'admin', 'dsqdsqdsqd', 8, 1),
                                                           (9, b'0', b'1', b'1', b'0', b'0', b'1', b'0', b'1', 'prof', 'sdqsd', 9, 2),
                                                           (12, b'0', b'1', b'1', b'0', b'0', b'1', b'0', b'1', 'student', 'dsqd', 11, 4),
                                                           (13, b'0', b'1', b'1', b'0', b'0', b'1', b'0', b'1', 'cadre_admin', 'dsqd', 11, 3),
                                                           (14, b'0', b'1', b'1', b'0', b'0', b'1', b'0', b'1', 'biblio', 'dsqdsq', 12, 5);
                                                           
                                                           -- --------------------------------------------------------
                                                           
                                                           --
                                                           -- Structure de la table `element`
                                                           --
                                                           
                                                           CREATE TABLE `element` (
                                                             `idMatiere` bigint(20) NOT NULL,
                                                             `code` varchar(255) DEFAULT NULL,
                                                             `currentCoefficient` double NOT NULL,
                                                             `nom` varchar(255) DEFAULT NULL,
                                                             `idModule` bigint(20) DEFAULT NULL
                                                           ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
                                                           
                                                           -- --------------------------------------------------------
                                                           
                                                           --
                                                           -- Structure de la table `enseignant`
                                                           --
                                                           
                                                           CREATE TABLE `enseignant` (
                                                             `specialite` varchar(255) DEFAULT NULL,
                                                             `idEnseighant` bigint(20) NOT NULL
                                                           ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
                                                           
                                                           --
                                                           -- Déchargement des données de la table `enseignant`
                                                           --
                                                           
                                                           INSERT INTO `enseignant` (`specialite`, `idEnseighant`) VALUES
                                                           ('math', 10);
                                                           
                                                           -- --------------------------------------------------------
                                                           
                                                           --
                                                           -- Structure de la table `etudiant`
                                                           --
                                                           
                                                           CREATE TABLE `etudiant` (
                                                             `cne` varchar(255) DEFAULT NULL,
                                                             `dateNaissance` datetime(6) DEFAULT NULL,
                                                             `idEtudiant` bigint(20) NOT NULL
                                                           ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
                                                           
                                                           --
                                                           -- Déchargement des données de la table `etudiant`
                                                           --
                                                           
                                                           INSERT INTO `etudiant` (`cne`, `dateNaissance`, `idEtudiant`) VALUES
                                                           ('aaaa', NULL, 9),
                                                           ('dsqd', NULL, 11);
                                                           
                                                           -- --------------------------------------------------------
                                                           
                                                           --
                                                           -- Structure de la table `filiere`
                                                           --
                                                           
                                                           CREATE TABLE `filiere` (
                                                             `idFiliere` bigint(20) NOT NULL,
                                                             `anneeFinaccreditation` int(11) NOT NULL,
                                                             `anneeaccreditation` int(11) NOT NULL,
                                                             `codeFiliere` varchar(255) DEFAULT NULL,
                                                             `titreFiliere` varchar(255) DEFAULT NULL
                                                           ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
                                                           
                                                           -- --------------------------------------------------------
                                                           
                                                           --
                                                           -- Structure de la table `inscriptionannuelle`
                                                           --
                                                           
                                                           CREATE TABLE `inscriptionannuelle` (
                                                             `idInscription` bigint(20) NOT NULL,
                                                             `annee` int(11) NOT NULL,
                                                             `etat` int(11) NOT NULL,
                                                             `mention` varchar(255) DEFAULT NULL,
                                                             `plusInfos` varchar(255) DEFAULT NULL,
                                                             `rang` int(11) NOT NULL,
                                                             `type` varchar(255) DEFAULT NULL,
                                                             `validation` varchar(255) DEFAULT NULL,
                                                             `idEtudiant` bigint(20) DEFAULT NULL,
                                                             `idNiveau` bigint(20) DEFAULT NULL
                                                           ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
                                                           
                                                           -- --------------------------------------------------------
                                                           
                                                           --
                                                           -- Structure de la table `inscriptionmatiere`
                                                           --
                                                           
                                                           CREATE TABLE `inscriptionmatiere` (
                                                             `idInscriptionMatiere` bigint(20) NOT NULL,
                                                             `coefficient` double NOT NULL,
                                                             `noteFinale` double NOT NULL,
                                                             `noteSN` double NOT NULL,
                                                             `noteSR` double NOT NULL,
                                                             `plusInfos` varchar(255) DEFAULT NULL,
                                                             `validation` varchar(255) DEFAULT NULL,
                                                             `idInscription` bigint(20) DEFAULT NULL,
                                                             `idMatiere` bigint(20) DEFAULT NULL
                                                           ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
                                                           
                                                           -- --------------------------------------------------------
                                                           
                                                           --
                                                           -- Structure de la table `inscriptionmodule`
                                                           --
                                                           
                                                           CREATE TABLE `inscriptionmodule` (
                                                             `idInscriptionModule` bigint(20) NOT NULL,
                                                             `noteFinale` double NOT NULL,
                                                             `noteSN` double NOT NULL,
                                                             `noteSR` double NOT NULL,
                                                             `plusInfos` varchar(255) DEFAULT NULL,
                                                             `validation` varchar(255) DEFAULT NULL,
                                                             `idInscription` bigint(20) DEFAULT NULL,
                                                             `idModule` bigint(20) DEFAULT NULL
                                                           ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
                                                           
                                                           -- --------------------------------------------------------
                                                           
                                                           --
                                                           -- Structure de la table `module`
                                                           --
                                                           
                                                           CREATE TABLE `module` (
                                                             `idModule` bigint(20) NOT NULL,
                                                             `code` varchar(255) DEFAULT NULL,
                                                             `titre` varchar(255) DEFAULT NULL,
                                                             `idNiveau` bigint(20) DEFAULT NULL
                                                           ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
                                                           
                                                           -- --------------------------------------------------------
                                                           
                                                           --
                                                           -- Structure de la table `niveau`
                                                           --
                                                           
                                                           CREATE TABLE `niveau` (
                                                             `idNiveau` bigint(20) NOT NULL,
                                                             `alias` varchar(255) DEFAULT NULL,
                                                             `titre` varchar(255) DEFAULT NULL,
                                                             `idFiliere` bigint(20) DEFAULT NULL
                                                           ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
                                                           
                                                           -- --------------------------------------------------------
                                                           
                                                           --
                                                           -- Structure de la table `role`
                                                           --
                                                           
                                                           CREATE TABLE `role` (
                                                             `idRole` bigint(20) NOT NULL,
                                                             `nomRole` varchar(255) DEFAULT NULL
                                                           ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
                                                           
                                                           --
                                                           -- Déchargement des données de la table `role`
                                                           --
                                                           
                                                           INSERT INTO `role` (`idRole`, `nomRole`) VALUES
                                                           (1, 'ROLE_ADMIN'),
                                                           (2, 'ROLE_PROF'),
                                                           (3, 'ROLE_CADRE_ADMIN'),
                                                           (4, 'ROLE_STUDENT'),
                                                           (5, 'ROLE_BIBLIO');
                                                           
                                                           -- --------------------------------------------------------
                                                           
                                                           --
                                                           -- Structure de la table `utilisateur`
                                                           --
                                                           
                                                           CREATE TABLE `utilisateur` (
                                                             `idUtilisateur` bigint(20) NOT NULL,
                                                             `cin` varchar(255) DEFAULT NULL,
                                                             `email` varchar(255) DEFAULT NULL,
                                                             `nom` varchar(255) DEFAULT NULL,
                                                             `nomArabe` varchar(255) DEFAULT NULL,
                                                             `photo` varchar(255) DEFAULT NULL,
                                                             `prenom` varchar(255) DEFAULT NULL,
                                                             `prenomArabe` varchar(255) DEFAULT NULL,
                                                             `telephone` varchar(255) DEFAULT NULL
                                                           ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
                                                           
                                                           --
                                                           -- Déchargement des données de la table `utilisateur`
                                                           --
                                                           
                                                           INSERT INTO `utilisateur` (`idUtilisateur`, `cin`, `email`, `nom`, `nomArabe`, `photo`, `prenom`, `prenomArabe`, `telephone`) VALUES
                                                           (8, 'AAAA', 'tarik@tarik.fr', 'BOUDAA', 'Tarik', NULL, 'Tarik', 'Boudaa', '060000000'),
                                                           (9, 'ABnh1', 'med1@med.com', 'BOUDAA', 'med', NULL, 'Mohamed', 'med', '2522255'),
                                                           (10, 'dqsdqsd', 'dqsdsqd', 'dqdqsd', 'dqsdqdqsd', NULL, 'dsqd', 'dqdqd', '12589632'),
                                                           (11, 'AB', 'dsqdqs', 'dsqd', 'dsqdqsd', NULL, 'aaa', 'dqsdqsd', '12589632'),
                                                           (12, 'BBBB', 'karama@karam.fr', 'KARAM', 'KARAM', NULL, 'KARAM', 'KARAM', '0666666666');
                                                           
                                                           --
                                                           -- Index pour les tables déchargées
                                                           --
                                                           
                                                           --
                                                           -- Index pour la table `cadreadministrateur`
                                                           --
                                                           ALTER TABLE `cadreadministrateur`
                                                             ADD PRIMARY KEY (`idCardreAdmin`);
                                                           
                                                           --
                                                           -- Index pour la table `compte`
                                                           --
                                                           ALTER TABLE `compte`
                                                             ADD PRIMARY KEY (`idCompte`),
                                                             ADD KEY `FK4me3b7yms83bsk757qlkk5icm` (`idUtilisateur`),
                                                             ADD KEY `FK6rqvo0g5sv97xlbrragf5rwn3` (`idRole`);
                                                           
                                                           --
                                                           -- Index pour la table `element`
                                                           --
                                                           ALTER TABLE `element`
                                                             ADD PRIMARY KEY (`idMatiere`),
                                                             ADD KEY `FKpy7uud3qt1x365dnkff4f41l8` (`idModule`);
                                                           
                                                           --
                                                           -- Index pour la table `enseignant`
                                                           --
                                                           ALTER TABLE `enseignant`
                                                             ADD PRIMARY KEY (`idEnseighant`);
                                                           
                                                           --
                                                           -- Index pour la table `etudiant`
                                                           --
                                                           ALTER TABLE `etudiant`
                                                             ADD PRIMARY KEY (`idEtudiant`);
                                                           
                                                           --
                                                           -- Index pour la table `filiere`
                                                           --
                                                           ALTER TABLE `filiere`
                                                             ADD PRIMARY KEY (`idFiliere`);
                                                           
                                                           --
                                                           -- Index pour la table `inscriptionannuelle`
                                                           --
                                                           ALTER TABLE `inscriptionannuelle`
                                                             ADD PRIMARY KEY (`idInscription`),
                                                             ADD KEY `FKge2xwqtfeqnojw9no8og6vbqn` (`idEtudiant`),
                                                             ADD KEY `FK9lrdmhkam481adiwotdpqo8w` (`idNiveau`);
                                                           
                                                           --
                                                           -- Index pour la table `inscriptionmatiere`
                                                           --
                                                           ALTER TABLE `inscriptionmatiere`
                                                             ADD PRIMARY KEY (`idInscriptionMatiere`),
                                                             ADD KEY `FKdrefbosgrrf561bghbosk681q` (`idInscription`),
                                                             ADD KEY `FK6om7ooil7qy2ipbtocv7hqrwo` (`idMatiere`);
                                                           
                                                           --
                                                           -- Index pour la table `inscriptionmodule`
                                                           --
                                                           ALTER TABLE `inscriptionmodule`
                                                             ADD PRIMARY KEY (`idInscriptionModule`),
                                                             ADD KEY `FK2rp4wu9gg4s1yvbannj858m3c` (`idInscription`),
                                                             ADD KEY `FKsfog581rh033dgomu0u7xywgd` (`idModule`);
                                                           
                                                           --
                                                           -- Index pour la table `module`
                                                           --
                                                           ALTER TABLE `module`
                                                             ADD PRIMARY KEY (`idModule`),
                                                             ADD KEY `FK2kgd6okkiatvq3do7akj1cm2k` (`idNiveau`);
                                                           
                                                           --
                                                           -- Index pour la table `niveau`
                                                           --
                                                           ALTER TABLE `niveau`
                                                             ADD PRIMARY KEY (`idNiveau`),
                                                             ADD KEY `FK9qvkxk4ayqkjopclmlgoel8d9` (`idFiliere`);
                                                           
                                                           --
                                                           -- Index pour la table `role`
                                                           --
                                                           ALTER TABLE `role`
                                                             ADD PRIMARY KEY (`idRole`);
                                                           
                                                           --
                                                           -- Index pour la table `utilisateur`
                                                           --
                                                           ALTER TABLE `utilisateur`
                                                             ADD PRIMARY KEY (`idUtilisateur`),
                                                             ADD UNIQUE KEY `UK_s4m395xkorrxtrdbuk1upglup` (`cin`),
                                                             ADD UNIQUE KEY `UK_35ysk0sh9ruwixrld3nc0weut` (`email`);
                                                           
                                                           --
                                                           -- AUTO_INCREMENT pour les tables déchargées
                                                           --
                                                           
                                                           --
                                                           -- AUTO_INCREMENT pour la table `compte`
                                                           --
                                                           ALTER TABLE `compte`
                                                             MODIFY `idCompte` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
                                                           
                                                           --
                                                           -- AUTO_INCREMENT pour la table `element`
                                                           --
                                                           ALTER TABLE `element`
                                                             MODIFY `idMatiere` bigint(20) NOT NULL AUTO_INCREMENT;
                                                           
                                                           --
                                                           -- AUTO_INCREMENT pour la table `filiere`
                                                           --
                                                           ALTER TABLE `filiere`
                                                             MODIFY `idFiliere` bigint(20) NOT NULL AUTO_INCREMENT;
                                                           
                                                           --
                                                           -- AUTO_INCREMENT pour la table `inscriptionannuelle`
                                                           --
                                                           ALTER TABLE `inscriptionannuelle`
                                                             MODIFY `idInscription` bigint(20) NOT NULL AUTO_INCREMENT;
                                                           
                                                           --
                                                           -- AUTO_INCREMENT pour la table `inscriptionmatiere`
                                                           --
                                                           ALTER TABLE `inscriptionmatiere`
                                                             MODIFY `idInscriptionMatiere` bigint(20) NOT NULL AUTO_INCREMENT;
                                                           
                                                           --
                                                           -- AUTO_INCREMENT pour la table `inscriptionmodule`
                                                           --
                                                           ALTER TABLE `inscriptionmodule`
                                                             MODIFY `idInscriptionModule` bigint(20) NOT NULL AUTO_INCREMENT;
                                                           
                                                           --
                                                           -- AUTO_INCREMENT pour la table `module`
                                                           --
                                                           ALTER TABLE `module`
                                                             MODIFY `idModule` bigint(20) NOT NULL AUTO_INCREMENT;
                                                           
                                                           --
                                                           -- AUTO_INCREMENT pour la table `niveau`
                                                           --
                                                           ALTER TABLE `niveau`
                                                             MODIFY `idNiveau` bigint(20) NOT NULL AUTO_INCREMENT;
                                                           
                                                           --
                                                           -- AUTO_INCREMENT pour la table `role`
                                                           --
                                                           ALTER TABLE `role`
                                                             MODIFY `idRole` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
                                                           
                                                           --
                                                           -- Contraintes pour les tables déchargées
                                                           --
                                                           
                                                           --
                                                           -- Contraintes pour la table `cadreadministrateur`
                                                           --
                                                           ALTER TABLE `cadreadministrateur`
                                                             ADD CONSTRAINT `FKq2jdlid8esk1jlagny4qhrh2k` FOREIGN KEY (`idCardreAdmin`) REFERENCES `utilisateur` (`idUtilisateur`);
                                                           
                                                           --
                                                           -- Contraintes pour la table `compte`
                                                           --
                                                           ALTER TABLE `compte`
                                                             ADD CONSTRAINT `FK4me3b7yms83bsk757qlkk5icm` FOREIGN KEY (`idUtilisateur`) REFERENCES `utilisateur` (`idUtilisateur`),
                                                             ADD CONSTRAINT `FK6rqvo0g5sv97xlbrragf5rwn3` FOREIGN KEY (`idRole`) REFERENCES `role` (`idRole`);
                                                           
                                                           --
                                                           -- Contraintes pour la table `element`
                                                           --
                                                           ALTER TABLE `element`
                                                             ADD CONSTRAINT `FKpy7uud3qt1x365dnkff4f41l8` FOREIGN KEY (`idModule`) REFERENCES `module` (`idModule`);
                                                           
                                                           --
                                                           -- Contraintes pour la table `enseignant`
                                                           --
                                                           ALTER TABLE `enseignant`
                                                             ADD CONSTRAINT `FKk26kuxt8qhs6nqv41b2hiyqwb` FOREIGN KEY (`idEnseighant`) REFERENCES `utilisateur` (`idUtilisateur`);
                                                           
                                                           --
                                                           -- Contraintes pour la table `etudiant`
                                                           --
                                                           ALTER TABLE `etudiant`
                                                             ADD CONSTRAINT `FKkku0boly4both705vo0fri81c` FOREIGN KEY (`idEtudiant`) REFERENCES `utilisateur` (`idUtilisateur`);
                                                           
                                                           --
                                                           -- Contraintes pour la table `inscriptionannuelle`
                                                           --
                                                           ALTER TABLE `inscriptionannuelle`
                                                             ADD CONSTRAINT `FK9lrdmhkam481adiwotdpqo8w` FOREIGN KEY (`idNiveau`) REFERENCES `niveau` (`idNiveau`),
                                                             ADD CONSTRAINT `FKge2xwqtfeqnojw9no8og6vbqn` FOREIGN KEY (`idEtudiant`) REFERENCES `etudiant` (`idEtudiant`);
                                                           
                                                           --
                                                           -- Contraintes pour la table `inscriptionmatiere`
                                                           --
                                                           ALTER TABLE `inscriptionmatiere`
                                                             ADD CONSTRAINT `FK6om7ooil7qy2ipbtocv7hqrwo` FOREIGN KEY (`idMatiere`) REFERENCES `element` (`idMatiere`),
                                                             ADD CONSTRAINT `FKdrefbosgrrf561bghbosk681q` FOREIGN KEY (`idInscription`) REFERENCES `inscriptionannuelle` (`idInscription`);
                                                           
                                                           --
                                                           -- Contraintes pour la table `inscriptionmodule`
                                                           --
                                                           ALTER TABLE `inscriptionmodule`
                                                             ADD CONSTRAINT `FK2rp4wu9gg4s1yvbannj858m3c` FOREIGN KEY (`idInscription`) REFERENCES `inscriptionannuelle` (`idInscription`),
                                                             ADD CONSTRAINT `FKsfog581rh033dgomu0u7xywgd` FOREIGN KEY (`idModule`) REFERENCES `module` (`idModule`);
                                                           
                                                           --
                                                           -- Contraintes pour la table `module`
                                                           --
                                                           ALTER TABLE `module`
                                                             ADD CONSTRAINT `FK2kgd6okkiatvq3do7akj1cm2k` FOREIGN KEY (`idNiveau`) REFERENCES `niveau` (`idNiveau`);
                                                           
                                                           --
                                                           -- Contraintes pour la table `niveau`
                                                           --
                                                           ALTER TABLE `niveau`
                                                             ADD CONSTRAINT `FK9qvkxk4ayqkjopclmlgoel8d9` FOREIGN KEY (`idFiliere`) REFERENCES `filiere` (`idFiliere`);
                                                           COMMIT;
                                                           
                                                           /*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
                                                           /*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
                                                           /*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
                                                           
                                
                                        
                              
                             
                    """;
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
        }catch (Exception ex){
            LOGGER.error(ex);
            throw  new DataBaseException(ex);
        }

    }

    public static boolean checkIfAlreadyInstalled() throws IOException {
        String userHomeDirectory = System.getProperty("user.home");
        Properties dbProperties = DbPropertiesLoader.loadProperties("conf.properties");
        String url = dbProperties.getProperty("db.url");
        String username = dbProperties.getProperty("db.login");
        String password = dbProperties.getProperty("db.password");
        String databaseName = dbProperties.getProperty("db.name");

        String databaseFile = userHomeDirectory + "\\" + databaseName + ".mv.db";
        System.out.println(databaseName);

        // Check if the database file exists
        boolean databaseExists = FileManager.fileExists(databaseFile);
        if (!databaseExists) {
            return false;
        }

        // Check if the tables are created
        try (Connection connection = DriverManager.getConnection(url + databaseName, username, password);
             Statement statement = connection.createStatement()) {

            // Check if the table exists
            ResultSet resultSet = connection.getMetaData().getTables(null, null, "your_table_name", null);
            boolean tableExists = resultSet.next();
            resultSet.close();

            if (!tableExists) {
                return false;
            }

            // Add more table checks if needed

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //

}

