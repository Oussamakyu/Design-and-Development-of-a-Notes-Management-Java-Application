package com.ensah.bll;

import com.ensah.bo.Module;
import org.apache.log4j.Logger;
import com.ensah.bo.*;
import com.ensah.dao.*;

import java.util.List;
import java.util.Scanner;

public class StudentManager {
    private Logger logger = Logger.getLogger(StudentManager.class);
    private EtudiantDao etudiantDao = new EtudiantDao();
    private UtilisateurDao utilisaterDao = new UtilisateurDao();
    private InscriptionAnnuelleDao inscriptionAnnuelleDao = new InscriptionAnnuelleDao();
    private InscriptionModuleDao inscriptionModuleDao = new InscriptionModuleDao();
    private InscriptionMatiereDao inscriptionMatiereDaoDao = new InscriptionMatiereDao();
    private NiveauDao niveauDao = new NiveauDao();

    private ModuleDao moduleDao = new ModuleDao();
    private HistoriqueModifDao historiqueModifDao = new HistoriqueModifDao();


    public void checkInscriptionEtudiant(Etudiant newStudent) throws DataBaseException, BllException {
        if (etudiantDao.etuExist(newStudent)) {
            logger.error("L'étudiant avec l'id " + newStudent.getIdUtilisateur() + " existe déja dans la base de données.");
            throw new BllException("L'étudiant avec l'id " + newStudent.getIdUtilisateur() + " existe déja dans la base de données.");
        }
    }

    public void inscriptionEtudiant(Etudiant newStudent) throws DataBaseException {
        utilisaterDao.add(newStudent);
        etudiantDao.inscrire(newStudent);
        logger.info("L'etudiant avec l'id " + newStudent.getIdUtilisateur() + "a été ajouter dans la base de données");
        long idInsc = inscriptionAnnuelleDao.ajouter(newStudent, new Niveau((long) 1), "INSCRIPTION");
        List<Module> niveauList = niveauDao.getModules(1);
        for (Module module : niveauList) {
            inscriptionModuleDao.ajouter(idInsc, module.getIdModule());
            List<Element> elementList = moduleDao.getElements(module.getIdModule());
            if (elementList.size() != 0) {
                for (Element element : elementList) {
                    inscriptionMatiereDaoDao.ajouter(idInsc, element);
                }
            }
        }
    }

    public void checkReInscriptionEtudiant(Etudiant pEtudiant, long pIdNiveau) throws DataBaseException, BllException {
        Niveau newNiveauForOldStudent = niveauDao.getNiveau(pIdNiveau);
        Etudiant oldStudent = etudiantDao.findById(pEtudiant.getIdUtilisateur());
        if (newNiveauForOldStudent == null) {
            logger.error("Le niveau avec l'id " + pIdNiveau + " n'existe pas.");
            throw new BllException("Le niveau avec l'id " + pIdNiveau + " n'existe pas.");
        }
        if (oldStudent == null) {
            logger.error("L'étudiant avec l'id " + pEtudiant.getIdUtilisateur() + " n'existe pas dans la base de données.");
            throw new BllException("L'étudiant avec l'id " + pEtudiant.getIdUtilisateur() + " n'existe pas dans la base de données.");
        }
        Utilisateur oldUtilisateur = utilisaterDao.findById(pEtudiant.getIdUtilisateur());
        oldStudent.setNom(oldUtilisateur.getNom());
        oldStudent.setPrenom(oldUtilisateur.getPrenom());
        List<InscriptionAnnuelle> oldStudentInscriptions = inscriptionAnnuelleDao.getAllInscriptionAnn(oldStudent);
        if(oldStudentInscriptions.get(0).getValidation() == null){
            throw new BllException("Les résultats de l'année précédente pour cet étudiant sont incomplets.");
        }
        if (oldStudentInscriptions.get(0).getValidation().equals("Aj")) {
            if (!oldStudentInscriptions.get(0).getNiveau().getIdNiveau().equals(newNiveauForOldStudent.getIdNiveau())) {
                logger.error("Le niveau de l'étudiant avec l'id " + pEtudiant.getIdUtilisateur() + " est contradictoire avec les resultats de l'année passer.");
                throw new BllException("Le niveau de l'étudiant avec l'id " + pEtudiant.getIdUtilisateur() + " est contradictoire avec les resultats de l'année passer.");
            }
        } else {
            if (niveauDao.getNextNiveau(oldStudentInscriptions.get(0).getNiveau().getIdNiveau()) != newNiveauForOldStudent.getIdNiveau()) {
                logger.error("Le niveau de l'étudiant avec l'id " + pEtudiant.getIdUtilisateur() + " est contradictoire avec les resultats de l'année passer.");
                throw new BllException("Le niveau de l'étudiant avec l'id " + pEtudiant.getIdUtilisateur() + " est contradictoire avec les resultats de l'année passer.");
            }
        }


    }

