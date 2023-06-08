package org.example.utils;

// @author Rim Lfellous

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.dao.*;

public class ExcelImporter {
    public static void main(String[] args) throws DataBaseException {
    Scanner scanner = new Scanner(System.in);

    // Read the module name from the user
    System.out.print("Enter the module name: ");
    String moduleName = scanner.nextLine();

    // Read the session from the user
    System.out.print("Enter the session: ");
    String session = scanner.nextLine();

    ExcelImporter excelImporter = new ExcelImporter();
    excelImporter.importDataFromExcel(moduleName + "_" + session + ".xlsx", moduleName,  session);



}

    public void importDataFromExcel(String filePath,String moduleName,  String session) {
        try (Connection con = DBConnection.getInstance();
             FileInputStream fis = new FileInputStream(new File(filePath));
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            // Get the sheet from the workbook
            // Assuming the sheet name is "Data"
            Sheet sheet = workbook.getSheet("Data");
            String ModuleTitre = getStringCellValue(sheet.getRow(0).getCell(1));


            // Iterate through each row in the sheet
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Assuming the data starts from the second row (index 1)
                if (row.getRowNum() > 3) {
                    // Retrieve the cell values from the row
                    Cell IDCell = row.getCell(0);
                    Cell moyenneCell = row.getCell(6);
                    Cell validationCell = row.getCell(7);

                    // Parse the cell values and insert into the database
                    long ID = getNumericCellValue(IDCell);
                    double moyenne = getNumericCellValue(moyenneCell);
                    String validation = getStringCellValue(validationCell);

                    // Insert the data into the database
                    if (session.equalsIgnoreCase("rattrapage"))
                        ModuleDao.insertDataIntoDatabaseSR(ModuleTitre, ID, moyenne, validation, con);
                    else {ModuleDao.insertDataIntoDatabase(ModuleTitre, ID, moyenne, validation, con);}
                }
                String element1 = getStringCellValue(sheet.getRow(3).getCell(4));
                String element2 = getStringCellValue(sheet.getRow(3).getCell(5));

                if (row.getRowNum() > 3) {
                    // Retrieve the cell values from the row
                    Cell IDCell = row.getCell(0);
                    Cell element1Cell = row.getCell(4);
                    Cell element2Cell = row.getCell(5);

                    long ID = getNumericCellValue(IDCell);
                    double e1 = getNumericCellValue(element1Cell);
                    double e2 = getNumericCellValue(element2Cell);

                    ElementDao element = new ElementDao();
                    NiveauDao niveauDao= new NiveauDao();
                    String alias = niveauDao.getAliasByModuleTitre(moduleName);

                    if (session.equalsIgnoreCase("rattrapage")){
                        element.insertDataintoDatabaseSR(element1, element2, ID, e1, e2, con);
                        element.getValidationResultDB(alias, ID, con);
                        element.getValidationResultDB(alias, ID, con);}

                    else {
                        element.insertDataintoDatabase(element1, element2, ID, e1, e2, con);
                        element.getValidationResultFromDB(alias, ID, con); // Call for element1
                        element.getValidationResultFromDB(alias, ID, con); // Call for element2
                        }
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (DataBaseException ex) {
            throw new RuntimeException(ex);
        }

        System.out.println("Data imported successfully.");
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }

    private long getNumericCellValue(Cell cell) {
        if (cell == null) {
            return (long) 0.0;
        }
        cell.setCellType(CellType.NUMERIC);
        return (long) cell.getNumericCellValue();
    }


}
