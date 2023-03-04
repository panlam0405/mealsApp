package org.mealsApp;


import com.itextpdf.text.*;
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
import java.io.IOException;
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
        pdfButton.setBackground(new Color(16, 75, 196, 197));
        pdfButton.setForeground(new Color(255, 255, 255));
        //Προσθήκη του κουμπιού
        buttonsPanel.add(pdfButton);
        //Δημιουργία κουμπιού εξόδου
        JButton closeButton = new JButton("Κλείσιμο");
        closeButton.setBackground(new Color(224, 67, 54, 197));
        closeButton.setForeground(new Color(255, 255, 255));
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
                    PrintPdf doc = new PrintPdf(table);
                    doc.createDocument();
                } catch (DocumentException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }


}
