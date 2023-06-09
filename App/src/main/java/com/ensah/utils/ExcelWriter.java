//
//package org.example.utils;
//
//import org.apache.log4j.Logger;
//import org.example.bo.Etudiant;
//import org.example.dao.DBConnection;
//import org.example.dao.DataBaseException;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Scanner;
//
//import org.example.dao.*;
//import org.example.bo.Element;
//import org.example.bo.Module;
//
//
//public class ExcelWriter {
//
//
//
//    public static void main(String[] args) throws SQLException, DataBaseException {
//        // Create an instance of ExcelWriter
//        ExcelWriter excelWriter = new ExcelWriter();
//
//        // Create a Scanner object to read user input
//        Scanner scanner = new Scanner(System.in);
//
//        // Read the module name from the user
//        System.out.print("Enter the module name: ");
//        String moduleName = scanner.nextLine();
//
//        // Read the session from the user
//        System.out.print("Enter the session: ");
//        String session = scanner.nextLine();
//
//        // Call the writeExcelFile method with the user input
//        excelWriter.writeExcelFile(moduleName, session);
//
//        // Close the scanner
//        scanner.close();
//    }
//    public ExcelWriter() throws SQLException, DataBaseException {
//    }
//
//    Connection con = DBConnection.getInstance();
//
//
//
//
//    public void writeExcelFile(String moduleTitre, String sessionType) {
//        try {
//            ElementDao elementDAO = new ElementDao();
//            List<Element> elements = elementDAO.getElementsByModule(moduleTitre);
//            // Create a new workbook
//            Workbook workbook = new XSSFWorkbook();
//            Sheet sheet = workbook.createSheet("Data");
//
//            // Define the column names and headers
//            String[] columnNames = {
//                    "Module", "", "Semestre", "", "Année", "",
//            };
//
//            String[] columnNames2 = {
//                    "Enseignant", "", "Session", "", "Classe", "", "",
//            };
//
//            String[] columnHeaders = {
//                    "ID", "CNE", "NOM", "PRENOM",
//                    elements.get(0).getNom(), elements.get(1).getNom() , "Moyenne", "Validation"
//            };
//
//
//            // Create cell style for column headers
//            CellStyle headerStyle = workbook.createCellStyle();
//            Font headerFont = workbook.createFont();
//            headerFont.setBold(true);
//            headerStyle.setFont(headerFont);
//
//            // Write the first row
//            Row row1 = sheet.createRow(0);
//            int columnCount = 0;
//            for (String columnName : columnNames) {
//                createCell(row1, columnCount++, columnName, headerStyle);
//            }
//
//            // Write the second row
//            Row row2 = sheet.createRow(1);
//            columnCount = 0;
//            for (String columnName2 : columnNames2) {
//                createCell(row2, columnCount++, columnName2, headerStyle);
//            }
//
//            // Write the third row (empty row)
//            sheet.createRow(2);
//
//            // Write the fourth row
//            Row row4 = sheet.createRow(3);
//            columnCount = 0;
//            for (String columnHeader : columnHeaders) {
//                createCell(row4, columnCount++, columnHeader, headerStyle);
//            }
//
//            int dataRowNumber = 0;
//
//            // Retrieve the module by titre
//            ModuleDao moduleDAO = new ModuleDao();
//            Module module = moduleDAO.getModuleByTitre(moduleTitre);
//            // Retrieve alias by module titre
//            NiveauDao niveauDao= new NiveauDao();
//            String alias = niveauDao.getAliasByModuleTitre(moduleTitre);
//            // Retrieve student infos based on session type
//            EtudiantDao etudiantdao = new EtudiantDao();
//            List<Etudiant> etudiants;
//
//            if (sessionType.equalsIgnoreCase("rattrapage")) {
//                etudiants = etudiantdao.getStudentsByRatt(moduleTitre);
//            } else {
//                etudiants = etudiantdao.getStudentInfoByModuleTitre(moduleTitre);
//            }
//
//            if (module != null) {
//                // Create a data row for the module
//                int moduleColumnNumber = 1;
//
//                // Set the module title
//                createCell(row1, moduleColumnNumber, module.getTitre(), null);
//
//                // Set the session type
//                createCell(row2, 3, sessionType, null);
//                createCell(row2, 5, alias, null);
//                // Write the student information to the Excel file
//                int rowCount = 4;
//                for (Etudiant etudiant : etudiants) {
//                    Row dataRow = sheet.createRow(rowCount++);
//                    createCell(dataRow, 0, etudiant.getIdUtilisateur(), null);
//                    createCell(dataRow, 1, etudiant.getCne(), null);
//                    createCell(dataRow, 2, etudiant.getNom(), null);
//                    createCell(dataRow, 3, etudiant.getPrenom(), null);
//
//                    if (sessionType.equalsIgnoreCase("rattrapage")) {
//
//                        String formula;
//                        if (alias.equalsIgnoreCase("CP1") || alias.equalsIgnoreCase("CP2")) {
//                            formula = "IF(AVERAGE(E" + rowCount + ":F" + rowCount + ") >= 10, 10, AVERAGE(E" + rowCount + ":F" + rowCount + "))";
//                        } else {
//                            formula = "IF(AVERAGE(E" + rowCount + ":F" + rowCount + ") >= 12, 12, AVERAGE(E" + rowCount + ":F" + rowCount + "))";
//                        }
//                        setFormulaCell(dataRow, 6, formula);
//
//                        String validationResultFormula ="IF($G$" + rowCount + "<12, \"NV\", \"V\")";
//                        if (alias.equalsIgnoreCase("CP1") || alias.equalsIgnoreCase("CP2")) {
//                            validationResultFormula = "IF($G$" + rowCount + "<10, \"NV\", \"V\")";
//                        }
//                        setFormulaCell(dataRow, 7, validationResultFormula);
//
//                    } else {
//                        String formula = "AVERAGE(E" + rowCount + ":F" + rowCount + ")";
//                        setFormulaCell(dataRow, 6, formula);
//                        // Determine the validation result
//                        String validationResultFormula ="IF($G$" + rowCount + "<12, \"R\", \"V\")";
//                        if (alias.equalsIgnoreCase("CP1") || alias.equalsIgnoreCase("CP2")) {
//                            validationResultFormula = "IF($G$" + rowCount + "<10, \"R\", \"V\")";
//                        }
//                        setFormulaCell(dataRow, 7, validationResultFormula);
//
//                    }
//                    }
//
//
//            }
//
//            // Specify the file path where you want to save the Excel file
//            String filename = moduleTitre + "_" + sessionType + ".xlsx";
//            String filePath = "" + filename;
//
//            // Save the workbook to the specified file path
//            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
//                workbook.write(outputStream);
//                System.out.println("Excel file created successfully.");
//            } catch (IOException e) {
//                System.out.println("Error occurred while creating the Excel file.");
//                e.printStackTrace();
//            }
//        } catch (DataBaseException e) {
//            System.out.println("Error occurred while accessing the database.");
//            e.printStackTrace();
//        }
//    }
//
//
//    private void createCell(Row row, int columnCount, String value, CellStyle style) {
//        Cell cell = row.createCell(columnCount);
//        cell.setCellValue(value);
//        if (style != null) {
//            cell.setCellStyle(style);
//        }
//    }
//    private void createCell(Row row, int columnCount, long value, CellStyle style) {
//        Cell cell = row.createCell(columnCount);
//        cell.setCellValue(value);
//        if (style != null) {
//            cell.setCellStyle(style);
//        }
//    }
//    private void setFormulaCell(Row row, int columnCount, String formula) {
//        Cell cell = row.createCell(columnCount);
//        cell.setCellFormula(formula);
//    }
//
//
//
//}