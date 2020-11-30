package main;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;


public class pdf
{
    public static void main(String[] args)
    {
        File file = new File("sample.pdf");

        try
        {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            PDFParser parser = new PDFParser(randomAccessFile);
            parser.parse();
            COSDocument cosDoc = parser.getDocument();
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(1);
            PDDocument pdf = new PDDocument(cosDoc);
            String text = stripper.getText(pdf);

            FileWriter writer = new FileWriter("pdf_text.txt", false);
            writer.write(text);
            writer.flush();
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
