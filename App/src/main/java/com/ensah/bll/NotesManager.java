package com.ensah.bll;

import com.ensah.bo.Etudiant;
import com.ensah.bo.InscriptionAnnuelle;
import com.ensah.dao.DataBaseException;
import com.ensah.dao.EtudiantDao;
import com.ensah.dao.InscriptionAnnuelleDao;
import com.ensah.utils.ExcelDelibReader;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static com.ensah.utils.ExcelDelibReader.readDelibResults;

public class NotesManager {
    private EtudiantDao etudiantDao = new EtudiantDao();
    InscriptionAnnuelleDao inscriptionAnnuelleDao = new InscriptionAnnuelleDao();
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
    public void insererNotesFinales() throws IOException, SQLException, DataBaseException {
    List<ExcelDelibReader.DelibResult> delibResults = readDelibResults();
    for (ExcelDelibReader.DelibResult result : delibResults) {
        double x = result.getMoyenne();
        boolean moyenneBol = inscriptionAnnuelleDao.validationModule(new Etudiant(result.getIdEtudiant()),(long) x);
        String mention;
        String validation;
        if(!moyenneBol) {
            mention = "Aj";
            validation = "NV";
        }
        else {
            mention = "V"; // à remplacer plus tard
            validation="V";
        }
        inscriptionAnnuelleDao.notesFinalesDelib(result.getIdEtudiant(),result.getAliasClass(),result.getRang(),mention,validation);
    }
}

    public boolean verifierExistanceEnregistrements() throws DataBaseException, IOException {
        List<ExcelDelibReader.DelibResult> delibResults = readDelibResults();
        for (ExcelDelibReader.DelibResult result : delibResults) {
            List<InscriptionAnnuelle> inscriptionAnnList = inscriptionAnnuelleDao.getInscriptionAnnByNiv(result.getAliasClass());

            for (InscriptionAnnuelle inscriptionAnn : inscriptionAnnList) {
                if (inscriptionAnn.getValidation() != null) {
                    return true;
                }
            }


        }
        return false;
    }





}
