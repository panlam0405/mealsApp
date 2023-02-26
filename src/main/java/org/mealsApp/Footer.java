package org.mealsApp;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Footer extends PdfPageEventHelper{

    private PdfTemplate headerTemplate, footerTemplate;
    private Font footerFont;

    public Footer() throws IOException, DocumentException {
        footerFont = new Font(BaseFont.createFont(), 10, Font.NORMAL, BaseColor.BLACK);
    }

    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();

        // Draw top line
        cb.moveTo(document.left(), document.top());
        cb.lineTo(document.right(), document.top());
        cb.stroke();

        // Print date in bold font on the right side
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = dateFormat.format(new Date());
        Phrase datePhrase = new Phrase(dateStr, new Font(Font.FontFamily.HELVETICA, 10));
        float dateWidth = datePhrase.getTotalLeading();
        float i = document.right() - dateWidth - document.rightMargin();
        float j = document.top() - 10;
        ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, datePhrase, i, j, 0);

        // Add Footer Line
        cb.setLineWidth(0.5f);
        cb.setGrayStroke(0.5f);
        cb.moveTo(document.left(), document.bottom() + 30);
        cb.lineTo(document.right(), document.bottom() + 30);
        cb.stroke();

        // Add page number
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
