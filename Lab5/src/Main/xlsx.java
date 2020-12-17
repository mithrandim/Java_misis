package Main;

import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


class Main
{
    public static void main(String args[]) throws Exception
    {
        // Создание выходного потока
        FileOutputStream fileOS = new FileOutputStream("result.xlsx");

        // Создание книги
        SXSSFWorkbook wb = new SXSSFWorkbook(10000);

        // Создание Листов
        SXSSFSheet sheet1 = wb.createSheet("Sheet with text");
        SXSSFSheet sheet2 = wb.createSheet("Sheet with formula");

        // Создание пароля и установка его для листов
        String password = "123";
        sheet1.protectSheet(password);
        sheet2.protectSheet(password);

        // Установка ширины колонок
        sheet1.setColumnWidth(0, Math.round(40 * 256));
        sheet1.setColumnWidth(1, Math.round(10 * 256));

        // Шрифт для текста
        Font textFont = wb.createFont();
        textFont.setItalic(true);
        textFont.setFontName("Arial");
        textFont.setColor((short)52);
        textFont.setFontHeightInPoints((short)12);

        // Шрифт для чисел
        Font numbersFont = wb.createFont();
        numbersFont.setBold(true);
        numbersFont.setFontName("Arial");
        numbersFont.setColor((short)44);
        numbersFont.setFontHeightInPoints((short)14);

        // Стиль для текста
        CellStyle textStyle = wb.createCellStyle();
        textStyle.setWrapText(true);
        textStyle.setFont(textFont);

        // Стиль для чисел
        CellStyle numbersStyle = wb.createCellStyle();
        numbersStyle.setWrapText(false);
        numbersStyle.setFont(numbersFont);

        int rowIndex = 0;
        SXSSFRow row = null;
        SXSSFCell cell = null;

        // Запись на первый лист
        row = sheet1.createRow(rowIndex++);

        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Все лабораторные работы лежат на гитхабе: https://github.com/Martynov-D/Java_misis");
        cell.setCellStyle(textStyle);

        for (int i = 1; i < 5; ++i)
        {
            row = sheet1.createRow(rowIndex++);

            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(String.format("Пример длинного текста, находящегося на строке № %s, который нужно перенести", rowIndex));
            cell.setCellStyle(textStyle);

            cell = row.createCell(1, CellType.NUMERIC);
            cell.setCellValue(5 * Math.pow(rowIndex, 2));
            cell.setCellStyle(numbersStyle);
        }

        // Запись на второй лист
        rowIndex = 0;
        row = sheet2.createRow(rowIndex++);
        cell = row.createCell(0, CellType.FORMULA);
        cell.setCellFormula("SUM('Sheet with text'!B2:B5)");
        cell.setCellStyle(numbersStyle);

        // закрытие книги и выходного потока
        wb.write(fileOS);
        fileOS.flush();
        fileOS.close();
        wb.close();
    }
}

