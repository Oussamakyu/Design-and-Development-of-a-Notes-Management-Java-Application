package com.ensah.ihm;

import com.ensah.bo.*;
import com.ensah.bo.Module;
import com.ensah.dao.*;
import com.ensah.utils.ExcelDelibExport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExportExcelDelibTest {

    public static void main(String[] args) throws IOException, DataBaseException {

        EtudiantDao etudiantDao = new EtudiantDao();
        NiveauDao niveauDao = new NiveauDao();
        ModuleDao moduleDao = new ModuleDao();
        InscriptionAnnuelleDao inscriptionAnnuelleDao = new InscriptionAnnuelleDao();
        InscriptionMatiereDao inscriptionMatiereDao = new InscriptionMatiereDao();
        InscriptionModuleDao inscriptionModuleDao = new InscriptionModuleDao();


        List<List> modules = new ArrayList<>();

        String classe = "CP1";
        List<InscriptionAnnuelle> inscriptionList = inscriptionAnnuelleDao.getInscriptionAnnByNiv(classe);
        List<Module> modulesName = niveauDao.getModulesByAlias("CP1");
        List<List> dataStudents = new ArrayList<>();
        for(int i=0;i<inscriptionList.size();i++){
            List<Double> notes = new ArrayList<>();
            List<String> etudInf = new ArrayList<>();
            Etudiant etud = etudiantDao.findById(inscriptionList.get(i).getEtudiant().getIdUtilisateur());
            //System.out.println(etud.getNom());
            etudInf.add(String.valueOf(etud.getIdUtilisateur()));
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
        System.out.println(dataStudents);
        System.out.println(modules);
        ExcelDelibExport delibExcel = new ExcelDelibExport(classe,inscriptionList.get(0).getAnnee()+"/"+(inscriptionList.get(0).getAnnee()+1),modules,dataStudents);
        delibExcel.generate();





//        for (Etudiant etud : etudiant){
//            System.out.println(etud.getCne());
//        }
//
//        List<Module> modulesName = niveauDao.getModulesByAlias("CP1");
//        for (Module mod : modulesName){
//            List<Element> elements = moduleDao.getElementsByName(mod.getTitre());
//            System.out.println(mod.getTitre());
//            if (elements.size() != 0){
//                for(Element ele : elements){
//                    System.out.println("Element  :"+ ele.getNom());
//                }
//            }
//            System.out.println("=============================");
//        }

    }
}
