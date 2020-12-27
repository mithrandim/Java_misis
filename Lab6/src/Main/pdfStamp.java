package Main;



class Main
{

    public static void main(String[] args)
    {
        PdfReader PDFReader = new PdfReader("C:\\file.pdf");

        FileStream Stream = new FileStream("C:\\new.pdf", FileMode.Create, FileAccess.Write);

        PdfStamper PDFStamper = new PdfStamper(PDFReader, Stream);

        for (int iCount = 0; iCount < PDFStamper.Reader.NumberOfPages; iCount++)
        {
            PdfContentByte PDFData = PDFStamper.GetOverContent(iCount + 1);
            BaseFont baseFont = BaseFont.CreateFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
            PDFData.BeginText();
            PDFData.SetColorFill(CMYKColor.LIGHT_GRAY);
            PDFData.SetFontAndSize(baseFont, 80);
            PDFData.ShowTextAligned(PdfContentByte.ALIGN_CENTER, "SAMPLE DOCUMENT", 300, 400, 45);
            PDFData.EndText();
        }

        Stream.Close();
        PDFReader.Close();
        PDFStamper.Close();
    }
}