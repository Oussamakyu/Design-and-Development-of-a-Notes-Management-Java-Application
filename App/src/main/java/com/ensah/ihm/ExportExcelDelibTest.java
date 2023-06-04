package com.ensah.ihm;

import com.ensah.bll.BllException;
import com.ensah.bll.DeliberationManager;
import com.ensah.bo.*;
import com.ensah.bo.Module;
import com.ensah.dao.*;
import com.ensah.utils.ExcelDelibExport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportExcelDelibTest {

    public static void main(String[] args) throws IOException, DataBaseException, BllException {

        DeliberationManager deliberationManager = new DeliberationManager();
        try{
            deliberationManager.exportDeliberation("CP2");
        }catch (BllException blex){
            System.err.println(blex.getMessage());
        }




    }
}
