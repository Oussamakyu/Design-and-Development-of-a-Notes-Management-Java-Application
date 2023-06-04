package com.ensah.bll;

import com.ensah.bo.*;
import com.ensah.bo.Module;
import com.ensah.dao.*;
import com.ensah.utils.ExcelDelibExport;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeliberationManager {

    public void exportDeliberation(String pClasseAlias) throws DataBaseException, IOException, BllException {

        EtudiantDao etudiantDao = new EtudiantDao();
        NiveauDao niveauDao = new NiveauDao();
        ModuleDao moduleDao = new ModuleDao();
        InscriptionAnnuelleDao inscriptionAnnuelleDao = new InscriptionAnnuelleDao();
        InscriptionMatiereDao inscriptionMatiereDao = new InscriptionMatiereDao();
        InscriptionModuleDao inscriptionModuleDao = new InscriptionModuleDao();

        List<List> modules = new ArrayList<>();


        List<InscriptionAnnuelle> inscriptionList = inscriptionAnnuelleDao.getInscriptionAnnByNiv(pClasseAlias);
        if(inscriptionList.size() ==0){
            throw new BllException("Aucune inscription pour ce niveau");
        }
        List<Module> modulesName = niveauDao.getModulesByAlias("CP1");
        List<List> dataStudents = new ArrayList<>();
        for(int i=0;i<inscriptionList.size();i++){
            List<Double> notes = new ArrayList<>();
            List<Object> etudInf = new ArrayList<>();
            Etudiant etud = etudiantDao.findById(inscriptionList.get(i).getEtudiant().getIdUtilisateur());
            //System.out.println(etud.getNom());
            etudInf.add(Integer.parseInt(String.valueOf(etud.getIdUtilisateur())));
            etudInf.add(etud.getCne());
            etudInf.add(etud.getNom());
            etudInf.add(etud.getPrenom());
            dataStudents.add(etudInf);
            for (Module mod : modulesName) {
                List<String> module = new ArrayList<>();
                if(i==0){
                    module.add(mod.getTitre());
                }
                List<Element> elements = moduleDao.getElementsByName(mod.getTitre());
                //System.out.println(mod.getTitre());
                if (elements.size() != 0) {
                    for (Element ele : elements) {
                        if(i==0){
                            module.add(ele.getNom());
                        }
                        InscriptionMatiere inscMatiere = inscriptionMatiereDao.getInscriptionMatiere(inscriptionList.get(i).getIdInscription(),ele.getNom());
                        //System.out.println("Element  :" + ele.getNom());
                        //System.out.println(inscMatiere.getNoteFinale());
                        notes.add(inscMatiere.getNoteFinale());
                    }
                }else{
                    InscriptionModule inscModule = inscriptionModuleDao.getInscriptionModule(inscriptionList.get(i).getIdInscription(),mod.getTitre());
                    //System.out.println(inscModule.getNoteFinale());
                    notes.add(inscModule.getNoteFinale());
                }
                if(i==0){
                    modules.add(module);
                }
                //System.out.println("=============================");
            }
            dataStudents.add(notes);
        }
        List<List> finaleDataStudents = new ArrayList<>();
        for(int i=0;i<dataStudents.size();i+=4){
            finaleDataStudents.add(dataStudents.get(i));
            finaleDataStudents.add(dataStudents.get(i+1));
            if(i+2<dataStudents.size()){
                if(dataStudents.get(i).get(0)==dataStudents.get(i+2).get(0)){
                    System.out.println(dataStudents.get(i).get(0)==dataStudents.get(i+2).get(0));
                    for(int j=0;j<dataStudents.get(i+1).size();j++){
                        if((Double.parseDouble(String.valueOf(dataStudents.get(i+1).get(j))))<(Double.parseDouble(String.valueOf(dataStudents.get(i+3).get(j))))){
                            dataStudents.get(i + 1).set(j, dataStudents.get(i + 3).get(j));
                        }
                    }
                    finaleDataStudents.set(i+1,dataStudents.get(i+1));
                }
            }
        }


        System.out.println(finaleDataStudents);
        System.out.println(modules);
        ExcelDelibExport delibExcel = new ExcelDelibExport(pClasseAlias,inscriptionList.get(0).getAnnee()+"/"+(inscriptionList.get(0).getAnnee()+1),modules,finaleDataStudents);
        delibExcel.generate();
    }


}
