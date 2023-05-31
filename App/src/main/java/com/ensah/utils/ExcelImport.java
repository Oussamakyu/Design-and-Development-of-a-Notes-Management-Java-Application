package com.ensah.utils;

import org.apache.log4j.Logger;
import com.ensah.bll.ImportException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.ensah.dao.DBConnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelImport {
    private Logger logger = Logger.getLogger(DBConnection.class);

    private XSSFSheet sheet;


    public ExcelImport(String excelPath) throws ImportException {
        try {
            FileInputStream inputStream = new FileInputStream(excelPath);
            XSSFWorkbook workbook= new XSSFWorkbook(inputStream);
            sheet=workbook.getSheetAt(0);

        } catch (IOException e){
            logger.error("Echec de l'importation du fichier Excel. "+e);
            throw new ImportException("Fichier Excel Introuvable.");
        }
    }
    public List<List<Object>> readExcel(XSSFSheet psheet) throws ImportException {
        List<List<Object>> exportedRows = new ArrayList<>();
        int cols = psheet.getRow(psheet.getFirstRowNum()).getLastCellNum();
        int rows=psheet.getPhysicalNumberOfRows();
        if (cols != 6 || !checkHeaderStruc(psheet.getRow(psheet.getFirstRowNum()))){
            logger.error("Fichier Excel importé inapproprié");
            throw new ImportException("Fichier Excel inapproprié");
        }
        for (int r=1;r<=rows;r++){
            XSSFRow row =sheet.getRow(r);
            if (checkEmptyRow(row)){
                continue;
            }
            List<Object> dataRow = new ArrayList<>();
            for(int c=0;c<cols;c++){
                Object cellVal;
                    try{
                        XSSFCell cell = row.getCell(c);
                        if (c==0) {
                            cellVal = (long) cell.getNumericCellValue();
                        } else if (c==1) {
                            cellVal = cell.getStringCellValue();
                        } else if (c==2) {
                            cellVal =cell.getStringCellValue();
                        } else if (c==3) {
                            cellVal =cell.getStringCellValue();
                        } else if (c==4) {
                            cellVal =(int) cell.getNumericCellValue();
                        }
                        else{
                            cellVal =cell.getStringCellValue();
                        }
                        dataRow.add(cellVal);
                    } catch (Exception e){
                        logger.error("Fichier Excel importé inapproprié");
                        throw new ImportException("Fichier Excel inapproprié");
                    }
                }
            exportedRows.add(dataRow);
            }
        return exportedRows;
        }

    private boolean checkHeaderStruc(XSSFRow row) {
        boolean status = true;
        int lastcellnum = row.getLastCellNum();
        for (int c=0; c< lastcellnum; c++){
            XSSFCell cell = row.getCell(c);
            if (c==0) {
                status = (cell.getStringCellValue().equals("ID ETUDIANT"));
                if(!status){
                    break;
                }
            } else if (c==1) {
                status = (cell.getStringCellValue().equals("CNE"));
                if(!status){
                    break;
                }
            } else if (c==2) {
                status = (cell.getStringCellValue().equals("NOM"));
                if(!status){
                    break;
                }
            }else if (c==3){
                status = (cell.getStringCellValue().equals("PRENOM"));
                if(!status){
                    break;
                }
            } else if (c==4) {
                status = (cell.getStringCellValue().equals("ID NIVEAU ACTUEL"));
                if(!status){
                    break;
                }
            }
            else {
                status = (cell.getStringCellValue().equals("TYPE"));
            }
        }
        return status;
    }

    private boolean checkEmptyRow(XSSFRow row) {
        if (row == null) {
            return true;
        }
        int lastCellNum = row.getLastCellNum();
        for (int c = 0; c < lastCellNum; c++) {
            XSSFCell cell = row.getCell(c);
            if (cell != null) {
                return false;
            }
        }
        return true;
    }

    public XSSFSheet getSheet() {
        return sheet;
    }
}
