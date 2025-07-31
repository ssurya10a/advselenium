package generateUtility;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.*;

public class WorkbookFileUtility {

    Workbook workbook;

    // Load the Excel file
    public void openExcel() throws IOException {
        FileInputStream fis = new FileInputStream("./src/test/resources/testdata.xlsx");
        workbook = WorkbookFactory.create(fis);
    }

    // Get cell value
    public String getCellValue(String sheetName, int rowNum, int colNum) {
        Sheet sheet = workbook.getSheet(sheetName);
        Row row = sheet.getRow(rowNum);
        if (row == null) return "";
        Cell cell = row.getCell(colNum);
        return (cell != null) ? cell.toString() : "";
    }

    // Get last used row
    public int getLastRow(String sheetName) {
        return workbook.getSheet(sheetName).getLastRowNum();
    }

    // Set cell value
    public void setCellValue(String sheetName, int rowNum, int colNum, String value) {
        Sheet sheet = workbook.getSheet(sheetName);
        Row row = sheet.getRow(rowNum);
        if (row == null) row = sheet.createRow(rowNum);
        Cell cell = row.getCell(colNum);
        if (cell == null) cell = row.createCell(colNum);
        cell.setCellValue(value);
    }

    // Save and close Excel
    public void saveAndClose() throws IOException {
        FileOutputStream fos = new FileOutputStream("./src/test/resources/testdata.xlsx");
        workbook.write(fos);
        workbook.close();
        fos.close();
    }
}
