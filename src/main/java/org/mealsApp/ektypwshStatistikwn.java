package org.mealsApp;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ektypwshStatistikwn extends JPanel {

    private JTabbedPane tabbedPane;
    private JTable table;


    public ektypwshStatistikwn(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        setSize(new Dimension(900, 850));
        setBackground(new Color(83, 83, 83));
        setLayout(new BorderLayout());

    }

    public void removePanel() {
        tabbedPane.remove(this);
    }

    public void openStatisticsChart() {
        DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        List<Views> selectViews = em.createNamedQuery("Views.findAll", Views.class).getResultList();
        System.out.println(selectViews);

        Collections.sort(selectViews, new Comparator<Views>() {
            @Override
            public int compare(Views v1, Views v2) {
                return Integer.compare(v2.getViews(), (v1.getViews()));
            }
        });
        for (Views v : selectViews) {

            categoryDataset.setValue(v.getViews(), v.getViews(), v.getMeal());
        }

        em.getTransaction().commit();
        em.close();
        emf.close();

        JFreeChart chart = ChartFactory.createBarChart("Στατιστικά Αναζητήσεων",
                "Όνομα Γεύματος",
                "Πλήθος Αναζητήσεων",
                categoryDataset);

        CategoryPlot p = chart.getCategoryPlot();
        p.setRangeGridlinePaint(Color.BLACK);
        ChartPanel cp = new ChartPanel(chart);

        String[] columnNames = {"Meal", "Views"};
        Object[][] data = new Object[selectViews.size()][columnNames.length];
        int i = 0;
        for (Views view : selectViews) {
            String mName = view.getMeal();
            int mViews = view.getViews();
            data[i] = new Object[]{mName, mViews};
            i++;
        }
        table = new JTable(data, columnNames);
        table.setVisible(true);
        table.setFillsViewportHeight(true);

        JPanel statistics = new JPanel();
        statistics.setPreferredSize(new Dimension(getWidth() - 150, getHeight()));
        JScrollPane container = new JScrollPane(table);
        container.setPreferredSize(new Dimension(getWidth() - 100, getHeight()));
        JPanel buttonsPanel = new JPanel();


        statistics.setLayout(new BorderLayout());
        statistics.add(cp, BorderLayout.SOUTH);
        statistics.add(container, BorderLayout.CENTER);
        add(statistics, BorderLayout.CENTER);
        tabbedPane.setVisible(true);


        JButton pdfButton = new JButton("Εκτύπωση Στατιστικών (pdf)");

        buttonsPanel.add(pdfButton);

        JButton closeButton = new JButton("Κλείσιμο");

        buttonsPanel.add(closeButton);
        add(buttonsPanel, BorderLayout.PAGE_END);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removePanel();
            }
        });


        pdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //Create Document instance.
                    Document document = new Document();

                    //Create OutputStream instance.
                    OutputStream outputStream =
                            new FileOutputStream(new File(System.getProperty("user.dir") + "/file.pdf"));

                    //Create PDFWriter instance.
                    PdfWriter writer = PdfWriter.getInstance(document, outputStream);

//                    προσθήκη αριθμών στο τέλος των σελίδων
                    Footer event = new Footer();
                    writer.setPageEvent(event);

                    //Open the document.
                    document.open();
                    event.onEndPage(writer, document);
                    // Add a spacer of 20 units
                    Paragraph spacer = new Paragraph(" ");
                    spacer.setSpacingBefore(20f);
                    document.add(spacer);

                    Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 26, Font.BOLD);
                    Paragraph title = new Paragraph("MealsApp statistics table", titleFont);
                    title.setAlignment(Element.ALIGN_CENTER);
                    document.add(title);
                    document.add(spacer);

                    // create a PdfPTable object with the same number of columns as the JTable
                    PdfPTable pdfTable = new PdfPTable(table.getColumnCount());

                    // add the column headers to the table
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        PdfPCell cell = new PdfPCell();
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell.setPhrase(new com.itextpdf.text.Phrase(table.getColumnName(i)));
                        pdfTable.addCell(cell);
                    }

                    // add the data rows to the table
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

                    // add the table to the PDF document
                    document.add(pdfTable);
//
//                    // Create a new page for the chart image
//                    document.newPage();
////                    writer.getPageNumber()
//                    document.setPageSize(PageSize.A4.rotate());
//                    System.out.println(writer.getCurrentPageNumber());
//
//
//                    PdfContentByte cb = writer.getDirectContent();
//                    PdfTemplate chartTemplate = cb.createTemplate(200, 200);
//                    java.awt.Image awtImage = chart.createBufferedImage(200, 200);
//                    Image chartImage = Image.getInstance(writer, awtImage, 1);
//                    chartImage.setAbsolutePosition(50, 50);
//                    chartTemplate.addImage(chartImage);
//                    cb.addTemplate(chartTemplate, 0, 0);

                    //Close document and outputStream.
                    document.close();
                    outputStream.close();

                    System.out.println("Pdf created successfully.");
                } catch (NullPointerException exception) {
                    exception.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }


}
