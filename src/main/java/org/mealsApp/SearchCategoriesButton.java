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
    //Ορισμός πεδίων κλάσης
    private JTabbedPane tabbedPane;
    private String getMeals;
    //Δημιουργία constructor
    public SearchCategoriesButton(JTabbedPane tabbedPane,String getMeals) {
        this.tabbedPane = tabbedPane;
        this.getMeals = getMeals;
        setLayout(new BorderLayout());
        setBackground(new Color(83, 83, 83));
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
            String text = "Η κατηγορία %s Περιλαμβάνει τα παρακάτω γεύματα : \n".formatted(getMeals);
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
                        text += "Νο %s : %s\n".formatted(i + 1, meal);
                    }
                }
            }
            //Δημιουργία κουμπιού κλεισίματος
            JButton closeButton = new JButton("Close");

            //Λειτουργικότητα κουμπιού κλεισίματος
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removePanel();
                }
            });
            //Μορφοποίηση κουμπιού κλεισίματος
            closeButton.setBounds(100, 850, 100, 30);

            JTextArea mealCategoriesTextArea = new JTextArea(text);
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
