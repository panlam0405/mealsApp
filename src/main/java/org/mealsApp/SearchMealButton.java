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
    private final JTabbedPane tabbedPane;
    private JEditorPane mealText;
    private JScrollPane scroll;
    private JButton Save;
    private JButton Modify;
    private JButton Delete;
    private ModifyPopUpAndConfirmation mod;

    //Δημιουργία constructor. Δέχεται ως είσοδο μια καρτέλα
    public SearchMealButton(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        setBackground(new Color(83, 83, 83,70));
    }
    //Μέθοδος κλεισίματος καρτέλας
    public void removePanel() {
        tabbedPane.remove(this);
    }
    //Μέθοδος δημιουργίας πεδίου αναζήτησης στην καρτέλα
    public void openSearchField() {
        //Δημιουργία και μορφοποίηση πεδίου
        final TextField tf = new TextField();
//        μορφωποίηση του πεδίου πριν το κλικ
        tf.setFont(new Font("Serif", Font.ITALIC, 16));
        tf.setForeground(new Color(83, 83, 83,120));
        //Αν το πεδίο είναι άδειο εμφανίζεται με το μήνυμα
        if (tf.getText().isEmpty() && !(FocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == tf)) {
            tf.setText("Εδώ μπορείτε να αναζητήσετε το γεύμα της αρεσκείας σας");
        }
        //Αν επιλέξει με το caret το πεδίο αναζήτησης εξαφανίζεται το μήνυμα και αλλάζει τη γραμματοσειρά
        tf.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println(tf.getText());
                if (tf.getText().equals("Εδώ μπορείτε να αναζητήσετε το γεύμα της αρεσκείας σας")) {
                    tf.setText("");
                    tf.setFont(new Font("Serif", Font.PLAIN, 16));
                    tf.setForeground(new Color(83, 83, 83,120));
                }
            }
        });
        //Μορφοποίηση του πεδίου αναζήτησης γεύματος
        add(tf);
        tf.setBounds(200, 50, 500, 30);
//        δημιουργία και προσθήκη κουμπιών για την αναζήτηση γευμάτψν ή το κλείσιμο της καρτέλας
        JButton submit = new JButton("Αναζήτηση");
        submit.setBounds(200, 100, 200, 30);
        submit.setBackground(new Color(4, 170, 109, 197));
        submit.setForeground(new Color(255, 255, 255));

        add(submit);
        JButton cancel = new JButton("Κλείσιμο");
        cancel.setBounds(500, 100, 200, 30);
        cancel.setBackground(new Color(224, 67, 54, 197));
        cancel.setForeground(new Color(255, 255, 255));
        add(cancel);
        setSize(700, 700);
//        χωρίς layout manager
        setLayout(null);
        // οπτικοποίηση του frame
        setVisible(true);


        //Λειτουργικότητα κουμπιού υποβολής αναζήτησης
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Αναζήτηση στη βάση για το συγκεκριμένο όνομα γεύματος που δόθηκε από τον Χρήστη
//                και αν βρεθούν τα δεδομένα να φέρνουμε τα στοιχεία από τη βάση αλλιώς
//                να γίνεται κλήση του API


                String searchboxText = tf.getText().trim();
                final String[] mealName = {null};
                String mealArea ;
                String mealInstructions ;
                String mealCategory ;

//              Δημιουργία αντικειμένου Meal για την ανάκτηση γεύματος
                Meal meal = new Meal();
