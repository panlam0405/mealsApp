package org.mealsApp;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SearchCategoriesButton extends JFrame {

    public SearchCategoriesButton(String title) throws HeadlessException {
        super(title);
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
        String getFavFruit = (String) JOptionPane.showInputDialog(
                this,
                "Επίλεξε μια κατηγορία",
                "Κατηγορίες Γευμάτων",
                JOptionPane.QUESTION_MESSAGE,
                null,
                arrayCategories,
                arrayCategories[0]);
        String text =  "<html>Η κατηγορία %s Περιλαμβάνει τα παρακάτω γεύματα :<br> <br>".formatted(getFavFruit);
        int labelHeight = 19;
        String JsonResponseOfcategorySelected = api.getCategory(getFavFruit);
        JsonElement newRootElement = new JsonParser().parse(JsonResponseOfcategorySelected);
        System.out.println(newRootElement.getAsJsonObject().get("meals"));
        if (newRootElement.getAsJsonObject().get("meals").isJsonArray()) {
            JsonElement mealObject = newRootElement.getAsJsonObject().get("meals");
            System.out.println(mealObject.isJsonArray());
            if (mealObject.isJsonArray()){
                JsonArray mealArray = mealObject.getAsJsonArray();
                labelHeight *= mealArray.size();
                for (int i = 0 ; i < mealArray.size() ; i ++){
                    String meal  = mealArray.get(i).getAsJsonObject().get("strMeal").getAsString();
                    text += "Νο %s : %s<br>".formatted(i+1,meal);
                    if( mealArray.size() == i+1){
                        text+="<br></html>";
                    }
                }
            }
        }


        JFrame SelectedCategoryFrame = new JFrame(getFavFruit);
        SelectedCategoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        SelectedCategoryFrame.setSize(new Dimension(300,300));
        SelectedCategoryFrame.setLayout(new BorderLayout());
        JLabel label = new JLabel(text,SwingConstants.CENTER);
        JScrollPane scrollableLabel = new JScrollPane(label);
        label.setPreferredSize(new Dimension(400, labelHeight));
        SelectedCategoryFrame.add(scrollableLabel, BorderLayout.CENTER);
        SelectedCategoryFrame.setVisible(true);
    }
}
