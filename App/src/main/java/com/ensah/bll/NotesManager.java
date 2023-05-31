package com.ensah.bll;

import com.ensah.bo.Etudiant;
import com.ensah.dao.DataBaseException;
import com.ensah.dao.EtudiantDao;
import org.apache.log4j.Logger;

public class NotesManager {
    private EtudiantDao etudiantDao = new EtudiantDao();
    private static Logger LOGGER = Logger.getLogger(NotesManager.class);
    public void ModifierEtudiant(Etudiant etu, String newCne) throws DataBaseException, BusinessLogicException {

        Etudiant et = rechercherEtudiant(etu.getCne());
        if(et == null)
            throw new BusinessLogicException("L'étudiant n'existe pas ");
        etudiantDao.modifyInfos(etu.getCne(),newCne,etu.getNom(),etu.getPrenom());
    }

    public Etudiant rechercherEtudiant(String cne) throws DataBaseException {
        Etudiant e =etudiantDao.searchStudent(cne);
        return e;
    }
//Nous allons ajouter d'autres critères pour rechercher l'étudiant plus tard



}
