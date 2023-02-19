package org.mealsApp;

import com.google.gson.JsonElement;

import com.google.gson.JsonParser;
import jakarta.persistence.*;

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
    private JButton Save;
    private JButton Modify;
    private JButton Delete;

    public SearchMealButton(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        setBackground(new Color(83, 83, 83));
    }

    public void removePanel() {
        tabbedPane.remove(this);
    }

    public void openSearchField() {

        final TextField tf = new TextField();
        tf.setFont(new Font("Serif", Font.ITALIC, 16));
        tf.setForeground(new Color(100, 100, 100, 80));

        if (tf.getText().isEmpty() && !(FocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == tf)) {
            tf.setText("Εδώ μπορείτε να αναζητήσετε το γεύμα της αρεσκείας σας");
        }
        tf.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println(tf.getText());
                if (tf.getText().equals("Εδώ μπορείτε να αναζητήσετε το γεύμα της αρεσκείας σας")) {
                    tf.setText("");
                    tf.setFont(new Font("Serif", Font.PLAIN, 16));
                    tf.setForeground(new Color(0, 0, 0));
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
                String mealName = null;
                String mealArea = null;
                String mealInstructions = null;
                String mealCategory = null;
//                check if meal is in Database
                Meal meal = new Meal();
                Meal existsInDb = meal.getDatafromDatabase(searchboxText);
                System.out.println("87 " + existsInDb);

                if (existsInDb == null) {
                    ApiCalls api = new ApiCalls();
                    JsonElement root = new JsonParser().parse(api.getMeal(searchboxText));

                    JsonElement getFirstMealProps = root.getAsJsonObject().get("meals")
                            .getAsJsonArray().get(0);

                    mealName = getFirstMealProps
                            .getAsJsonObject().get("strMeal")
                            .getAsString();

                    if (root.getAsJsonObject().get("meals").isJsonNull() ||
                            (root.getAsJsonObject().get("meals")
                                    .getAsJsonArray().size() > 1) ||
                            !searchboxText.equals(mealName)) {
                        if (mealText == null) {
                            mealText = new JTextArea();
                            mealText.setBounds(10, 250, 900, 500);
                            mealText.setLineWrap(true);
                            add(mealText);
                            mealText.setText("There in no search result with the given name. Please make a Newsearch");
                            mealText.updateUI();
                        } else {
                            mealText.setText("There in no search result with the given name. Please make a Newsearch");
                        }
                    } else {


                        mealArea = getFirstMealProps
                                .getAsJsonObject().get("strArea")
                                .getAsString();

                        mealInstructions = getFirstMealProps
                                .getAsJsonObject().get("strInstructions")
                                .getAsString();

                        mealCategory = getFirstMealProps
                                .getAsJsonObject().get("strCategory")
                                .getAsString();

                        String text = "Name : \n%s\n\n".formatted(mealName) +
                                "Category : \n%s\n\n".formatted(mealCategory) +
                                "Area : \n%s\n\n".formatted(mealArea) +
                                "Instructions :\n%s".formatted(mealInstructions);

                        try {
                            EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
                            EntityManager em = emf.createEntityManager();

                            Query selectMeal = em.createNamedQuery("Views.findByMeal", Views.class);
                            selectMeal.setParameter("meal", mealName);

                            Views view = (Views) selectMeal.getSingleResult();
                            view.setViews(view.getViews() + 1);
                            em.getTransaction().begin();
                            em.persist(view);
                            em.getTransaction().commit();
                            em.close();
                            emf.close();
                        } catch (NoResultException ex) {

                            Views newView = new Views();
                            newView.setDataBaseNewInsert(mealName);
                        }


                        if (mealText == null) {
                            mealText = new JTextArea();
                            mealText.setBounds(10, 250, 900, 500);
                            mealText.setLineWrap(true);
                            add(mealText);
                            mealText.setText(text);
                            mealText.updateUI();
                        } else {
                            mealText.setText(text);
                        }
                        if (Save == null) {
                            Save = new JButton("Αποθήκευση");
                            Save.setBounds(100, 760, 150, 40);
                            add(Save);

                        }
                        if (Modify == null) {
                            Modify = new JButton("Επεξεργασία");
                            Modify.setBounds(500, 760, 150, 40);
                            add(Modify);
                        }
                        if (Delete == null) {
                            Delete = new JButton("Διαγραφή");
                            Delete.setBounds(700, 760, 150, 40);
                            add(Delete);
                        }
                        Save.setEnabled(true);
                        Modify.setEnabled(false);
                        Delete.setEnabled(false);


                        String finalMealName = mealName;
                        String finalMealArea = mealArea;
                        String finalMealCategory = mealCategory;
                        String finalMealInstructions = mealInstructions;
                        Save.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Save.setEnabled(false);
                                Modify.setEnabled(true);
                                Delete.setEnabled(true);

                                Meal meal = new Meal();
                                meal.setMeal(finalMealName);
                                meal.setArea(finalMealArea);
                                meal.setCategory(finalMealCategory);
                                meal.setInstructions(finalMealInstructions);

                                EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
                                EntityManager em = emf.createEntityManager();
                                em.getTransaction().begin();
                                em.persist(meal);
                                em.getTransaction().commit();
                                em.close();
                                emf.close();
                            }
                        });

                        Modify.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                new ModifyPopUpAndConfirmation(finalMealName,finalMealArea,
                                        finalMealCategory,finalMealInstructions);
                            }
                        });

                        Delete.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                new DeletePopUpQuestion(finalMealName);
                            }
                        });

                        Delete.updateUI();
                        Save.updateUI();
                        Modify.updateUI();
                    }

                } else {

                    mealName = existsInDb.getMeal();
                    mealArea = existsInDb.getArea();
                    mealCategory = existsInDb.getCategory();
                    mealInstructions = existsInDb.getInstructions();

                    String text = "Name : \n%s\n\n".formatted(mealName) +
                            "Category : \n%s\n\n".formatted(mealCategory) +
                            "Area : \n%s\n\n".formatted(mealArea) +
                            "Instructions :\n%s".formatted(mealInstructions);
                    try {
                        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
                        EntityManager em = emf.createEntityManager();

                        Query selectMeal = em.createNamedQuery("Views.findByMeal", Views.class);
                        selectMeal.setParameter("meal", mealName);

                        Views view = (Views) selectMeal.getSingleResult();
                        view.setViews(view.getViews() + 1);
                        em.getTransaction().begin();
                        em.persist(view);
                        em.getTransaction().commit();
                        em.close();
                        emf.close();
                    } catch (NoResultException ex) {

                        Views newView = new Views();
                        newView.setDataBaseNewInsert(mealName);
                    }

                    if (mealText == null) {
                        mealText = new JTextArea();
                        mealText.setBounds(10, 250, 900, 500);
                        mealText.setLineWrap(true);
                        add(mealText);
                        mealText.setText(text);
                        mealText.updateUI();
                    } else {
                        mealText.setText(text);
                    }

                    if (Save == null) {
                        Save = new JButton("Αποθήκευση");
                        Save.setBounds(100, 760, 150, 40);
                        add(Save);

                    }
                    if (Modify == null) {
                        Modify = new JButton("Επεξεργασία");
                        Modify.setBounds(500, 760, 150, 40);
                        add(Modify);
                    }
                    if (Delete == null) {
                        Delete = new JButton("Διαγραφή");
                        Delete.setBounds(700, 760, 150, 40);
                        add(Delete);
                    }
                    Save.setEnabled(false);
                    Modify.setEnabled(true);
                    Delete.setEnabled(true);

                    Delete.updateUI();
                    Save.updateUI();
                    Modify.updateUI();

                    String finalMealName1 = mealName;
                    String finalMealArea1 = mealArea;
                    String finalMealInstructions1 = mealInstructions;
                    String finalMealCategory1 = mealCategory;
                    Modify.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new ModifyPopUpAndConfirmation(finalMealName1, finalMealArea1, finalMealCategory1, finalMealInstructions1);
                        }
                    });

                    Delete.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new DeletePopUpQuestion(finalMealName1);
                        }
                    });

                }


//                    Map <String, String> DBDetails = new HashMap<>();
//                    DBDetails.put("dbName",mealName);
//                    DBDetails.put("dbArea",mealArea);
//                    DBDetails.put("dbCategory",mealCategory);
//                    DBDetails.put("dbInstructions",mealInstructions);


//                αναζήτηση στη βάση με το όνομα για να ελέγξουμε αν υπάρχει και να μην προχωρήσουμε σε εγγραφή αλλά
//                σε αύξηση του count
//

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
