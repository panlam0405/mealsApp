package org.mealsApp;


import com.itextpdf.text.*;
import com.itextpdf.text.Font;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//Δημιουργία class για την εκτύπωση των στατιστικών και επέκταση της class JPanel

public class ektypwshStatistikwn extends JPanel {

    private JTabbedPane tabbedPane;
    private JTable table;

    //Δημιουργία constructor της κλάσης

    public ektypwshStatistikwn(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        //Μορφοποίηση καρτέλας ektypeshStatistikwn
        setSize(new Dimension(900, 850));
        setBackground(new Color(83, 83, 83));
        setLayout(new BorderLayout());

    }
    //Δημιουργία μεθόδου για το κλείσιμο της καρτέλας
    public void removePanel() {
        tabbedPane.remove(this);
    }

    //Δημιουργία Chart στατιστικών
    public void openStatisticsChart() {
        //Δημιουργία αντικειμένου DefaultCategoryDataset
        DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
        //Δημιουργία EntityManagerFactory για την δημιουργία του EntityManager
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        //Δημιουργία EntityManager
        EntityManager em = emf.createEntityManager();
        //Εκκίνηση συναλλαγής με τον πίνακα της Βάσης Δεδομένων
        em.getTransaction().begin();
        //Αποθήκευση επιστρεφόμενων αποτελεσμάτων σε αντικείμενο τύπου List
        List<Views> selectViews = em.createNamedQuery("Views.findAll", Views.class).getResultList();
        System.out.println(selectViews);

        //Ταξινόμηση αποτελεσμάτων βάσει των views
        Collections.sort(selectViews, new Comparator<Views>() {
            @Override
            public int compare(Views v1, Views v2) {
                return Integer.compare(v2.getViews(), (v1.getViews()));
            }
        });

        for (Views v : selectViews) {

            categoryDataset.setValue(v.getViews(), v.getViews(), v.getMeal());
        }

        //Ολοκλήρωση συναλλαγής με τον πίνακα της Βάσης Δεδομένων
        em.getTransaction().commit();
        //Κλείσιμο του EntityManager
        em.close();
        //Κλείσιμο του EntityManagerFactory
        emf.close();

        //Δημιουργία αντικειμένου JFreeChart που θα δεχτεί τα δεδομένα
        JFreeChart chart = ChartFactory.createBarChart("Στατιστικά Αναζητήσεων",
                "Όνομα Γεύματος",
                "Πλήθος Αναζητήσεων",
                categoryDataset);
        //Μορφοποίηση του chart
        CategoryPlot p = chart.getCategoryPlot();
        p.setRangeGridlinePaint(Color.BLACK);
        ChartPanel cp = new ChartPanel(chart);

        //Δημιουργία διατεταγμένου ζεύγους {'Όνομα γεύματος','Views'}
        String[] columnNames = {"Meal", "Views"};
        //Δημιουργία δισδιάστατου αντικειμένου. Η μια διάσταση αφορά το διατεταγμένο ζεύγος
        // {'Όνομα γεύματος','Πλήθος views'} και η άλλη διάσταση αφορά τον αύξοντα αριθμό τους.
        Object[][] data = new Object[selectViews.size()][columnNames.length];
        //Εισαγωγή των δυάδων στον πίνακα
        int i = 0;
        for (Views view : selectViews) {
            String mName = view.getMeal();
            int mViews = view.getViews();
            data[i] = new Object[]{mName, mViews};
            i++;
        }
        //Κατασκευή του πίνακα με τα αντικείμενα
        table = new JTable(data, columnNames);
        table.setVisible(true);
        table.setFillsViewportHeight(true);

        //Δημιουργία panel statistics και μορφοποίηση του
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


        //Δημιουργία κουμπιού εκτύπωσης δεδομένων
        JButton pdfButton = new JButton("Εκτύπωση Στατιστικών (pdf)");
        //Προσθήκη του κουμπιού
        buttonsPanel.add(pdfButton);
        //Δημιουργία κουμπιού εξόδου
        JButton closeButton = new JButton("Κλείσιμο");
        //Προσθήκη κουμπιού εξόδου
        buttonsPanel.add(closeButton);
        add(buttonsPanel, BorderLayout.PAGE_END);
        //Προσθήκη λειτουργικότητας στο κουμπί εξόδου
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removePanel();
            }
        });


        //Προσθήκη λειτουργικότητας στο κουμπί εκτύπωσης pdf
        pdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    //Δημιουργία αντικειμένου Document
                    Document document = new Document();

                    //Δημιουργία αντικειμένου OutputStream
                    OutputStream outputStream =
                            new FileOutputStream(new File(System.getProperty("user.dir") + "/file.pdf"));

                    //Δημιουργία αντικειμένου PDFWriter
                    PdfWriter writer = PdfWriter.getInstance(document, outputStream);

                    //προσθήκη αριθμών στο τέλος των σελίδων
                    Footer event = new Footer();
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
                } catch (NullPointerException exception) {
                    exception.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }


}
