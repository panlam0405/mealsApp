package org.mealsApp;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//Κατασκευή παραθύρου τροποποίησης εγγραφής
public class ModifyPopUpAndConfirmation extends JFrame {
    //Ορισμός πεδίων της κλάσης. Το παράθυρο θα επιτρέπει την τροποποίηση των τεσσάρων χαρακτηριστικών του γεύματος
    private String mealName;
    private String area;
    private String category;
    private String instructions;
    private JFrame jf;
    private static ModifyPopUpAndConfirmation instance;
    private JEditorPane mealText;

    /*Όταν καλείται o constructor της κλάσης δημιουργείται ένα παράθυρο διαλόγου με τέσσερα πεδία τα οποία
    αντιστοιχούν σε τέσσερις αλλαγές που μπορεί να κάνει ο χρήστης σε ένα αποθηκευμένο γεύμα. Ο Constructor
    θα δέχεται τις αρχικές τιμές των τεσσάρων χαρακτηριστικών του εκάστοτε γεύματος και οι οποίες μπορούν
    να τροποποιηθούν*/
    private ModifyPopUpAndConfirmation(String mealName,
                                       String area,
                                       String category, String instructions,JEditorPane mealText) {
        //αποθήκευση των χαρακτηριστικών του γεύματος στο αντίστοιχο πεδίο της κλάσης
        this.area = area;
        this.mealName = mealName;
        this.category = category;
        this.instructions = instructions;
        this.mealText=mealText;
    }

    public static ModifyPopUpAndConfirmation getInstance(String mealName,
                                                         String area,
                                                         String category, String instructions,JEditorPane mealText) {
        if (instance == null) {
            instance = new ModifyPopUpAndConfirmation(mealName,
                    area,
                    category, instructions, mealText);
        }
        return instance;
    }

    public void makeModifications() {
        //Δημιουργία του πλαισίου διαλόγου και μορφοποίηση
        jf = new JFrame("Επεξεργασία γεύματος " + mealName);
        jf.setVisible(true);
        jf.setSize(1400, 900);
        JPanel buttonPanel = new JPanel();
        jf.add(buttonPanel, BorderLayout.PAGE_END);
        //Δημιουργία panel στο οποίο θα τοποθετηθεί ο πίνακα των στοιχειών του γεύματος κατά στήλες
        JPanel jp = new JPanel(new BorderLayout());
        jp.setBackground(new Color(83, 83, 83));
        jf.add(jp);
        //Δημιουργία δισδιάστατου αντικειμένου για την αποθήκευση των δεδομένων του γεύματος
        Object data[][] = {{mealName, area, category,  instructions.replace("\n", "<br>")}};
        //Δημιουργία μονοδιάστατου πίνακα με τα ονόματα των στηλών του πίνακα που θα δημιουργηθεί
        String column[] = {"Name", "Area", "Category", "Instructions"};
        //Δημιουργία πίνακα και μορφοποίηση του
        JTable jt = new JTable(data, column);
        jt.setPreferredScrollableViewportSize(jt.getPreferredSize());
        //Ορισμός του πλάτους των στηλών σε fixed τιμές
        jt.getColumnModel().getColumn(0).setPreferredWidth(100);
        jt.getColumnModel().getColumn(1).setPreferredWidth(100);
        jt.getColumnModel().getColumn(2).setPreferredWidth(100);
        jt.getColumnModel().getColumn(3).setPreferredWidth(200);

        jt.getColumnModel().getColumn(3).setCellRenderer(new CustomTableCellRenderer());

        //Δημιουργία scroll bar για τον πίνακα ώστε να μην κρύβεται τμήμα της συνταγής
        JScrollPane scrollPane = new JScrollPane(jt);
        jp.add(scrollPane, BorderLayout.CENTER);
        jt.setCellSelectionEnabled(true);

        //Δημιουργία κουμπιού αποθήκευσης αλλαγών
        JButton Save = new JButton("Αποθήκευση");
        //Δημιουργία κουμπιού ακύρωσης αποθήκευσης αλλαγών
        JButton Cancel = new JButton("Κλείσιμο");

        //Λειτουργικότητα κουμπιού αποθήκευσης
        Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableColumnModel columnModel = jt.getColumnModel();
                int columnCount = columnModel.getColumnCount();
                //Δημιουργία μεταβλητών που χρησιμοποιούνται για την αποθήκευση των χαρακτηριστικών του γεύματος
                String newMealName = null;
                String mealArea = area;
                String mealCategory = category;
                String mealInstructions = instructions;
                /*Επαναληπτική δομή η οποία περνάει όλα τα κελιά του πίνακα και αποθηκεύει τις τιμές αυτών στην
                κατάλληλη μεταβλητή*/
                for (int i = 0; i < columnCount; i++) {
                    TableColumn col = columnModel.getColumn(i);
                    String columnName = (String) col.getHeaderValue();
                    System.out.println("Column " + i + " name: " + columnName + ", value: " + jt.getValueAt(0, i));
                    if (columnName.equals("Name")) {
                        newMealName = jt.getValueAt(0, i).toString();
                    } else if (columnName.equals("Area")) {
                        mealArea = jt.getValueAt(0, i).toString();
                    } else if (columnName.equals("Category")) {
                        mealCategory = jt.getValueAt(0, i).toString();
                    } else if (columnName.equals("Instructions")) {
                        mealInstructions = jt.getValueAt(0, i).toString();
                    }
                }
                //Το αντικείμενο αποθηκεύει τις καινούργιες των χαρακτηριστικών του γεύματος
                new ModifyConfirmation(newMealName, mealArea, mealCategory, mealInstructions, mealName);
                area = mealArea;
                mealName = newMealName;
                category=mealCategory;
                instructions = mealInstructions;
                String text = "<b>Name :</b> <br>%s<br><br>".formatted(mealName) +
                        "<b>Category :</b> <br>%s<br><br>".formatted(category) +
                        "<b>Area :</b> <br>%s<br><br>".formatted(area) +
                        "<b>Instructions :</b><br>%s".formatted(instructions).replaceAll("\\n","<br>");
                mealText.setText(text);
            }
        });

        //Λειτουργικότητα κουμπιού ακύρωσης
        Cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.dispose();
            }
        });

        //Προσθήκη κουμπιών στο panel
        buttonPanel.add(Save);
        buttonPanel.add(Cancel);



    }
    public static void destroyInstance() {
        instance = null;
    }

    //Κατασκευή πίνακα ανάλογα με το μέγεθος της κειμένου
    private static class CustomTableCellRenderer extends DefaultTableCellRenderer {
        private final JEditorPane area;

        public CustomTableCellRenderer() {
            area = new JEditorPane("text/html", "");;
            area.setBackground(Color.WHITE);
            area.setBorder(null);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            area.setText(value != null ? value.toString() : "");
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            area.setSize(new Dimension(tableColumn.getWidth(), Integer.MAX_VALUE));
            int preferredHeight = area.getPreferredSize().height;
            table.setRowHeight(row, preferredHeight);
            return area;
        }
    }
}
