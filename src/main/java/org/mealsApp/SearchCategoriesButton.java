package org.mealsApp;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SearchCategoriesButton extends JPanel {
    private JTabbedPane tabbedPane;
    public SearchCategoriesButton(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        setBackground(new Color(83, 83, 83));
    }
    public void removePanel(){
        tabbedPane.remove(this);
    }

    public void openCategoriesFrame() {

//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ApiCalls api = new ApiCalls();
        JsonElement root = new JsonParser().parse(api.showCategories());
//        setSize(700, 700);//400 width and 500 height
//        setLayout(null);//using no layout managers
//        setVisible(true);//making the frame visible


        ArrayList<String> categoriesArray = new ArrayList<String>();
        if (root.isJsonObject()) {
            JsonArray mealsCategoriesArray = root.getAsJsonObject().get("meals").getAsJsonArray();
            for (int i = 0; i < mealsCategoriesArray.size(); i++) {
                if (mealsCategoriesArray.get(i).isJsonObject()) {
                    JsonObject categoryObject = mealsCategoriesArray.get(i).getAsJsonObject();
                    String category = categoryObject.get("strCategory").getAsString();
                    categoriesArray.add(category);
                }
            }
        }
        System.out.println(categoriesArray);
        String[] arrayCategories = new String[categoriesArray.size()];
        for (int i = 0; i < categoriesArray.size(); i++) {
            arrayCategories[i] = categoriesArray.get(i);
        }



        String getMeals = (String) JOptionPane.showInputDialog(
                this,
                "Επίλεξε μια κατηγορία",
                "Κατηγορίες Γευμάτων",
                JOptionPane.QUESTION_MESSAGE,
                null,
                arrayCategories,
                arrayCategories[0]);
        if (getMeals != null) {
            String text = "<html>Η κατηγορία %s Περιλαμβάνει τα παρακάτω γεύματα :<br> <br>".formatted(getMeals);
            String JsonResponseOfcategorySelected = api.getCategory(getMeals);
            JsonElement newRootElement = new JsonParser().parse(JsonResponseOfcategorySelected);
            System.out.println(newRootElement.getAsJsonObject().get("meals"));
            if (newRootElement.getAsJsonObject().get("meals").isJsonArray()) {
                JsonElement mealObject = newRootElement.getAsJsonObject().get("meals");
                System.out.println(mealObject.isJsonArray());
                if (mealObject.isJsonArray()) {
                    JsonArray mealArray = mealObject.getAsJsonArray();
                    for (int i = 0; i < mealArray.size(); i++) {
                        String meal = mealArray.get(i).getAsJsonObject().get("strMeal").getAsString();
                        text += "Νο %s : %s<br>".formatted(i + 1, meal);
                        if (mealArray.size() == i + 1) {
                            text += "<br></html>";
                        }
                    }
                }
            }

            JButton closeButton = new JButton("Close");

            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removePanel();
                }
            });

            closeButton.setBounds(100, 850, 100, 30);

            JLabel mealCategoriesLabel = new JLabel(text);
            mealCategoriesLabel.setForeground(Color.white);
            add(mealCategoriesLabel);
            add(closeButton);

//        SelectedCategoryFrame.setSize(new Dimension(300,300));
//        SelectedCategoryFrame.setLayout(new BorderLayout());


            setVisible(true);
        }else {
            removePanel();
        }
    }
}