//                αν υπάρχουν τα δεδομένα στη βάση η μεταβλητή δε θα είναι null
                Meal existsInDb = meal.getDatafromDatabase(searchboxText);

                //Έλεγχος μη-ύπαρξης του γεύματος στη Βάση Δεδομένων και εκτέλεση κώδικα για κλήση από το API
                if (existsInDb == null) {
                    ApiCalls api = new ApiCalls();
                    JsonElement root = new JsonParser().parse(api.getMeal(searchboxText));
                    JsonElement getFirstMealProps = root.getAsJsonObject().get("meals");

//                    έλεγχος αν τα δεδομένα που ήρθαν πίσω μετά την αναζήτηση δεν περιέχουν κάποιο γεύμα
                    if (!getFirstMealProps.isJsonNull()) {
                        getFirstMealProps = getFirstMealProps.getAsJsonArray().get(0);

                        mealName[0] = getFirstMealProps
                                .getAsJsonObject().get("strMeal")
                                .getAsString();
                    }

//                    η παρακάτω ενέργεια εκτελείται αν δεν υπάρχουν δεδομένα στο json ή αν τα γεύματα που παραλαμβάνουμε είναι περισσότερα από ένα.
//                    εμφανίζει έτσι ένα μήνυμα λάθους και προτέπει σε καινούρια αναζήτηση.
                    if (root.getAsJsonObject().get("meals").isJsonNull() ||
                            (root.getAsJsonObject().get("meals")
                                    .getAsJsonArray().size() > 1) ||
                            !searchboxText.equals(mealName[0])) {

//                            αν δεν υπάρχει το editorPane το δημιουργεί και κάνει update το ui ήδη στην οθόνη κάνει update το ui
                        if (mealText == null) {
                            mealText = new JEditorPane("text/html", "");
                            mealText.setBounds(10, 250, 940, 500);
                            mealText.setEditable(false);
                            scroll = new JScrollPane(mealText);
                            scroll.setBounds(10, 250, 950, 500);
                            add(scroll);
                            mealText.setText("<br><br><br>" +
                                    "<div align='center'> <font size=\"5\" ><b>Δεν υπάρχει αναζήτηση που να αντιστοιχεί στο όνομα που δώσατε στο πεδίο.<br>" +
                                    "Κάντε μία καινούρια αναζήτηση ή συμβουλευτείτε τις κατηγορίες γευμάτων στο Μενού της εφαρμογής.</b> </font></div>");
                            mealText.updateUI();
                        } else {
                            mealText.setText("<br><br><br>" +
                                    "<div align='center'> <font size=\"5\" ><b>Δεν υπάρχει αναζήτηση που να αντιστοιχεί στο όνομα που δώσατε στο πεδίο.<br>" +
                                    "Κάντε μία καινούρια αναζήτηση ή συμβουλευτείτε τις κατηγορίες γευμάτων στο Μενού της εφαρμογής.</b> </font></div>");
                        }
                    } else { // αν η αναζήτηση στο Api επιστρέψει επιτυχώς ένα και μοναδικό γέυμα με βάση το όνομα που πληκτρολόγησε ο Χρήστης


                        mealArea = getFirstMealProps
                                .getAsJsonObject().get("strArea")
                                .getAsString();

                        mealInstructions = getFirstMealProps
                                .getAsJsonObject().get("strInstructions")
                                .getAsString();

                        mealCategory = getFirstMealProps
                                .getAsJsonObject().get("strCategory")
                                .getAsString();
//                        Κείμεno poυ εμφανίζεται στον Χρήστη.
                        String text = "<b>Name :</b> <br>%s<br><br>".formatted(mealName[0]) +
                                "<b>Category :</b> <br>%s<br><br>".formatted(mealCategory) +
                                "<b>Area :</b> <br>%s<br><br>".formatted(mealArea) +
                                "<b>Instructions :</b><br>%s".formatted(mealInstructions).replaceAll("\\n","<br>");
// επικοινωνία με τη βάση για να ανεβάσει τον αριθμό των views η να ορίσει το νέο αντικείμενο στον πίνακα με αρχικό view count = 1
                        try {
                            EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
                            EntityManager em = emf.createEntityManager();

                            Query selectMeal = em.createNamedQuery("Views.findByMeal", Views.class);
                            selectMeal.setParameter("meal", mealName[0]);

                            Views view = (Views) selectMeal.getSingleResult();
                            view.setViews(view.getViews() + 1);
                            em.getTransaction().begin();
                            em.persist(view);
                            em.getTransaction().commit();
                            em.close();
                            emf.close();
                        } catch (NoResultException ex) {
                            Views newView = new Views();
                            newView.setDataBaseNewInsert(mealName[0]);
                        }

//                έλεγχος αν υπάρχει το mealText(EditPane)από προηγούμενη αναζήτηση
                        if (mealText == null) {
                            mealText = new JEditorPane("text/html", "");
                            mealText.setBounds(10, 250, 940, 500);
                            mealText.setEditable(false);
                            scroll = new JScrollPane(mealText);
                            scroll.setBounds(10, 250, 950, 500);
                            add(scroll);
                            mealText.setText(text);
                            mealText.updateUI();
                        } else {
                            mealText.setText(text);
                        }
//                        έλεγχος ύπαρξης του κουμπιού Save και προσθήκη αν δεν υπάρχει
                        if (Save == null) {
                            Save = new JButton("Αποθήκευση");
                            Save.setBounds(100, 760, 150, 40);
                            add(Save);
                        }
//                        έλεγχος ύπαρξης του κουμπιού Modify και προσθήκη αν δεν υπάρχει
                        if (Modify == null) {
                            Modify = new JButton("Επεξεργασία");
                            Modify.setBounds(500, 760, 150, 40);
                            add(Modify);

                        }
//                        έλεγχος ύπαρξης του κουμπιού Delete και προσθήκη αν δεν υπάρχει
                        if (Delete == null) {
                            Delete = new JButton("Διαγραφή");
                            Delete.setBounds(700, 760, 150, 40);
                            add(Delete);
                        }
//   διαγραφή όποιων προηγούμενων listener είχαν τοποθετηθεί στα κουμπιά
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


                        String finalMealName = mealName[0];
                        String finalMealArea = mealArea;
                        String finalMealCategory = mealCategory;
                        String finalMealInstructions = mealInstructions;

//                        listener για το Save button
                        Save.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
//                     ενναλλαγή χρηστικότητας κουμπιών μετά από το click στο save
                                Save.setEnabled(false);
                                Modify.setEnabled(true);
                                Delete.setEnabled(true);
// καταγραφή στη βάση δεδομένων όσων περιλαμβάνονται στο EditPane
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

//                        listener για το Modify button
                        Modify.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
//                         αν υπάρχει προηγούμενο instance της κλάσης που ανοίγει το παράθυρο τροποποίησης τίθεται σε null
                                if(mod != null){
                                    System.out.println(mod);
                                    ModifyPopUpAndConfirmation.destroyInstance();
                                }
//                                ανοιγμα παραθύρου για την τροποίηση των στοιχείων που λάβαμε από το Api και αποθήκευση στη Βάση δεδομένων.
                                ModifyPopUpAndConfirmation mod =ModifyPopUpAndConfirmation.getInstance(finalMealName, finalMealArea, finalMealCategory, finalMealInstructions,mealText);
                                mod.makeModifications();
                            }
                        });
