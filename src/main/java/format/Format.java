package format;

import combine_single.StudentS;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import split.Student;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

public class Format {
    public static void main(String[] args) throws Exception {
        Map<Integer, Integer> map = getStudents("src/main/resources/format/HW_final.xls");
        writeToFile("src/main/resources/format/HW_final_result.xls", map);
    }

    public static Map<Integer, Integer> getStudents(String path) throws Exception {
        HashMap<Integer, Integer> map = new HashMap<>();
        File file = new File(path);
        Workbook wb = Workbook.getWorkbook(file);
        int sheet_size = wb.getNumberOfSheets();
        ArrayList<Student> students = new ArrayList<Student>();

        Sheet sheet = wb.getSheet(0);
        for(int j = 1; j < sheet.getRows(); ++j) {
            ArrayList<String> row = new ArrayList<String>();
            for(int k = 0; k < 2; ++k) {
                String cellInfo = sheet.getCell(k, j).getContents();
                row.add(cellInfo);
            }
            Integer key = Integer.parseInt(row.get(0));
            Integer score = Integer.parseInt(row.get(1));
            map.put(key, score);
        }

        System.out.println("total record: " + map.size());
        return map;
    }



    public static Integer getNumberFromCell(org.apache.poi.ss.usermodel.Cell cell) {
        switch (cell.getCellType()) {
            case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
                return Integer.parseInt(cell.getStringCellValue());
            case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN:
                return 0;
            case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
                double numericCellValue = cell.getNumericCellValue();
                return Integer.valueOf((int) numericCellValue);
        }
        return 0;
    }

    public static void writeToFile(String path, Map<Integer, Integer> map) throws Exception {
        File destFile = new File(path);
        destFile.delete();
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();
        XSSFRow row;
        Cell cell;
        int rowId = 0;
        int cellId = 0;

        // 表头
        row = sheet.createRow(rowId++);
        row.createCell(cellId++).setCellValue("user_id");
        row.createCell(cellId++).setCellValue("基础分数");
        row.createCell(cellId++).setCellValue("提高分数");
        row.createCell(cellId++).setCellValue("总分");

        for(Integer key: map.keySet()) {
            row = sheet.createRow(rowId++);
            cellId = 0;
            row.createCell(cellId++).setCellValue(key);
            row.createCell(cellId++).setCellValue(map.get(key));
            row.createCell(cellId++).setCellValue(0);
            row.createCell(cellId++).setCellValue(map.get(key));

        }
        FileOutputStream out = new FileOutputStream(destFile);
        wb.write(out);
        out.close();
    }
}
