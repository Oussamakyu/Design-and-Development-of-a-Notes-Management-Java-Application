package com.ensah.bll;

import com.ensah.bo.Element;
import com.ensah.bo.Filiere;
import com.ensah.bo.Module;
import com.ensah.bo.Niveau;
import com.ensah.dao.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class StructureManager {
    private ElementDao elementDao;
    private ModuleDao moduleDao;
    private NiveauDao niveauDao;
    private FiliereDao filiereDao;
    private CoordinatorFiliereDao coordinatorFiliereDao;


    public StructureManager() {
        elementDao = new ElementDao();
        moduleDao = new ModuleDao();
        niveauDao = new NiveauDao();
        filiereDao = new FiliereDao();
        coordinatorFiliereDao = new CoordinatorFiliereDao();
    }

    // Element methods
    public void createElement(Element element) throws DataBaseException, BusinessLogicException {
        boolean x = elementDao.createElement(element);
        if(!x){
            throw new BusinessLogicException("L'élément' existe déjà ! ");
        }
    }

    public void updateElement(Element element) throws DataBaseException, BusinessLogicException {
        boolean x = elementDao.updateElement(element);
        if(!x)
            throw new BusinessLogicException("Elément n'existe pas  ! ");
    }

    public void deleteElement(long elementId) throws DataBaseException, BusinessLogicException {
        boolean x =elementDao.deleteElement(elementId);
        if(!x)
            throw new BusinessLogicException("Elément n'existe pas  ! ");
    }

    // Module methods
    public void createModule(Module module) throws BusinessLogicException, DataBaseException {
        if(moduleDao.findModule(getIdModule(module.getTitre())) != null)
            throw new BusinessLogicException("Module existe déjà  ! ");
        moduleDao.createModule(module);
    }

    public void updateModule(Module module) throws DataBaseException, BusinessLogicException {
        if(moduleDao.findModule(module.getIdModule()) == null)
            throw new BusinessLogicException("Module n'existe pas ! ");
        moduleDao.updateModule(module);
    }

    public void deleteModule(long moduleId) throws DataBaseException, BusinessLogicException {
        if(moduleDao.findModule(moduleId) == null)
            throw new BusinessLogicException("Module n'existe pas ! ");
        moduleDao.deleteModule(moduleId);
    }

    // Niveau (Class) methods
    public void createNiveau(Niveau niveau) throws DataBaseException, BusinessLogicException {
        if(niveauDao.getNiveauIdByAlias(niveau.getAlias())!= null)
            throw new BusinessLogicException("Niveau existe déjà ! ");
        niveauDao.createNiveau(niveau);
    }

    public void updateNiveau(Niveau niveau,long idNextNiveau) throws DataBaseException, BusinessLogicException {
        if(niveauDao.findNiveauById(niveau.getIdNiveau())== null)
            throw new BusinessLogicException("Niveau n'existe pas ! ");
        niveauDao.updateNiveau(niveau,idNextNiveau);
    }

    public void deleteNiveau(long niveauId) throws DataBaseException, BusinessLogicException {
        if(niveauDao.findNiveauById(niveauId)== null)
            throw new BusinessLogicException("Niveau n'existe pas ! ");
        niveauDao.deleteNiveau(niveauId);
    }

    // Filiere methods
    public void createFiliere(Filiere filiere) throws DataBaseException, BusinessLogicException {
        filiereDao.createFiliere(filiere);
    }

    public void updateFiliere(Filiere filiere) throws DataBaseException, BusinessLogicException {
        if(filiereDao.findFiliere(filiere.getIdFiliere()) == null)
            throw new BusinessLogicException("Filière n'existe pas ! ");
        filiereDao.updateFiliere(filiere);
    }

    public void deleteFiliere(long filiereId) throws DataBaseException, BusinessLogicException {
        if(filiereDao.findFiliere(filiereId) == null)
            throw new BusinessLogicException("Filière n'existe pas ! ");
        filiereDao.deleteFiliere(filiereId);
    }

    // Method to associate a module with a class (niveau)
    public boolean associateModuleWithNiveau(long moduleId, long niveauId) throws DataBaseException {
        // Check if the module and niveau exist
        Module module = moduleDao.findModule(moduleId);
        Niveau niveau = niveauDao.findNiveauById(niveauId);

        if (module != null && niveau != null) {
            // Associate the module with the niveau
            niveau.addModule(module);
            moduleDao.associateModulesToNiveau(niveauId,niveau.getModules());
            return true;
        }

        return false;
    }

    public Module findModule(long moduleId) throws DataBaseException {
        return findModule(moduleId);
    }

    // Method to associate an element with a module
    public boolean associateElementWithModule(long elementId, long moduleId) throws DataBaseException {
        // Check if the element and module exist
        Element element = elementDao.findElementById(elementId);
        Module module = moduleDao.findModule(moduleId);

        if (element != null && module != null) {
            // Associate the element with the module
            module.addElement(element);
            moduleDao.updateModule(module);
            return true;
        }

        return false;
    }

    public Element findElementById(long elementId) throws BusinessLogicException,DataBaseException {
        Element element = elementDao.findElementById(elementId);
        if(element==null)
            throw new BusinessLogicException("Cet élément n'existe pas ");
        return element;
    }

    public Niveau findNiveauById(long niveauId) throws DataBaseException {
        Niveau niveau = niveauDao.findNiveauById(niveauId);
        return niveau;
    }

    public void associateElementsToModule(long moduleId, List<Element> elements) throws DataBaseException, BusinessLogicException {
        List<Element> allElements = elementDao.getAllElements();
        for (Element j : elements) {
            for (Element i : allElements) {
                if ((i.getIdMatiere() ==j.getIdMatiere())& (i.getModule().getIdModule()==moduleId)){
                    throw new BusinessLogicException("Ce module est déjà associé au élément ayant "+j.getIdMatiere()+" comme id.");
                }
            }
        }
        elementDao.associateElementsToModule(moduleId,elements);
    }

    public List<Element> getAllElements() throws DataBaseException {
        return elementDao.getAllElements();
    }

    public List<Niveau> getAllLevels() throws DataBaseException {
        return niveauDao.getAllLevels();
    }

    public List<Filiere> getAllFiliere() throws DataBaseException {
        return filiereDao.getAllFiliere();
    }

    // Method to associate classes (niveaux) with a filière
    public boolean associateNiveauxWithFiliere(List<Long> niveauIds, long filiereId) throws DataBaseException {
        // Check if the filière and niveaux exist
        Filiere filiere = filiereDao.findFiliere(filiereId);
        List<Niveau> niveaux = null;
        for(long i : niveauIds){
            niveaux.add(niveauDao.findNiveauById(i));
        }

        if (filiere != null && niveaux != null) {
            // Associate the niveaux with the filière
            filiere.setNiveaux((Set<Niveau>) niveaux);
            filiereDao.updateFiliere(filiere);
            return true;
        }

        return false;
    }

    public void associateClassesToFiliere(long filiereId, List<Niveau> niveaux) throws DataBaseException, BusinessLogicException {
        if(filiereDao.findFiliere(filiereId)==null)
            throw new BusinessLogicException("Filière n'existe pas ");
        for(Niveau i : niveaux){
            if(niveauDao.getFiliereIDByAlias(i.getAlias()) == filiereId){
                throw new BusinessLogicException("Niveau déjà associé à la filière !");
            }
        }
        niveauDao.associateClassesToFiliere(filiereId,niveaux);
    }

    // Method to retrieve modules for a class (niveau)
    public List<Module> getModulesForNiveau(long niveauId) throws DataBaseException {
        Niveau niveau = niveauDao.findNiveauById(niveauId);

        if (niveau != null) {
            return niveau.getModules();
        }

        return Collections.emptyList();
    }

    // Method to assign a coordinator to a filière
    public boolean assignCoordinatorToFiliere(long filiereId, long coordinatorId) throws DataBaseException {
        // Check if the filière and coordinator exist
        Filiere filiere = filiereDao.findFiliere(filiereId);
        if (filiere != null) {
            // Assign the coordinator to the filière
            coordinatorFiliereDao.createCoordinatorFiliere(coordinatorId,filiereId);
            return true;
        }

        return false;
    }

    public Filiere findFiliere(long filiereId) throws DataBaseException {
        return filiereDao.findFiliere(filiereId);
    }

    public List<Module> getModulesByClass(long niveauId) throws DataBaseException {
        return moduleDao.getModulesByClass(niveauId);
    }

    public long getIdModule(String name) throws DataBaseException {
        for(Module i : moduleDao.getAllModules()){
            if(name.equals(i.getTitre())){
                return i.getIdModule();
            }
        }
        return 0;
    }

    public long getIdNiveau(String alias) throws DataBaseException, BusinessLogicException {
        if(niveauDao.getNiveauIdByAlias(alias)==null)
            throw new BusinessLogicException("Pas de niveaux portant ce Alias");
        return niveauDao.getNiveauIdByAlias(alias);
    }

}

