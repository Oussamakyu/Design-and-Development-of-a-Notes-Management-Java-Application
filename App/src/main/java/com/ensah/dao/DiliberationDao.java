package com.ensah.dao;

import java.util.List;

public class DiliberationDao {

    public long calcNote(List<Long> l, String cne){ // une liste contenant toute les notes de l'année
        long res=0;
        for (long i : l ){
            res+=i;
        }
        return res;
    }

    public String validAnn(int nmbrNV , long noteAnn){ //pour ci
        if (nmbrNV>3)
                return "Aj"; //Ajourné
        if(noteAnn>=12)
            return "V";
        return "Aj";
    }



}
