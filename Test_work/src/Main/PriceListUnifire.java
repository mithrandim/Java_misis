package Main;

import java.io.IOException;

import java.util.*;

import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class PriceListUnifire
{
    // Карта двумерных массивов, чтобы хранить только уникальные значения
    // Двумерный массив хранит значение ячейки и тип этого значения
    private final Map <String, String[][]> priceListData = new HashMap<>();
    // Массив ключей (хэшей)
    private final ArrayList<String> keys;
    private Font font;
    private CellStyle style;
    // Массив названий столбцов из исходных файлов
    private String[] rowNames;

    public PriceListUnifire(String[] paths) throws IOException
    {
        keys = new ArrayList<String>();
        for (String path : paths)
        {
            // Входной поток для .xlsx
            FileInputStream fileIS = new FileInputStream(new File(path));

            // Книга
            XSSFWorkbook wb = new XSSFWorkbook(fileIS);
            // Берем исходный стиль и шрифт
            font = wb.getFontAt((short) 0);
            style = wb.getCellStyleAt(0);
            // Лист. Так как в файле он один, то и тут будет один
            XSSFSheet sheet = wb.getSheetAt(0);

            // Итератор для строк
            Iterator<Row> rowIterator = sheet.iterator();
            // Первую строку с наименованиями столбцов сохраняем в отдельное поле
            // Это делается дважды, потому что таким образом реализовать быстрее всего
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            rowNames = new String[3];
            for (int j = 0; j < 3; ++j)
            {
                Cell cell = cellIterator.next();
                rowNames[j] = cell.getStringCellValue();
            }

            // Идем по остальным строкам
            while (rowIterator.hasNext())
            {
                row = rowIterator.next();

                // Итератор по всем ячейкам текущей строки
                cellIterator = row.cellIterator();

                // Первый столбец - артикулы, поэтому сохраним его отдельно
                Cell temp = cellIterator.next();

                String key = String.valueOf(temp.getNumericCellValue());
                // Если такой ключ уже есть, пропускаем итерацию
                if (priceListData.containsKey(key))
                    continue;
                else
                    {
                    keys.add(key);
                    String[][] rowValues = new String[2][2];
                    int k = 0;

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();

                        // ========== Поменять на getCellType() если используется POI 4.x
                        CellType cellType = cell.getCellTypeEnum();

                        switch (cellType)
                        {
                            case _NONE:
                                rowValues[k][0] = null;
                                rowValues[k][1] = "_NONE";
                                System.out.print(rowValues[k][0] + '\t' + rowValues[k][1]);
                                System.out.print('\n');
                                ++k;
                                break;
                            case NUMERIC:
                                rowValues[k][0] = String.valueOf(cell.getNumericCellValue());
                                rowValues[k][1] = "NUMERIC";
                                System.out.print(rowValues[k][0] + '\t' + rowValues[k][1]);
                                System.out.print('\n');
                                ++k;
                                break;
                            case STRING:
                                rowValues[k][0] = cell.getStringCellValue();
                                rowValues[k][1] = "STRING";
                                System.out.print(rowValues[k][0] + '\t' + rowValues[k][1]);
                                System.out.print('\n');
                                ++k;
                                break;
                                // Технические сообщения, можно отключить
                            case ERROR:
                                System.out.print("!");
                                System.out.print("\n");
                                break;
                        }
                    }
                    priceListData.put(key, rowValues);
                }
            }
        }
        // Стилем ячеек будет тот, что берем из последнего файла
        style.setFont(font);
    }

    // Метод для тестирования
    private void printData(String key)
    {
        String[][] temp = priceListData.get(key);
        for (int i = 0; i<temp.length; ++i)
        {
            System.out.println(temp[i][0] + '\t' + temp[i][1] + '\n');
        }
    }

    // Создание нового xlsx документа и запись в него
    public void writeToXLSX(String path) throws IOException
    {
        FileOutputStream fileOS = new FileOutputStream(path);

        SXSSFWorkbook wb = new SXSSFWorkbook(1000);
        SXSSFSheet sheet1 = wb.createSheet("Sheet 1");

        int rowIndex = 0;
        SXSSFRow row = null;
        SXSSFCell cell = null;

        row = sheet1.createRow(rowIndex++);

        // Записываем названия столбцов
        for (int k = 0; k<3; ++k)
        {
            cell = row.createCell(k, CellType.STRING);
            cell.setCellValue(rowNames[k]);
            cell.setCellStyle(style);
        }

        // Записываем всю остальную информацию
        for (int i = 0; i<priceListData.size(); ++i)
        {
            row = sheet1.createRow(rowIndex++);

            cell = row.createCell(0, CellType.NUMERIC);
            cell.setCellValue(Double.parseDouble(keys.get(i)));
            cell.setCellStyle(style);

            String[][] temp = priceListData.get(keys.get(i));

            for (int j=0; j<temp.length; ++j)
            {
                String type = temp[j][1];
                switch(type)
                {
                    case "NUMERIC":
                        cell = row.createCell(j+1, CellType.NUMERIC);
                        cell.setCellValue(Double.parseDouble(temp[j][0]));
                        break;
                    case "STRING":
                        cell = row.createCell(j+1, CellType.STRING);
                        cell.setCellValue(temp[j][0]);
                        break;
                }
                cell.setCellStyle(style);
            }
        }

        wb.write(fileOS);
        fileOS.flush();
        fileOS.close();
        wb.close();
    }
}

class Main
{
    public static void main(String args[]) throws IOException
    {
        String[] paths = {"List1.xlsx","List2.xlsx"};
        PriceListUnifire a = new PriceListUnifire(paths);
        a.writeToXLSX("result.xlsx");
    }
}
