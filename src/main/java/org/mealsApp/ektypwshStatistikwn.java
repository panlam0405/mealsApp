package org.mealsApp;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ektypwshStatistikwn extends JPanel{
    private JPanel jp;

    private JTabbedPane tabbedPane;


    public ektypwshStatistikwn(JTabbedPane tabbedPane) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.tabbedPane = tabbedPane;
        setOpaque(false);
    }
    public void removePanel(){
        tabbedPane.remove(this);
    }

    public void openStatisticsChart() {
        DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        List<Views> selectViews = em.createNamedQuery("Views.findAll", Views.class).getResultList();

        Collections.sort(selectViews, new Comparator<Views>() {
            @Override
            public int compare(Views v1, Views v2) {
                return Integer.compare((v1.getViews()), v2.getViews());
            }
        });
        for (Views v : selectViews) {

            categoryDataset.setValue(v.getViews(), v.getViews(), v.getMeal());
        }

        em.getTransaction().commit();
        em.close();
        emf.close();
        JFreeChart chart = ChartFactory.createBarChart("Στατιστικά Αναζητήσεων", "Όναμα Γέυματος", "Πλήθος Αναζητήσεων", categoryDataset);

        CategoryPlot p = chart.getCategoryPlot();
        p.setRangeGridlinePaint(Color.BLACK);
        ChartPanel cp = new ChartPanel(chart);

        tabbedPane.add("Στατιστικά Αναζητήσεων", cp);
        tabbedPane.setVisible(true);


         JButton pdfButton = new JButton("ΕΚΤΥΠΩΣΗ ΣΤΑΤΙΣΤΙΚΩΝ");
         pdfButton.setBounds(440, 100, 200, 30);
         add(pdfButton);
         JButton closeButton = new JButton("Close");
         closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removePanel();
            }
        });

        closeButton.setBounds(100, 850, 100, 30);

         pdfButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 try {
                     //Create Document instance.
                     Document document = new Document();

                     //Create OutputStream instance.
                     OutputStream outputStream =
                             new FileOutputStream(new File("D:\\TestFile.pdf"));

                     //Create PDFWriter instance.
                     PdfWriter.getInstance(document, outputStream);

                     //Open the document.
                     document.open();


                     EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
                     EntityManager em = emf.createEntityManager();
                     em.getTransaction().begin();

                     List<Views> selectViews = em.createNamedQuery("Views.findAll", Views.class).getResultList();

                     Collections.sort(selectViews, new Comparator<Views>() {
                         @Override
                         public int compare(Views v1, Views v2) {
                             return Integer.compare((v1.getViews()), v2.getViews());
                         }
                     });
                     for (Views v : selectViews) {

                         categoryDataset.setValue(v.getViews(), v.getViews(), v.getMeal());
                     }

                     em.getTransaction().commit();
                     em.close();
                     emf.close();

                     //Add content to the document.
                     document.add(new Paragraph(String.valueOf(selectViews)));

                     //Close document and outputStream.
                     document.close();
                     outputStream.close();

                     System.out.println("Pdf created successfully.");
                 } catch (Exception ex) {
                     ex.printStackTrace();
                 }
             }
         });

    }



}
