package org.mealsApp;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchCategoriesButton extends JPanel {
    private JTabbedPane tabbedPane;
    private String getMeals;
    public SearchCategoriesButton(JTabbedPane tabbedPane,String getMeals) {
        this.tabbedPane = tabbedPane;
        this.getMeals = getMeals;
        setLayout(new BorderLayout());
        setBackground(new Color(83, 83, 83));
    }
    public void removePanel(){
        tabbedPane.remove(this);
    }

    public void openCategoriesFrame() {
        ApiCalls api = new ApiCalls();
        if (getMeals != null) {
            String text = "Η κατηγορία %s Περιλαμβάνει τα παρακάτω γεύματα : \n".formatted(getMeals);
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
                        text += "Νο %s : %s\n".formatted(i + 1, meal);
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

            JTextArea mealCategoriesTextArea = new JTextArea(text);
            JScrollPane scrollArea = new JScrollPane(mealCategoriesTextArea);
            scrollArea.setSize(800,850);
            mealCategoriesTextArea.setForeground(Color.black);
            mealCategoriesTextArea.setEditable(false);
            add(scrollArea,BorderLayout.CENTER);
            scrollArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            add(closeButton,BorderLayout.PAGE_END);

//        SelectedCategoryFrame.setSize(new Dimension(300,300));
//        SelectedCategoryFrame.setLayout(new BorderLayout());

            setVisible(true);
        }else {
            removePanel();
        }
        requestFocus();

    }
}
