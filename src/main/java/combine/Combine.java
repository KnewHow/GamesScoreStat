package combine;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import split.TargetData;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Combine {
    public static void main(String[] args) throws Exception {
        List<StudentS> s1 = getStudents("src/main/resources/combine/HW2_result.xls");
        List<StudentS> s2 = getStudents("src/main/resources/combine/HW2_Plus_result.xls");
        System.out.println("sum size: " + (s1.size() + s2.size()));
        List<StudentS> r = doCombine(s1, s2);
        System.out.println("totalSize: " + r.size());
        writeToFile("src/main/resources/combine/HW2.xls", r);
    }

    public static List<StudentS> getStudents(String path) throws Exception {
        ArrayList<StudentS> students = new ArrayList<StudentS>();
        File file = new File(path);
        XSSFWorkbook wb = new XSSFWorkbook(file);
        XSSFSheet sheet = wb.getSheetAt(0);
        Iterator<Row> iterator = sheet.iterator();
        iterator.next(); // jump title
        while (iterator.hasNext()) {
            Row row = iterator.next();
            StudentS s = new StudentS();
            s.uid = row.getCell(0).getStringCellValue();
            org.apache.poi.ss.usermodel.Cell c1 = row.getCell(1);

            s.base = getNumberFromCell(row.getCell(1));
            s.improve = getNumberFromCell(row.getCell(2));
            s.total = getNumberFromCell(row.getCell(3));
            students.add(s);
        }
        System.out.println("total record: " + students.size());
        return students;
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

    public static void writeToFile(String path, List<StudentS> ss) throws Exception {
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

        for(StudentS s: ss) {
            row = sheet.createRow(rowId++);
            cellId = 0;
            row.createCell(cellId++).setCellValue(s.uid);
            row.createCell(cellId++).setCellValue(s.base);
            row.createCell(cellId++).setCellValue(s.improve);
            row.createCell(cellId++).setCellValue(s.total);

        }
        FileOutputStream out = new FileOutputStream(destFile);
        wb.write(out);
        out.close();
    }

    public static List<StudentS> doCombine(List<StudentS> s1, List<StudentS> s2) {
        ArrayList<StudentS> sr = new ArrayList<StudentS>();
        HashMap<String, StudentS> m1 = toMap(s1);
        HashMap<String, StudentS> m2 = toMap(s2);
        for(String key: m1.keySet()) {
            if(m2.containsKey(key)) {
                System.out.println("common user_id: " + key);
                StudentS t1 = m1.get(key);
                StudentS t2 = m2.get(key);
                if(t1.total > t2.total) {
                    sr.add(t1);
                } else {
                    sr.add(t2);
                }
                m2.remove(key);
            } else {
                sr.add(m1.get(key));
            }
        }
        for(String key: m2.keySet()) {
            sr.add(m2.get(key));
        }
        return sr;
    }

    public static HashMap<String, StudentS> toMap(List<StudentS> ss) {
        HashMap<String, StudentS> map = new HashMap<String, StudentS>();
        for(StudentS s: ss) {
            map.put(s.uid, s);
        }
        return map;
    }
}
