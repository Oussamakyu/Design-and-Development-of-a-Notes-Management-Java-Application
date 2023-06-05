package com.ensah.utils;



import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;


import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExcelDelibExport {
    private static String[] row1 = {"Année\nUniversitaire","Date\ndéliberation"};
    private static String[] row2 = {"Année Universitaire","Date déliberation"};
    private static String[] row4 = {"ID\nEtudiant","CNE","NOM","PRENOM"};
    private XSSFWorkbook workbook;
    public ExcelDelibExport(String classe,String annee,List<List> pModules,List<List> pStudentInfos) throws IOException {
        workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Deliberation");

        XSSFCellStyle cellStyle0 = workbook.createCellStyle();
        cellStyle0.setBorderBottom(BorderStyle.THIN);
        cellStyle0.setBorderTop(BorderStyle.THIN);
        cellStyle0.setBorderLeft(BorderStyle.THIN);
        cellStyle0.setBorderRight(BorderStyle.THIN);
        cellStyle0.setAlignment(HorizontalAlignment.CENTER);
        cellStyle0.setVerticalAlignment(VerticalAlignment.TOP);

        XSSFCellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.CENTER);
        cellStyle1.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyle1.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle1.setBorderBottom(BorderStyle.THIN);
        cellStyle1.setBorderTop(BorderStyle.THIN);
        cellStyle1.setBorderLeft(BorderStyle.THIN);
        cellStyle1.setBorderRight(BorderStyle.THIN);


        XSSFCellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.CENTER);
        cellStyle2.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyle2.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle2.setBorderBottom(BorderStyle.THIN);
        cellStyle2.setBorderTop(BorderStyle.THIN);
        cellStyle2.setBorderLeft(BorderStyle.THIN);
        cellStyle2.setBorderRight(BorderStyle.THIN);

        XSSFCellStyle cellStyle3 = workbook.createCellStyle();
        cellStyle3.setAlignment(HorizontalAlignment.CENTER);
        cellStyle3.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyle3.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
        cellStyle3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle3.setBorderBottom(BorderStyle.THIN);
        cellStyle3.setBorderTop(BorderStyle.THIN);
        cellStyle3.setBorderLeft(BorderStyle.THIN);
        cellStyle3.setBorderRight(BorderStyle.THIN);


        XSSFRow headerRow1 = sheet.createRow(0);
        XSSFRow headerRow2 = sheet.createRow(1);
        XSSFRow coloredRow = sheet.createRow(2);
        XSSFRow headerRow4 = sheet.createRow(3);
        XSSFRow elementsHeaders = sheet.createRow(4);

        XSSFCell anneeCell = headerRow1.createCell(1);
        anneeCell.setCellValue(annee);
        anneeCell.setCellStyle(cellStyle0);

        XSSFCell dateDelibCell = headerRow1.createCell(3);
        dateDelibCell.setCellValue(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dateDelibCell.setCellStyle(cellStyle0);

        XSSFCell classeCell = headerRow2.createCell(1);
        classeCell.setCellValue(classe);
        classeCell.setCellStyle(cellStyle0);

        XSSFCell classeCelllb = headerRow2.createCell(0);
        classeCelllb.setCellValue("Classe");
        classeCelllb.setCellStyle(cellStyle1);

        for (int i=0,j=0;i<row1.length;i++,j+=2){
            XSSFCell cell = headerRow1.createCell(j);
            cell.setCellValue(row1[i]);
            cell.setCellStyle(cellStyle1);
        }


        for(int i=0;i<row4.length;i++){
            XSSFCell cell = headerRow4.createCell(i);
            XSSFCell blankcell = elementsHeaders.createCell(i);
            cell.setCellValue(row4[i]);
            cell.setCellStyle(cellStyle2);
            blankcell.setCellStyle(cellStyle2);
            XSSFCell blank2cell = coloredRow.createCell(i);
            blank2cell.setCellStyle(cellStyle3);
        }

        List<XSSFRow> studentsRow = new ArrayList<>();
        for(int i=0,j=5;i<pStudentInfos.size() && j<pStudentInfos.size()+5;i+=2,j++){
            XSSFRow studentData = sheet.createRow(j);
            studentsRow.add(studentData);
            for(int k=0;k<4;k++){
                XSSFCell cell = studentData.createCell(k);
                if (pStudentInfos.get(i).get(k) instanceof Integer){
                    cell.setCellValue((Integer) pStudentInfos.get(i).get(k));
                }else{
                    cell.setCellValue((String) pStudentInfos.get(i).get(k));
                }
                cell.setCellStyle(cellStyle0);
            }
        }

        List<List> StudentsFinalNotes = new ArrayList<>();
        for(int i=1;i<pStudentInfos.size();i+=2){
            StudentsFinalNotes.add(pStudentInfos.get(i));
        }
        List<String> allMoyGenRefCell = new ArrayList<>();
        List<XSSFCell> allRangCell = new ArrayList<>();
        for(int i=0;i<studentsRow.size();i++){
            List<String> allMoyRefer = new ArrayList<>();
            int indicenote =0;
            int k = 4;
                for(int e=0;e< pModules.size();e++){
                    if(pModules.get(e).size() == 1){
                        XSSFCell markcell = studentsRow.get(i).createCell(k);
                        String refernote = markcell.getReference();
                        XSSFCell validationcell = studentsRow.get(i).createCell(k+1);
                        markcell.setCellValue(Double.parseDouble(StudentsFinalNotes.get(i).get(indicenote).toString()));
                        allMoyRefer.add(markcell.getReference());
                        markcell.setCellStyle(cellStyle0);
                        validationcell.setCellFormula("IF("+refernote+"<10,\"NV\",\"V\")");
                        validationcell.setCellStyle(cellStyle0);
                        indicenote +=1;
                        k= k+2;
                    }
                    else{
                        int note=indicenote;
                        List<String> refer = new ArrayList<>();
                        while(indicenote<=note+pModules.get(e).size()-2){
                            XSSFCell markcell2 = studentsRow.get(i).createCell(k);
                            markcell2.setCellValue(Double.parseDouble(StudentsFinalNotes.get(i).get(indicenote).toString()));
                            refer.add(markcell2.getReference());
                            markcell2.setCellStyle(cellStyle0);
                            indicenote +=1;
                            k=k+1;
                        }
                        XSSFCell moyElementcell = studentsRow.get(i).createCell(k);
                        String refermoy = moyElementcell.getReference();
                        XSSFCell valicell = studentsRow.get(i).createCell(k+1);
                        moyElementcell.setCellFormula(createMeanFormula(refer));
                        moyElementcell.setCellStyle(cellStyle0);
                        allMoyRefer.add(moyElementcell.getReference());
                        valicell.setCellFormula("IF("+refermoy+"<10,\"NV\",\"V\")");
                        valicell.setCellStyle(cellStyle0);
                        k=k+2;
                    }
                }
                XSSFCell genMoyen = studentsRow.get(i).createCell(k);
                genMoyen.setCellStyle(cellStyle0);
                allMoyGenRefCell.add(genMoyen.getReference());
                XSSFCell rang = studentsRow.get(i).createCell(k+1);
                allRangCell.add(rang);
                rang.setCellStyle(cellStyle0);
                genMoyen.setCellFormula(createMeanFormula(allMoyRefer));
        }
        for (int i=0;i<allMoyGenRefCell.size();i++){
            allRangCell.get(i).setCellFormula(createRankFormula(allMoyGenRefCell.get(i),allMoyGenRefCell.get(0),allMoyGenRefCell.get(allMoyGenRefCell.size() -1)));
        }

        int i,j;
        for(i=0,j=4;i<pModules.size();i++){
            if(pModules.get(i).size() == 1){
                XSSFCell cell1 = headerRow4.createCell(j);
                XSSFCell cell2 = headerRow4.createCell(j+1);
                cell1.setCellValue((String) pModules.get(i).get(0));
                cell1.setCellStyle(cellStyle2);
                cell2.setCellStyle(cellStyle2);
                XSSFCell cellmoyen = elementsHeaders.createCell(j);
                XSSFCell validationmoyen = elementsHeaders.createCell(j+1);
                cellmoyen.setCellValue("Moyenne");
                cellmoyen.setCellStyle(cellStyle2);
                validationmoyen.setCellValue("Validation");
                validationmoyen.setCellStyle(cellStyle2);
                XSSFCell blank2cell = coloredRow.createCell(j);
                blank2cell.setCellStyle(cellStyle3);
                sheet.addMergedRegion(new CellRangeAddress(0,0,j,j+1));
                sheet.addMergedRegion(new CellRangeAddress(1,1,j,j+1));
                sheet.addMergedRegion(new CellRangeAddress(2,2,j,j+1));
                sheet.addMergedRegion(new CellRangeAddress(3,3,j,j+1));
                j+=pModules.get(i).size()+1;

            }else{
                for(int k=j,c=0;k<j+pModules.get(i).size()+1;k++,c++){
                    XSSFCell cell = headerRow4.createCell(k);
                    for(int n=1,cellnum=k;n<pModules.get(i).size();n++,cellnum++){
                        if (c == 0){
                            XSSFCell blank2cell = coloredRow.createCell(j);
                            blank2cell.setCellStyle(cellStyle3);
                            XSSFCell elementCell = elementsHeaders.createCell(cellnum);
                            elementCell.setCellValue((String) pModules.get(i).get(n));
                            elementCell.setCellStyle(cellStyle2);
                        }
                    }
                    cell.setCellStyle(cellStyle2);
                }
                XSSFCell cellmoyen = elementsHeaders.createCell(j+pModules.get(i).size()-1);
                cellmoyen.setCellValue("Moyen");
                cellmoyen.setCellStyle(cellStyle2);
                XSSFCell cellvalidation = elementsHeaders.createCell(j+pModules.get(i).size());
                cellvalidation.setCellValue("Validation");
                cellvalidation.setCellStyle(cellStyle2);
                XSSFCell cell = headerRow4.createCell(j);
                cell.setCellValue((String) pModules.get(i).get(0));
                cell.setCellStyle(cellStyle2);
                sheet.addMergedRegion(new CellRangeAddress(0,0,j,j+pModules.get(i).size()));
                sheet.addMergedRegion(new CellRangeAddress(1,1,j,j+pModules.get(i).size()));
                sheet.addMergedRegion(new CellRangeAddress(2,2,j,j+pModules.get(i).size()));
                sheet.addMergedRegion(new CellRangeAddress(3,3,j,j+pModules.get(i).size()));
                j+=pModules.get(i).size()+1;
            }

        }
        XSSFCell cellMoy = headerRow4.createCell(j);
        XSSFCell celldownMoy = elementsHeaders.createCell(j);
        XSSFCell blank2cell = coloredRow.createCell(j);
        blank2cell.setCellStyle(cellStyle3);
        cellMoy.setCellValue("Moyenne");
        cellMoy.setCellStyle(cellStyle2);
        celldownMoy.setCellStyle(cellStyle2);
        XSSFCell cellRang = headerRow4.createCell(j+1);
        XSSFCell celldownRang = elementsHeaders.createCell(j+1);
        cellRang.setCellValue("Rang");
        cellRang.setCellStyle(cellStyle2);
        celldownRang.setCellStyle(cellStyle2);
        XSSFCell blank2cell2 = coloredRow.createCell(j+1);
        blank2cell2.setCellStyle(cellStyle3);

    }

    public void generate() throws IOException {
        FileOutputStream fileOut = new FileOutputStream("delib.xlsx");
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    private static String createMeanFormula(List<String> cellReferences) {
        StringBuilder formulaBuilder = new StringBuilder();
        formulaBuilder.append("AVERAGE(");
        for (String cellReference : cellReferences) {
            formulaBuilder.append(cellReference);
            formulaBuilder.append(",");
        }
        formulaBuilder.deleteCharAt(formulaBuilder.length() - 1);
        formulaBuilder.append(")");
        return formulaBuilder.toString();
    }

    private static String createRankFormula(String targetCellReference, String startCellReference, String endCellReference) {
        StringBuilder formulaBuilder = new StringBuilder();
        formulaBuilder.append("RANK(");
        formulaBuilder.append(targetCellReference);
        formulaBuilder.append(",");
        formulaBuilder.append(startCellReference);
        formulaBuilder.append(":");
        formulaBuilder.append(endCellReference);
        formulaBuilder.append(")");
        return formulaBuilder.toString();
    }
}
