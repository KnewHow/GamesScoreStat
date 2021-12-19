package split;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import split.Student;
import split.TargetData;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Split {
    public static void main(String[] args) throws Exception {
        String path = "src/main/resources/split/HW2_Plus.xls";
        String targetPath = "src/main/resources/split/HW2_Plus_result.xls";
        List<Student> studens = getStudentsFromFile(path);
        //getTargetData(studens.get(2));
        List<TargetData> targetDatas = getTargetDatas(studens);
        //System.out.println(targetDatas);
        writeToExcel(targetPath, targetDatas);
    }

    public static List<Student> getStudentsFromFile(String path) throws Exception {
        File file = new File(path);
        Workbook wb = Workbook.getWorkbook(file);
        int sheet_size = wb.getNumberOfSheets();
        ArrayList<Student> students = new ArrayList<Student>();
        for(int i = 0; i < sheet_size; ++i) {
            Sheet sheet = wb.getSheet(i);
            for(int j = 1; j < sheet.getRows(); ++j) {
                ArrayList<String> row = new ArrayList<String>();
                for(int k = 0; k < 5; ++k) {
                    String cellInfo = sheet.getCell(k, j).getContents();
                    row.add(cellInfo);
                }
                Student s = new Student();
                s.setUid(row.get(0));
                s.setFirstName(row.get(1));
                s.setFamilyName(row.get(2));
                s.setComment(row.get(3));
                //System.out.println("firstName:" + s.getFirstName());
                s.setScore(Integer.parseInt(row.get(4)));
                students.add(s);
            }
        }
        System.out.println("total record: " + students.size());
        return  students;
    }

    public static void writeToExcel(String path, List<TargetData> ds) throws Exception {
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
        row.createCell(cellId++).setCellValue("姓");
        row.createCell(cellId++).setCellValue("名");
        row.createCell(cellId++).setCellValue("基础分数");
        row.createCell(cellId++).setCellValue("提高分数");
        row.createCell(cellId++).setCellValue("总分");

        for(TargetData td: ds) {
            row = sheet.createRow(rowId++);
            cellId = 0;
            row.createCell(cellId++).setCellValue(td.getUid());
            row.createCell(cellId++).setCellValue(td.getFirstName());
            row.createCell(cellId++).setCellValue(td.getFamilyName());
            row.createCell(cellId++).setCellValue(td.getBase());
            row.createCell(cellId++).setCellValue(td.getImprove());
            row.createCell(cellId++).setCellValue(td.getTotal());

        }
        FileOutputStream out = new FileOutputStream(destFile);
        wb.write(out);
        out.close();


    }

    public static List<TargetData> getTargetDatas(List<Student> students) {
        ArrayList<TargetData> ds = new ArrayList<TargetData>();
        for(Student s: students) {
            TargetData data = getTargetData(s);
            ds.add(data);
        }
        return ds;
    }

    public static TargetData getTargetData(Student student) {
        TargetData data = new TargetData();
        data.setUid(student.getUid());
        data.setFamilyName(student.getFamilyName());
        data.setFirstName(student.getFirstName());
        data.setTotal(student.getScore());
        if(data.getTotal() == 0) {
            data.setBase(0);
            data.setImprove(0);
            return data;
        }
        List<Integer> scores = getBaseAndImprove(student.getComment());
        if(scores.size() == 2 && (scores.get(0) + scores.get(1) == data.getTotal())) {
            data.setBase(scores.get(0));
            data.setImprove(scores.get(1));
        } else {
            data.setBase(-100);
            data.setImprove(-100);
            printScoreErrorMessage(student);
        }
        return data;
    }

    public static List<Integer> getBaseAndImprove(String comment) {
        ArrayList<Integer> scores = new ArrayList<>();
        String[] comments = comment.split("\n");
        int base = 0;
        int improve = 0;
        for(String s: comments) {
            if(s.contains("[") && s.contains("]")) {
                int begin = s.indexOf("[");
                int end = s.indexOf("]") + 1;
                String scoreStr = s.substring(begin, end);
                int score = getNumberFromString(scoreStr);
                if(scoreStr.contains("提高项")) {
                    improve += score;
                } else {
                    base += score;
                }
            }
        }
        if(base != 0 || improve != 0) {
            scores.add(base);
            scores.add(improve);
        }
        return scores;
    }

    public static Integer getNumberFromString(String str) {
        Pattern integerPattern = Pattern.compile("-?\\d+");
        Matcher matcher = integerPattern.matcher(str);

        List<String> integerList = new ArrayList<>();
        while (matcher.find()) {
            integerList.add(matcher.group());
        }
        if(integerList.size() > 0) {
            return Integer.parseInt(integerList.get(0));
        } else {
            return 0;
        }
    }

    public static void printScoreErrorMessage(Student s) {
        System.out.println("calculate score error! user_id:" + s.getUid() + ", FirstName: " + s.getFirstName() + ", FamilyName: " + s.getFamilyName());
    }


}
