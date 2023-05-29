package com.ensah.dao;

public class SessionRattDao {
    public long moyMat(long elem1 , long elem2){
        return (elem1+elem2)/2;
    }

    public String validation(String cycleAlias, long moy){
        if(cycleAlias.equals("CI")){
            if(moy>=12)
                return "V";
            return "NV";
        }
        if(cycleAlias.equals("CP")){
            if(moy>=10)
                return "V";
            return "NV";
        }
        return "";
    }
}
