package com.ensah.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelDelibReader {

    public static List<DelibResult> readDelibResults() throws IOException {
        List<DelibResult> delibResults = new ArrayList<>();

        FileInputStream fis = new FileInputStream("delib.xlsx");
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet("Deliberation");

        int lastRowIndex = sheet.getLastRowNum();
        Row row1 = sheet.getRow(1);
        Cell aliasClassCell = row1.getCell(1);
        int startRowIndex = 5;


        for (int rowIndex = startRowIndex; rowIndex <= lastRowIndex; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                Cell idEtudiantCell = row.getCell(0);
                Cell rangCell = row.getCell(row.getLastCellNum() - 1);
                Cell moyenneCell = row.getCell(row.getLastCellNum() - 2); // Assuming moyenne cell is the 3rd cell from the end

                if (idEtudiantCell != null && aliasClassCell != null && rangCell != null && moyenneCell != null) {
                    long idEtudiant = (long) idEtudiantCell.getNumericCellValue();
                    String aliasClass = aliasClassCell.getStringCellValue();
                    int rang = (int) rangCell.getNumericCellValue();
                    double moyenne = 0.0;

                    if (moyenneCell.getCellType() == CellType.NUMERIC) {
                        moyenne = moyenneCell.getNumericCellValue();
                    } else if (moyenneCell.getCellType() == CellType.STRING) {
                        try {
                            moyenne = Double.parseDouble(moyenneCell.getStringCellValue());
                        } catch (NumberFormatException e) {
                            e.getStackTrace();
                        }
                    }

                    DelibResult delibResult = new DelibResult(idEtudiant, aliasClass, rang, moyenne);
                    delibResults.add(delibResult);
                }
            }
        }

        workbook.close();
        fis.close();

        return delibResults;
    }


    public static class DelibResult {
        private long idEtudiant;
        private String aliasClass;
        private int rang;
        private double moyenne;

        public DelibResult(long idEtudiant, String aliasClass, int rang, double moyennen) {
            this.idEtudiant = idEtudiant;
            this.aliasClass = aliasClass;
            this.rang = rang;
            this.moyenne = moyenne;
        }

        public long getIdEtudiant() {
            return idEtudiant;
        }

        public String getAliasClass() {
            return aliasClass;
        }

        public int getRang() {
            return rang;
        }

        public double getMoyenne() {
            return moyenne;
        }

    }


}
