package Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.util.zip.*;

public class Zip
{
    private static final int BUFFER_SIZE = 4096;

//    Метод, который заносит в zip контейнер файл
    private static void putFile(ZipOutputStream zipOS, File file) throws IOException
    {
//        Резервируем место в zip файле
        zipOS.putNextEntry(new ZipEntry(file.getName()));
//        Открываем входной поток
        BufferedInputStream bufferedIS = new BufferedInputStream(new FileInputStream(file));
//        Объявляем буфер
        byte[] bytesIn = new byte[BUFFER_SIZE];
//        long bytesRead = 0;
        int read = 0;
//        Заносим в буфер файл
        while ((read = bufferedIS.read(bytesIn)) != -1)
        {
//            И записываем его в zip контейнер
            zipOS.write(bytesIn, 0, read);
//            bytesRead += read;
        }
        zipOS.closeEntry();
    }

//    Метод, который заносит в zip контейнер папку и через рекурсию все остальное, что находилось в ней
    private static void putDirectory(ZipOutputStream zipOS, File folder, String parentFolder) throws IOException
    {
        for (File file : folder.listFiles())
        {
//            Если следующий "файл" - директория, то рекурсивно вызываем самого себя (метод)
            if (file.isDirectory())
            {
                putDirectory(zipOS, file, parentFolder + "/" + file.getName());
                continue;
            }
//            Иначе - просто добавляем новую директорию, используя буфер по аналогии с методом putFile
            zipOS.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));
            BufferedInputStream bufferIS = new BufferedInputStream(new FileInputStream(file));
            byte[] bytesIn = new byte[BUFFER_SIZE];
//            long bytesRead = 0;
            int read = 0;
            while ((read = bufferIS.read(bytesIn)) != -1)
            {
                zipOS.write(bytesIn, 0, read);
//                bytesRead += read;
            }
            zipOS.closeEntry();
        }
    }


    public static void putIntoZip(File dir, ZipOutputStream zipOS) throws IOException
    {
        for (File file : dir.listFiles())
        {
            if (file.isHidden())
            {
                System.out.println("Hidden file '" + file.getName() + "' was skipped");
                continue;
            }
            if (file.isDirectory())
            {
                putDirectory(zipOS, file, file.getName());
            }
            else
            {
                putFile(zipOS, file);
            }
        }
    }
}

class Main
{
    public static void main(String[] args) throws IOException
    {
//        if (args.length < 2)
//        {
//            System.out.println("Not enough arguments");
//            return;
//        }

//        Путь к исходному каталогу
        File folder = new File("samples");
//        Строчка для считывания из аргументов ->->->
//        File folder = new File(args[0]);

//        Открываем поток для записи
        ZipOutputStream zipOS = new ZipOutputStream(new FileOutputStream("zip_sample.zip"));
//        Строчка для считывания из аргументов ->->->
//        ZipOutputStream zipOS = new ZipOutputStream(new FileOutputStream(args[1]));

//        Ставим уровень сжатия 0, т.е. без сжатия
        zipOS.setLevel(0);

//        Метод putIntoZip класса Zip и будет заносить файлы и папки в zip контейнер
        try
        {
            Zip.putIntoZip(folder, zipOS);
            zipOS.finish();
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
