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

    private JTabbedPane tabbedPane;


    public ektypwshStatistikwn(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        setBackground(new Color(83, 83, 83));
        setLayout(new BorderLayout());

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

        JPanel container = new JPanel();
        JPanel buttonsPanel = new JPanel();

        add(container,BorderLayout.CENTER);
        container.setBounds(0,0,900,850);



//        chartPanel.setSize(500,500);


        container.add(cp);

        tabbedPane.setVisible(true);


         JButton pdfButton = new JButton("ΕΚΤΥΠΩΣΗ ΣΤΑΤΙΣΤΙΚΩΝ");

        buttonsPanel.add(pdfButton);

         JButton closeButton = new JButton("Close");

         buttonsPanel.add(closeButton);
         add(buttonsPanel,BorderLayout.PAGE_END);
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
                             new FileOutputStream(new File(System.getProperty("user.dir")+"/file.pdf"));

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
                 } catch (NullPointerException exception){
                     exception.printStackTrace();
                 } catch (Exception ex) {
                     ex.printStackTrace();
                 }
             }
         });

    }



}
