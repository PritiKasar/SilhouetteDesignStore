/**
 * Excel Utility – unified helper for reading/writing Excel-based test data.
 * Supports:
 *   - Conditional row extraction
 *   - Random term fetching
 *   - DataProvider sheet loading
 *   - Appending order records
 *   - Writing product data
 */
package com.mystore.utility;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.*;

public class ExcelUtility {

    private XSSFWorkbook workbook;
    private static final String FILE_PATH = System.getProperty("user.dir") + "/TestData/Orders.xlsx";

    /**
     * Loads Excel workbook from given file path.
     */
    public ExcelUtility(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            workbook = new XSSFWorkbook(fis);
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to load Excel file: " + e.getMessage(), e);
        }
    }

    /**
     * Fetches first row that matches a column/value condition.
     * Returns: Entire matching row as String[]
     */
    public String[] getRowDataByCondition(String sheetName, String columnName, String value) {
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null)
            throw new RuntimeException("❌ Sheet not found: " + sheetName);

        XSSFRow headerRow = sheet.getRow(0);
        if (headerRow == null)
            throw new RuntimeException("❌ Header row not found in sheet: " + sheetName);

        // Find column index
        int colIndex = -1;
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            XSSFCell cell = headerRow.getCell(i);
            if (cell != null && cell.getStringCellValue().trim().equalsIgnoreCase(columnName)) {
                colIndex = i;
                break;
            }
        }
        if (colIndex == -1)
            throw new RuntimeException("❌ Column not found: " + columnName);

        // Find matching row
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            XSSFRow row = sheet.getRow(r);
            if (row != null && row.getCell(colIndex) != null &&
                    row.getCell(colIndex).getStringCellValue().trim().equalsIgnoreCase(value)) {

                String[] rowData = new String[row.getLastCellNum()];
                for (int c = 0; c < row.getLastCellNum(); c++) {
                    XSSFCell cell = row.getCell(c);
                    rowData[c] = cell == null ? "" : getCellValueAsString(cell);
                }
                return rowData;
            }
        }
        return null;
    }

    /**
     * Returns random non-empty value from sheet’s first column.
     */
    public String getRandomValueFromColumn(String sheetName) {
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null)
            throw new RuntimeException("❌ Sheet not found: " + sheetName);

        List<String> values = new ArrayList<>();

        for (int r = 0; r <= sheet.getLastRowNum(); r++) {
            XSSFRow row = sheet.getRow(r);
            if (row != null && row.getCell(0) != null) {
                String val = row.getCell(0).toString().trim();
                if (!val.isEmpty())
                    values.add(val);
            }
        }

        if (values.isEmpty())
            throw new RuntimeException("⚠ No non-empty values found in sheet: " + sheetName);

        return values.get(new Random().nextInt(values.size()));
    }

    /**
     * Converts cell value to readable String safely.
     */
    private String getCellValueAsString(XSSFCell cell) {
        return new DataFormatter().formatCellValue(cell);
    }

    /**
     * Returns sheet data as 2D array (useful for TestNG @DataProvider)
     */
    public String[][] getSheetData(String sheetName) {
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null)
            throw new RuntimeException("❌ Sheet not found: " + sheetName);

        int rowCount = sheet.getPhysicalNumberOfRows();
        int colCount = sheet.getRow(0).getLastCellNum();

        String[][] data = new String[rowCount - 1][colCount];

        for (int i = 1; i < rowCount; i++) {
            XSSFRow row = sheet.getRow(i);
            for (int j = 0; j < colCount; j++) {
                XSSFCell cell = row.getCell(j);
                data[i - 1][j] = cell == null ? "" : getCellValueAsString(cell);
            }
        }
        return data;
    }

    /**
     * Writes product data list to an Excel sheet.
     */
    public static void writeProductData(String excelPath, String sheetName, List<String[]> dataToWrite) {
        try {
            File file = new File(excelPath);
            XSSFWorkbook workbook;
            Sheet sheet;

            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheet(sheetName);

                if (sheet == null)
                    sheet = workbook.createSheet(sheetName);

                fis.close();
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet(sheetName);
            }

            int lastRow = sheet.getLastRowNum() + 1;

            for (String[] rowData : dataToWrite) {
                Row row = sheet.createRow(lastRow++);
                for (int i = 0; i < rowData.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(rowData[i]);
                }
            }

            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);

            fos.close();
            workbook.close();

            System.out.println("✅ Product data written successfully!");

        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to write Excel file: " + e.getMessage(), e);
        }
    }

    /**
     * Appends an order ID + timestamp to Orders.xlsx
     */
    public static void appendOrderRecord(String orderId) {
        FileInputStream fis = null;
        Workbook workbook = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(FILE_PATH);
            workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            Row row = sheet.createRow(sheet.getLastRowNum() + 1);

            row.createCell(0).setCellValue(orderId);
            row.createCell(1).setCellValue(new Date().toString());

            fis.close(); // MUST CLOSE before writing

            fos = new FileOutputStream(FILE_PATH);
            workbook.write(fos);

            System.out.println("✅ Order ID appended: " + orderId);

        } catch (IOException e) {
            System.out.println("❌ Failed to write Orders.xlsx: " + e.getMessage());

        } finally {
            try {
                if (workbook != null) workbook.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                System.out.println("⚠ Failed to close stream: " + e.getMessage());
            }
        }
    }
}

