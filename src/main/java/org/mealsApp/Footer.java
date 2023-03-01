package org.mealsApp;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


//Δημιουργία κλάσης για τη μορφοποίηση του pdf. Επεκτείνει την κλάση PdfPageEventHelper
public class Footer extends PdfPageEventHelper{

    //Δημιουργία πεδίου για το τύπο γραμματοσειράς
    private Font footerFont;

    //Δημιουργία construtor
    public Footer() throws IOException, DocumentException {
        footerFont = new Font(BaseFont.createFont(), 10, Font.NORMAL, BaseColor.BLACK);
    }

    //Κατασκευή κλάσης onEndPage για τη μορφοποίηση σελίδας pdf. Η έξοδος είναι κενή. Έχει ως είσοδο το
    //PdfWriter και Document αντικείμενα.
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();

        //Δημιουργία γραμμής στο πάνω μέρος της σελίδας
        cb.moveTo(document.left(), document.top());
        cb.lineTo(document.right(), document.top());
        cb.stroke();

        //Εισαγωγή ημερομηνίας με bold συμβολοσειρά στο δεξί μέρος της σελίδας
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = dateFormat.format(new Date());
        Phrase datePhrase = new Phrase(dateStr, new Font(Font.FontFamily.HELVETICA, 10));
        float dateWidth = datePhrase.getTotalLeading();
        float i = document.right() - dateWidth - document.rightMargin();
        float j = document.top() - 10;
        ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, datePhrase, i, j, 0);

        //Εισαγωγή γραμμής στο κάτω μέρος της σελίδας
        cb.setLineWidth(0.5f);
        cb.setGrayStroke(0.5f);
        cb.moveTo(document.left(), document.bottom() + 30);
        cb.lineTo(document.right(), document.bottom() + 30);
        cb.stroke();

        //Εισαγωγή αρίθμησης σελίδας
        Phrase pageNumber = new Phrase(String.format("Page %d", writer.getPageNumber()), footerFont);
        Rectangle pageSize = document.getPageSize();
        float textWidth = footerFont.getBaseFont().getWidthPoint(pageNumber.getContent(), footerFont.getSize());
        float x = document.leftMargin() + (pageSize.getWidth() - document.leftMargin() - document.rightMargin() - textWidth) / 2;
        float y = document.bottom() + 10;
        cb.beginText();
        cb.setFontAndSize(footerFont.getBaseFont(), footerFont.getSize());
        cb.showTextAligned(Element.ALIGN_LEFT, pageNumber.toString(), x, y, 0);
        cb.endText();
    }
}
