package org.mealsApp;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.io.*;

public class PrintPdf {

    private JTable table;

    public PrintPdf(JTable table) throws DocumentException, FileNotFoundException, IOException {
        this.table = table;
    }

    public void createDocument() {
        try {

            //Δημιουργία αντικειμένου Document
            Document document = new Document();

            //Δημιουργία αντικειμένου OutputStream
            OutputStream outputStream =
                    new FileOutputStream(new File(System.getProperty("user.dir") + "/file.pdf"));

            //Δημιουργία αντικειμένου PDFWriter
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            //προσθήκη αριθμών στο τέλος των σελίδων
            CustomHeaderAndFooter event = new CustomHeaderAndFooter();
            writer.setPageEvent(event);

            //Άνοιγμα του Document
            document.open();
            event.onEndPage(writer, document);
            //Προσθήκη κενού 20 μονάδων
            Paragraph spacer = new Paragraph(" ");
            spacer.setSpacingBefore(20f);
            document.add(spacer);
            //Μορφοποίηση κειμένου του pdf
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 26, Font.BOLD);
            Paragraph title = new Paragraph("MealsApp statistics table", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(spacer);

            //Δημιουργία ενός αντικειμένου PdfTable με ίδιο αριθμό στηλών όπως το JTable
            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());

            //Προσθήκη επικεφαλίδας στήλης στον πίνακα
            for (int i = 0; i < table.getColumnCount(); i++) {
                PdfPCell cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setPhrase(new com.itextpdf.text.Phrase(table.getColumnName(i)));
                pdfTable.addCell(cell);
            }

            //Προσθήκη των εγγραφών στον πίνακα
            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    PdfPCell cell = new PdfPCell();
                    if (j == 1) {
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    }
                    cell.setPhrase(new com.itextpdf.text.Phrase((table.getValueAt(i, j)).toString()));
                    pdfTable.addCell(cell);
                }
            }

            //Προσθήκη του πίνακα στο Document PDF
            document.add(pdfTable);

            //Κλείσιμο των αντικειμενών document και outputStream μετά την ολοκλήρωση χρήσης τους.
            document.close();
            outputStream.close();
            //Μήνυμα επιτυχούς δημιουργία του pdf
            System.out.println("Pdf created successfully.");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


}
