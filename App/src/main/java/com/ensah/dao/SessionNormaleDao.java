package com.ensah.dao;

public class SessionNormaleDao {

    public long moyMat(long elem1 , long elem2){
        return (elem1+elem2)/2;
    }

    public String validation(String cycleAlias, long moy){
        if(cycleAlias.equals("CI")){
            if(moy>=12)
                return "V";
            return "R";
        }
        if(cycleAlias.equals("CP")){
            if(moy>=10)
                return "V";
            return "R";
        }
        return "";
    }


}
