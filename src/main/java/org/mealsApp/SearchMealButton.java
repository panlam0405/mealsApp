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

//Λειτουργικότητα του κουμπιού αναζήτησης γεύματος
public class SearchMealButton extends JPanel {
    //Δημιουργία πεδίων κλάσης
    private JTabbedPane tabbedPane;
    private JEditorPane mealText;
    private JScrollPane scroll;
    private JButton Save;
    private JButton Modify;
    private JButton Delete;

    //Δημιουργία constructor. Δέχεται ως είσοδο μια καρτέλα
    public SearchMealButton(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        setBackground(new Color(83, 83, 83));
    }
    //Μέθοδος κλεισίματος καρτέλας
    public void removePanel() {
        tabbedPane.remove(this);
    }
    //Μέθοδος δημιουργίας πεδίου αναζήτησης στην καρτέλα
    public void openSearchField() {
        //Δημιουργία και μορφοποίηση πεδίου
        final TextField tf = new TextField();
        tf.setFont(new Font("Serif", Font.ITALIC, 16));
        tf.setForeground(new Color(100, 100, 100, 80));
        //Αν το πεδίο είναι άδειο εμφανίζεται με το μήνυμα
        if (tf.getText().isEmpty() && !(FocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == tf)) {
            tf.setText("Εδώ μπορείτε να αναζητήσετε το γεύμα της αρεσκείας σας");
        }
        //Αν επιλέξει με το caret το πεδίο αναζήτησης εξαφανίζεται το μήνυμα
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
        //Μορφοποίηση του πεδίου αναζήτησης γεύματος
        add(tf);
        tf.setBounds(200, 50, 500, 30);
        JButton submit = new JButton("Αναζήτηση");
        submit.setBounds(200, 100, 200, 30);
        add(submit);
        JButton cancel = new JButton("Κλείσιμο");
        cancel.setBounds(500, 100, 200, 30);
        add(cancel);
        setSize(700, 700);//400 width and 500 height
        setLayout(null);//using no layout managers
        setVisible(true);//making the frame visible


        //Λειτουργικότητα κουμπιού υποβολής αναζήτησης
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Αναζήτηση στη βάση για το συγκεκριμένο όνομα γεύματος που δόθηκε από τον Χρήστη
//                και αν βρεθούν τα δεδομένα να φέρνουμε τα στοιχεία από τη βάση αλλιώς
//                να γίνεται κλήση του API

                String searchboxText = tf.getText();
                String mealName = null;
                String mealArea = null;
                String mealInstructions = null;
                String mealCategory = null;
//              Δημιουργία αντικειμένου Meal για την ανάκτηση γεύματος
                Meal meal = new Meal();
                Meal existsInDb = meal.getDatafromDatabase(searchboxText);
                //Έλεγχος μη-ύπαρξης του γεύματος στη Βάση Δεδομένων
                if (existsInDb == null) {
                    ApiCalls api = new ApiCalls();
                    JsonElement root = new JsonParser().parse(api.getMeal(searchboxText));
                    JsonElement getFirstMealProps = root.getAsJsonObject().get("meals");
                    if (!getFirstMealProps.isJsonNull()) {
                        getFirstMealProps = getFirstMealProps.getAsJsonArray().get(0);

                        mealName = getFirstMealProps
                                .getAsJsonObject().get("strMeal")
                                .getAsString();
                    }

                    if (root.getAsJsonObject().get("meals").isJsonNull() ||
                            (root.getAsJsonObject().get("meals")
                                    .getAsJsonArray().size() > 1) ||
                            !searchboxText.equals(mealName)) {
                        if (mealText == null) {
                            mealText = new JEditorPane();
                            mealText.setBounds(10, 250, 950, 500);
//                            mealText.setLineWrap(true);
                            mealText.setEditable(false);
                            scroll = new JScrollPane(mealText);
                            scroll.setBounds(10, 250, 950, 500);
                            add(scroll);
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

                        String text = "<b>Name :</b> <br>%s<br><br>".formatted(mealName) +
                                "<b>Category :</b> <br>%s<br><br>".formatted(mealCategory) +
                                "<b>Area :</b> <br>%s<br><br>".formatted(mealArea) +
                                "<b>Instructions :</b><br>%s".formatted(mealInstructions).replaceAll("\\n","<br>");

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
                            mealText = new JEditorPane("text/html", "");
                            mealText.setBounds(10, 250, 950, 500);
//                            mealText.setLineWrap(true);
                            mealText.setEditable(false);
                            scroll = new JScrollPane(mealText);
                            scroll.setBounds(10, 250, 950, 500);
                            add(scroll);
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
                        for( ActionListener all : Save.getActionListeners() ) {
                            Save.removeActionListener( all );
                        }
                        for( ActionListener all : Modify.getActionListeners() ) {
                            Modify.removeActionListener( all );
                        }
                        for( ActionListener all : Delete.getActionListeners() ) {
                            Delete.removeActionListener( all );
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
                                new ModifyPopUpAndConfirmation(finalMealName, finalMealArea,
                                        finalMealCategory, finalMealInstructions);
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

                    String text = "<b>Name :</b> <br>%s<br><br>".formatted(mealName) +
                            "<b>Category :</b> <br>%s<br><br>".formatted(mealCategory) +
                            "<b>Area :</b> <br>%s<br><br>".formatted(mealArea) +
                            "<b>Instructions :</b><br>%s".formatted(mealInstructions).replaceAll("\\n","<br>");
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
                        mealText = new JEditorPane("text/html", "");
                        mealText.setBounds(10, 250, 950, 500);
//                        mealText.setLineWrap(true);
                        mealText.setEditable(false);
                        scroll = new JScrollPane(mealText);
                        scroll.setBounds(10, 250, 950, 500);
                        add(scroll);
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
                    for( ActionListener all : Save.getActionListeners() ) {
                        Save.removeActionListener( all );
                    }
                    for( ActionListener all : Modify.getActionListeners() ) {
                        Modify.removeActionListener( all );
                    }
                    for( ActionListener all : Delete.getActionListeners() ) {
                        Delete.removeActionListener( all );
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