//                        listener για το Modify button
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

                } else { // σε περίπτωη που υπάρχει στη βάση δεδομένων το προς αναζήτηση γευμα
                    mealName[0] = existsInDb.getMeal();
                    mealArea = existsInDb.getArea();
                    mealCategory = existsInDb.getCategory();
                    mealInstructions = existsInDb.getInstructions();

                    String text = "<b>Name :</b> <br>%s<br><br>".formatted(mealName[0]) +
                            "<b>Category :</b> <br>%s<br><br>".formatted(mealCategory) +
                            "<b>Area :</b> <br>%s<br><br>".formatted(mealArea) +
                            "<b>Instructions :</b><br>%s".formatted(mealInstructions).replaceAll("\\n","<br>");
// επικοινωνία με τη βάση για να ανεβάσει τον αριθμό των views η να ορίσει το νέο αντικείμενο στον πίνακα με αρχικό view count = 1
                    try {
                        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
                        EntityManager em = emf.createEntityManager();

                        Query selectMeal = em.createNamedQuery("Views.findByMeal", Views.class);
                        selectMeal.setParameter("meal", mealName[0]);

                        Views view = (Views) selectMeal.getSingleResult();
                        view.setViews(view.getViews() + 1);
                        em.getTransaction().begin();
                        em.persist(view);
                        em.getTransaction().commit();
                        em.close();
                        emf.close();
                    } catch (NoResultException ex) {

                        Views newView = new Views();
                        newView.setDataBaseNewInsert(mealName[0]);
                    }

                    //                έλεγχος αν υπάρχει το mealText(EditPane)από προηγούμενη αναζήτηση
                    if (mealText == null) {
                        mealText = new JEditorPane("text/html", "");
                        mealText.setBounds(10, 250, 940, 500);
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
//   διαγραφή όποιων προηγούμενων listener είχαν τοποθετηθεί στα κουμπιά
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

                    String finalMealName1 = mealName[0];
                    String finalMealArea1 = mealArea;
                    String finalMealInstructions1 = mealInstructions;
                    String finalMealCategory1 = mealCategory;
                    Modify.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (mod != null){
                                System.out.println(mod);
                                ModifyPopUpAndConfirmation.destroyInstance();
                            }
                            mod = ModifyPopUpAndConfirmation.getInstance(finalMealName1, finalMealArea1, finalMealCategory1, finalMealInstructions1,mealText);
                            mod.makeModifications();

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
//listener για κλείσιμο της καρτέλας
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removePanel();
            }
        });

    }
}