    public void reInscriptionEtudiant(Etudiant pEtudiant, long pIdNiveau) throws DataBaseException {
        Niveau newNiveauForOldStudent = niveauDao.getNiveau(pIdNiveau);
        Etudiant oldStudent = etudiantDao.findById(pEtudiant.getIdUtilisateur());
        Utilisateur oldUtilisateur = utilisaterDao.findById(pEtudiant.getIdUtilisateur());
        oldStudent.setNom(oldUtilisateur.getNom());
        oldStudent.setPrenom(oldUtilisateur.getPrenom());
        List<InscriptionAnnuelle> oldStudentInscriptions = inscriptionAnnuelleDao.getAllInscriptionAnn(oldStudent);
        if (!oldStudent.getCne().equals(pEtudiant.getCne()) || !oldUtilisateur.getNom().equals(pEtudiant.getNom()) || !oldUtilisateur.getPrenom().equals(pEtudiant.getPrenom())) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Les données d'etudiant suivant ne sont pasles même que celles présentes dans l'application.");
            System.out.println("Données du fichier:\n   CNE : " + pEtudiant.getCne() + "\n   NOM : " + pEtudiant.getNom() + "\n   PRENOM : " + pEtudiant.getPrenom());
            System.out.println("Données en base de données:\n   CNE : " + oldStudent.getCne() + "\n   NOM : " + oldUtilisateur.getNom() + "\n   PRENOM : " + oldUtilisateur.getPrenom());
            System.out.print("Voulez vous modifier ces informations ? (Y/N) : ");
            String option = sc.nextLine();
            if (option.equals("y") || option.equals("Y")) {
                oldUtilisateur.setNom(pEtudiant.getNom());
                oldUtilisateur.setPrenom(pEtudiant.getPrenom());
                oldStudent.setCne(pEtudiant.getCne());
                historiqueModifDao.add(oldStudent);
                utilisaterDao.modify(oldUtilisateur);
                etudiantDao.mofidy(oldStudent);
                logger.info("Modification des données de l'etudiant avec l'id " + oldStudent.getIdUtilisateur());
            }
        }
        if (oldStudentInscriptions.get(0).getValidation().equals("Aj")) {
            List<InscriptionModule> nvInscModules = inscriptionModuleDao.getNvIncModule(oldStudentInscriptions.get(0).getIdInscription());
            logger.info("Reinscription de l'etudiant avec l'id " + oldStudent.getIdUtilisateur()+ " a été effectué");
            long idInsc = inscriptionAnnuelleDao.ajouter(oldStudent, newNiveauForOldStudent, "REINSCRIPTION");
            int nbmodNextNiv = 12 - nvInscModules.size();
            List<Module> niveauList = niveauDao.getModules(niveauDao.getNextNiveau(pIdNiveau));
            for (InscriptionModule nvModule : nvInscModules){
                inscriptionModuleDao.ajouter(idInsc,nvModule.getModule().getIdModule());
                List<Element> nvelementList = moduleDao.getElements(nvModule.getModule().getIdModule());
                if (nvelementList.size() != 0){
                    for (Element element : nvelementList){
                        inscriptionMatiereDaoDao.ajouter(idInsc,element);
                    }
                }
            }
            for (int i = 0; i < nbmodNextNiv; i++) {
                inscriptionModuleDao.ajouter(idInsc, niveauList.get(i).getIdModule());
                List<Element> elementList = moduleDao.getElements(niveauList.get(i).getIdModule());
                if (elementList.size() != 0){
                    for (Element element : elementList){
                        inscriptionMatiereDaoDao.ajouter(idInsc,element);
                    }
                }
            }
        }else {
            long idInsc = inscriptionAnnuelleDao.ajouter(oldStudent,newNiveauForOldStudent,"REINSCRIPTION");
            logger.info("Reinscription de l'etudiant avec l'id " + oldStudent.getIdUtilisateur()+ " a été effectué");
            List<Module> niveauList = niveauDao.getModules(pIdNiveau);
            for(Module module : niveauList){
                inscriptionModuleDao.ajouter(idInsc,module.getIdModule());
                List<Element> elementList =moduleDao.getElements(module.getIdModule());
                if (elementList.size() != 0){
                    for (Element element : elementList){
                        inscriptionMatiereDaoDao.ajouter(idInsc,element);
                    }
                }
            }
        }
    }
}

