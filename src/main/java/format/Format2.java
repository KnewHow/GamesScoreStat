package format;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import split.Student;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Format2 {
    public static void main(String[] args) throws Exception {
        Map<Integer, List<Integer>> map = getStudents("src/main/resources/format/HW2.xls");
        writeToFile("src/main/resources/format/HW2_result.xls", map);
    }

    public static Map<Integer, List<Integer>> getStudents(String path) throws Exception {
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        File file = new File(path);
        Workbook wb = Workbook.getWorkbook(file);
        int sheet_size = wb.getNumberOfSheets();
        ArrayList<Student> students = new ArrayList<Student>();

        Sheet sheet = wb.getSheet(0);
        for(int j = 1; j < sheet.getRows(); ++j) {
            ArrayList<String> row = new ArrayList<String>();
            for(int k = 0; k < 3; ++k) {
                String cellInfo = sheet.getCell(k, j).getContents();
                row.add(cellInfo);
            }
            Integer key = Integer.parseInt(row.get(0));
            Integer base = Integer.parseInt(row.get(1));
            Integer bounds = Integer.parseInt(row.get(2));
            ArrayList<Integer> scores = new ArrayList<>();
            scores.add(base);
            scores.add(bounds);
            map.put(key, scores);
        }

        System.out.println("total record: " + map.size());
        return map;
    }



    public static void writeToFile(String path, Map<Integer, List<Integer>> map) throws Exception {
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
            Integer base = map.get(key).get(0);
            Integer bounds = map.get(key).get(1);
            row.createCell(cellId++).setCellValue(key);
            row.createCell(cellId++).setCellValue(base);
            row.createCell(cellId++).setCellValue(bounds);
            row.createCell(cellId++).setCellValue((base + bounds));

        }
        FileOutputStream out = new FileOutputStream(destFile);
        wb.write(out);
        out.close();
    }
}
