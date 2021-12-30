package combine_all;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import split.Student;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class CombineAll {
    public static void main(String[] args) throws Exception {
        ArrayList<Map<Integer, List<Integer>>> hws = new ArrayList<>();
        for(int i = 1; i <=9; ++i) {
            Map<Integer, List<Integer>> hw = loadHW(i);
            hws.add(hw);
        }
        Map<Integer, List<Integer>>  r = hws.get(0);
        for(int i = 1; i < hws.size(); ++i) {
            r = combine(r, hws.get(i));
        }

        writeFile("src/main/resources/combine_all/result.xls", r);
    }

    public static Map<Integer, List<Integer>> loadHW(int i) throws Exception {
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        String path = "src/main/resources/combine_all/HW" + i + ".xls";
        System.out.println(path);
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

    public static Map<Integer, List<Integer>> combine(Map<Integer, List<Integer>> base, Map<Integer, List<Integer>> newSubmit) {
        int scoreLength = 0;
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        for(Integer key: base.keySet()) {
            if(scoreLength == 0) {
                scoreLength = base.get(key).size();
            }

            List<Integer> newScores = newSubmit.get(key);
            ArrayList<Integer> scores = new ArrayList<>();
            scores.addAll(base.get(key));
            if(newSubmit.containsKey(key)) { // find newSubmit, combine them
                scores.add(newScores.get(0));
                scores.add(newScores.get(1));
                newSubmit.remove(key);
            } else { // not find, as zero
                scores.add(0);
                scores.add(0);
            }
            map.put(key, scores);
        }

        for(Integer key: newSubmit.keySet()) { // just newSubmit, set previous as zero
            ArrayList<Integer> scores = new ArrayList<>();
            for(int i = 0; i < scoreLength; ++i) {
                scores.add(0);
            }
            scores.addAll(newSubmit.get(key));
            map.put(key, scores);
        }
        return map;

    }

    public static void writeFile(String path, Map<Integer, List<Integer>> map) throws Exception {
        ArrayList<List<Integer>> results = new ArrayList<>();
        for(Integer key: map.keySet()) {
            ArrayList<Integer> row = new ArrayList<>();
            row.add(key);
            row.addAll(map.get(key));
            results.add(row);
        }
        results.sort(new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                return o1.get(0) - o2.get(0);
            }
        });
        File destFile = new File(path);
        destFile.delete();
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();
        XSSFRow row;
        Cell cell;
        int rowId = 0;
        int cellId = 0;
        final int HW_TOTAL_TIMES = 9;

        // 表头
        row = sheet.createRow(rowId++);
        row.createCell(cellId++).setCellValue("ID");
        for(int i = 1; i <= HW_TOTAL_TIMES; ++i) {
            if(i != 9) {
                if(i == 5 || i == 8) {
                    row.createCell(cellId++).setCellValue("HW" + i + "_base");
                } else {
                    row.createCell(cellId++).setCellValue("HW" + i + "_base");
                    row.createCell(cellId++).setCellValue("HW" + i + "_bonus");
                }

            } else {
                row.createCell(cellId++).setCellValue("HW" + "_final");
            }
        }
        row.createCell(cellId++).setCellValue("base_total");
        row.createCell(cellId++).setCellValue("bonus_total");
        row.createCell(cellId++).setCellValue("total");


        for(int i = 0; i < results.size(); ++i) {
            int index = 0;
            List<Integer> rowData = results.get(i);
            row = sheet.createRow(rowId++);
            cellId = 0;
            row.createCell(cellId++).setCellValue(rowData.get(index++));

            int base_total = 0;
            int bonus_total = 0;
            for(int j = 1; j <= HW_TOTAL_TIMES; ++j) {
                if(j == 5 || j == 8 || j == 9) {
                    int base = rowData.get(index);
                    row.createCell(cellId++).setCellValue(base);
                    base_total += base;
                    index += 2;
                } else {
                    int base = rowData.get(index++);
                    int bonus = rowData.get(index++);
                    row.createCell(cellId++).setCellValue(base);
                    row.createCell(cellId++).setCellValue(bonus);
                    base_total += base;
                    bonus_total += bonus;
                }
            }
            int total = base_total + bonus_total;
            row.createCell(cellId++).setCellValue(base_total);
            row.createCell(cellId++).setCellValue(bonus_total);
            row.createCell(cellId++).setCellValue(total);

        }

        FileOutputStream out = new FileOutputStream(destFile);
        wb.write(out);
        out.close();

    }
}
