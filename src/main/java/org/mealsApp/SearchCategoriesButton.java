package org.mealsApp;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//Λειτουργικότητα του κουμπιού αναζήτησης κατηγορίας
public class SearchCategoriesButton extends JPanel {
    //Ορισμός ιδιοτήτων κλάσης
    private JTabbedPane tabbedPane;
    private String getMeals;
    //Δημιουργία constructor
    public SearchCategoriesButton(JTabbedPane tabbedPane,String getMeals) {
        this.tabbedPane = tabbedPane;
        this.getMeals = getMeals;
        setLayout(new BorderLayout());
        setBackground(new Color(83, 83, 83,120));
    }
    //Μέθοδος κλείσιμο καρτέλας αναζήτηση γεύματος ανά κατηγορία
    public void removePanel(){
        tabbedPane.remove(this);
    }

    //Δημιουργία της καρτέλας αναζήτηση γεύματος ανά κατηγορία
    public void openCategoriesFrame() {
        //Δημιουργία αντικειμένου για τις API κλήσεις
        ApiCalls api = new ApiCalls();
        //Έλεγχος για την κατηγορία αναζήτησης.
        if (getMeals != null) {
            String text = "<div align='center'> <font size=\"5\" > Η κατηγορία <b> %s </b> περιλαμβάνει τα παρακάτω γεύματα :</font> </div> <br><br><br>".formatted(getMeals);
            String JsonResponseOfCategorySelected = api.getCategory(getMeals);
            JsonElement newRootElement = new JsonParser().parse(JsonResponseOfCategorySelected);
            System.out.println(newRootElement.getAsJsonObject().get("meals"));
            //Έλεγχος των επιστρεφόμενων δεδομένων
            if (newRootElement.getAsJsonObject().get("meals").isJsonArray()) {
                JsonElement mealObject = newRootElement.getAsJsonObject().get("meals");
                System.out.println(mealObject.isJsonArray());
                if (mealObject.isJsonArray()) {
                    JsonArray mealArray = mealObject.getAsJsonArray();
                    //Επαναληπτική δομή για την προσθήκη των γευμάτων στο text που θα εμφανιστεί
                    for (int i = 0; i < mealArray.size(); i++) {
                        String meal = mealArray.get(i).getAsJsonObject().get("strMeal").getAsString();
                        text += "<div align='left'><font size=\"5\" >Νο %s</font> :<font size=\"4\" > %s </font></div> <br>".formatted(i + 1, meal);
                    }
                }
            }
            //Δημιουργία κουμπιού κλεισίματος
            JButton closeButton = new JButton("Κλείσιμο");

            //Λειτουργικότητα κουμπιού κλεισίματος
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removePanel();
                }
            });
            //Μορφοποίηση κουμπιού κλεισίματος
            closeButton.setBounds(100, 850, 100, 30);
            closeButton.setBackground(new Color(224, 67, 54, 197));
            closeButton.setForeground(new Color(255, 255, 255));
//            μορφοποίηση του χώρου παρουσίασεις των αποτελεσμάων που έρχονται από το Api με την λίστα των γευμάτων από τις κατηγορίες.
            JEditorPane mealCategoriesTextArea = new JEditorPane("text/html", text);
            JScrollPane scrollArea = new JScrollPane(mealCategoriesTextArea);
            scrollArea.setSize(800, 850);
            mealCategoriesTextArea.setForeground(Color.black);
            mealCategoriesTextArea.setEditable(false);
            add(scrollArea, BorderLayout.CENTER);
            scrollArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            add(closeButton, BorderLayout.PAGE_END);
            setVisible(true);
        }
        else {
            removePanel();
        }
        requestFocus();

    }
}
