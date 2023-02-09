package org.mealsApp;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.google.gson.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SearchMealButton extends JFrame {
    private JFrame jframe;

    public SearchMealButton(String title) {
        super(title);
    }


    public void openSearchField(){

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final TextField tf = new TextField();
        add(tf);
        tf.setBounds(50,50, 150,20);
        JButton submit = new JButton("Sumbit");
        submit.setBounds(50,100,100,40);
        add(submit);
        JButton cancel = new JButton("Cancel");
        cancel.setBounds(150,100,100,40);
        add(cancel);
        setSize(700, 700);//400 width and 500 height
        setLayout(null);//using no layout managers
        setVisible(true);//making the frame visible

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Αναζήτηση στη βάση για το συγκεκριμένο όνομα γεύματος που δόθηκε από τον Χρήστη
//                και αν βρεθούν τα δεδομένα να φέρνουμε τα στοιχεία από την βάση αλλιώς
//                να γίνεται κλήση του API

                ApiCalls api = new ApiCalls();

                JsonElement root = new JsonParser().parse(api.getMeal(tf.getText()));

                JsonElement getFirstMealProps =root.getAsJsonObject().get("meals")
                        .getAsJsonArray().get(0);

                String mealName = getFirstMealProps
                        .getAsJsonObject().get("strMeal")
                        .getAsString();

                String mealArea = getFirstMealProps
                        .getAsJsonObject().get("strArea")
                        .getAsString();

                String mealInstructions = getFirstMealProps
                        .getAsJsonObject().get("strInstructions")
                        .getAsString();

                String mealCategory = getFirstMealProps
                        .getAsJsonObject().get("strCategory")
                        .getAsString();

                String text = "Name : %s\n\n".formatted(mealName) +
                        "Category : %s\n\n".formatted(mealCategory) +
                        "Area : %s\n\n".formatted(mealArea) +
                        "Instructions :\n%s".formatted(mealInstructions);

//                αναζήτηση στη βάση με το όνομα για να ελέγξουμε αν υπάρχει και να μην προχωρήσουμε σε εγγραφή αλλά
//                σε αύξηση του count
//                

                JTextArea mealText = new JTextArea(20,20);
                mealText.setBounds(50,250,500,800);
                mealText.setLineWrap(true);
                add(mealText);
                mealText.append(text);
                mealText.updateUI();
            }
        });

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComponent comp = (JComponent) e.getSource();
                Window win = SwingUtilities.getWindowAncestor(comp);
                win.dispose();
            }
        });

    }


}
