package com.ensah.utils;


import com.ensah.bo.Etudiant;

import java.util.ArrayList;
import java.util.List;

public class FilterExcel {

    private List<Etudiant> newStudents = new ArrayList<>();
    private List<Object> oldStudents = new ArrayList<>();

    public void importStudent(List<List<Object>> pDataFromExcel){
        for(List<Object> row : pDataFromExcel){
            if (row.get(5).equals("INSCRIPTION")){
                Etudiant newEtudiant = new Etudiant();
                newEtudiant.setIdUtilisateur((long) row.get(0));
                newEtudiant.setCne((String) row.get(1));
                newEtudiant.setNom((String) row.get(2));
                newEtudiant.setPrenom((String) row.get(3));
                newStudents.add(newEtudiant);
            }else if (row.get(5).equals("REINSCRIPTION")){
                Etudiant oldEtudiant = new Etudiant();
                oldEtudiant.setIdUtilisateur((long) row.get(0));
                oldEtudiant.setCne((String) row.get(1));
                oldEtudiant.setNom((String) row.get(2));
                oldEtudiant.setPrenom((String) row.get(3));
                oldStudents.add(oldEtudiant);
                oldStudents.add(row.get(4));
            }
        }
    }

    public List<Etudiant> getNewStudents() {
        return newStudents;
    }

    public List<Object> getOldStudents() {
        return oldStudents;
    }
}
