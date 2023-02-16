package org.mealsApp;

import com.google.gson.JsonElement;

import com.google.gson.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;


public class SearchMealButton extends JPanel {
    private JPanel jp;
    private JTabbedPane tabbedPane;
    private JTextArea mealText;
    public SearchMealButton(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        setBackground(new Color(83, 83, 83));
    }

    public void removePanel(){
        tabbedPane.remove(this);
    }
    public void openSearchField() {

        final TextField tf = new TextField();
        tf.setFont(new Font("Serif", Font.ITALIC, 16));
        tf.setForeground(new Color(100,100,100,80));

        if(tf.getText().isEmpty() && ! (FocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == tf)) {
            tf.setText("Εδώ μπορείτε να αναζητήσετε το γεύμα της αρεσκείας σας");
        }
        tf.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println(tf.getText());
                if(tf.getText().equals("Εδώ μπορείτε να αναζητήσετε το γεύμα της αρεσκείας σας")){
                    tf.setText("");
                    tf.setFont(new Font("Serif", Font.PLAIN, 16));
                    tf.setForeground(new Color(0,0,0));
                }
            }
        });

        add(tf);
        tf.setBounds(250, 50, 400, 30);
        JButton submit = new JButton("Sumbit");
        submit.setBounds(10, 100, 200, 30);
        add(submit);
        JButton cancel = new JButton("Cancel");
        cancel.setBounds(740, 100, 200, 30);
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

                String searchboxText = tf.getText();
                String mealName ;
                String mealArea ;
                String mealInstructions ;
                String mealCategory;


                    ApiCalls api = new ApiCalls();

                    JsonElement root = new JsonParser().parse(api.getMeal(searchboxText));

                    JsonElement getFirstMealProps = root.getAsJsonObject().get("meals")
                            .getAsJsonArray().get(0);

                    mealName = getFirstMealProps
                            .getAsJsonObject().get("strMeal")
                            .getAsString();

                    mealArea = getFirstMealProps
                            .getAsJsonObject().get("strArea")
                            .getAsString();

                    mealInstructions = getFirstMealProps
                            .getAsJsonObject().get("strInstructions")
                            .getAsString();

                    mealCategory = getFirstMealProps
                            .getAsJsonObject().get("strCategory")
                            .getAsString();


                    Map <String, String> DBDetails = new HashMap<>();
                    DBDetails.put("dbName",mealName);
                    DBDetails.put("dbArea",mealArea);
                    DBDetails.put("dbCategory",mealCategory);
                    DBDetails.put("dbInstructions",mealInstructions);



//                αναζήτηση στη βάση με το όνομα για να ελέγξουμε αν υπάρχει και να μην προχωρήσουμε σε εγγραφή αλλά
//                σε αύξηση του count
//
                String text = "Name : \n%s\n\n".formatted(mealName) +
                        "Category : \n%s\n\n".formatted(mealCategory) +
                        "Area : \n%s\n\n".formatted(mealArea) +
                        "Instructions :\n%s".formatted(mealInstructions);


                if (mealText == null) {
                    mealText = new JTextArea();
                    mealText.setBounds(10, 250, 900, 500);
                    mealText.setLineWrap(true);
                    add(mealText);
                    mealText.setText(text);
                    mealText.updateUI();
                }else {
                    mealText.setText(text);
                }

            }
        });

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removePanel();
            }
        });

    }


}
