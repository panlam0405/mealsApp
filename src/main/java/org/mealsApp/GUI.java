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

//Δημιουργία γραφικού περιβάλλοντος κεντρικού παραθύρου διαλόγου εφαρμογής. Το παράθυρο αυτό θα καλέσει τα υπόλοιπα
//Το GUI κάνει extend την κλάση JFrame
public class GUI extends JFrame {


    private JTabbedPane tabbedPane;

    public GUI(String title) throws HeadlessException {
        super(title);
    }


    //Ορισμός διαστάσεων κουμπιών διαχείρησης
    private final int BUTTONWIDTH = 400;
    private final int BUTTONHEIGHT = 50;


    //Δημιουργια panel του παραθύρου
    public void Simple() {
//       κλείνει η εφαρμογή με το κλείσιμο του παραθύρου
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(null);
//      Δημιουργία δύο panel
        Panel panel1 = new Panel(null);
        Panel panel2 = new Panel(null);
//      Δημιουργία εικόνας τύπου JLabel
        JLabel panel2bg = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/src/assets/mealsAppImg.jpg"));
        //Ορισμός διαστάσεων του JLabel
        panel2bg.setSize(new Dimension(1000, 900));
        panel2bg.setVisible(true);
//      Ορισμός ενός tabbedPane σαν περιεχόμενο του panel set a tabbedPane as content to the split Panel
        tabbedPane = new JTabbedPane();
//      διαχωρισμός του περιεχομένου του panel split panels content
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel1, tabbedPane);
        //Δημιουργία επικεφαλίδας tab Meals Application
        tabbedPane.add("Meals Application", panel2);
        //Προσθήκη Jlabel στο panel2
        panel2.add(panel2bg);
//        Χωρισμός του frame σε δύο τμήματα
        add(splitPane);
        //Ορισμός χρώματος background του tabbedPane
        tabbedPane.setBackground(new Color(100, 100, 100, 70));

        tabbedPane.setBorder(null);
        //Ορισμός χρωμάτος background panel1
        panel1.setBackground(new Color(83, 83, 83, 120));

//        ορισμός διαστάσεων του διαχωρισμένου παραθύρου
        splitPane.setSize(new Dimension(1390, 850));
        //Ορισμός διαστάσεων panel1 και panel2
        panel1.setSize(new Dimension(400, 900));
        panel2.setSize(new Dimension(1000, 900));

        splitPane.setOneTouchExpandable(false); // αποτροπή από το να αυξομειώνεται το μεγεθος του splitPane
        splitPane.isVisible();
        splitPane.setEnabled(false);
//      Δημιουργία αντικειμένου JButton για την αναζήτηση γεύματος creating instance of JButton
        JButton anazitisiGeymatos = new JButton("Αναζήτηση Γευμάτων");

//listener για το κουμπί "Αναζήτηση Γευμάτων"
        anazitisiGeymatos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                δημιουργία καρτέλας που εντάσσεται στο δεξιό panel ως έτερο panel
                SearchMealButton jp = new SearchMealButton(tabbedPane);
                tabbedPane.add("Αναζήτηση Γευμάτων", jp);
                jp.openSearchField();
//                ορισμός του panel ως ενεργό
                tabbedPane.setSelectedComponent(jp);

            }
        });

//     Δημιουργία αντικειμένου JButton για την αναζήτηση κατηγοριών
        JButton anazitisiKatigorias = new JButton("Αναζήτηση Κατηγοριών");

//listener για το κουμπί "Αναζήτηση Κατηγοριών"

        anazitisiKatigorias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                instance του αντικειμένου ApiCalls
                ApiCalls api = new ApiCalls();

//                κλήση στο API για να δημιουργήσουμε ένα πίνακα με τις παρεχόμενες κατηγορίες
                JsonElement root = new JsonParser().parse(api.showCategories());
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

//                δημιουργία παραθύρου JOptionPane με βάση τις κατηγορίες που ανασύραμε από το API
                String getMeals = (String) JOptionPane.showInputDialog(
                        tabbedPane,
                        "Επίλεξε μια κατηγορία",
                        "Κατηγορίες Γευμάτων",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        arrayCategories,
                        arrayCategories[0]);

//                δημιουργία καρτέλας με βάση το όνομα που επιλέγετε "getMeals"
                SearchCategoriesButton jf = new SearchCategoriesButton(tabbedPane, getMeals);
                tabbedPane.add(getMeals, jf);
                jf.openCategoriesFrame();
                tabbedPane.setSelectedComponent(jf);
            }
        });

        //Δημιουργία JButton εκτύπωσης στατιστικών

        JButton ektypwshStatistikwn = new JButton("Αναζήτηση Στατιστικών");// instance του JButton


//listener για το κουμπί "Στατιστικά Αναζητήσεων"

        ektypwshStatistikwn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                αφαίρεση καρτέλας αν υπάρχει ήδη ανοιχτή για να φέρει τα ανανεωμένα δεδομένα και να μη δημιουργεί σύγχυση στο χρήστη
                int index = tabbedPane.indexOfTab("Στατιστικά Αναζητήσεων");
                if (index != -1) {
                    tabbedPane.removeTabAt(index);
                }

//                δημιουργία καρτέλας "Στατιστικά Αναζητήσεων"
                ektypwshStatistikwn es = new ektypwshStatistikwn(tabbedPane);
                es.setSize(1000, getHeight());
                tabbedPane.add("Στατιστικά Αναζητήσεων", es);
                es.openStatisticsChart();
                tabbedPane.setSelectedComponent(es);

            }
        });


        //Δημιουργία JButton εξόδου
        JButton eksodos = new JButton("Εξοδος");//creating instance of JButton

//listener για το κουμπί "Εξοδος" που κλείνει την εφαρμογή

        eksodos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

//        Ορισμός μη λειτουργικών κουμπιών με βάση τις απαιτήσεις της άσκησης για να είναι πλήρης η εφαρμογή
        JButton filterByFirstLetter = new JButton("Αναζήτηση με Πρώτο Γράμμα Γεύματος");//creating instance of JButton
        JButton mealByID = new JButton("Αναζήτηση με Id Γεύματος");//creating instance of JButton
        JButton randomMeal = new JButton("Τυχαία Aναζήτηση");//creating instance of JButton
        JButton areaList = new JButton("Αναζήτηση Περιοχών");//creating instance of JButton
        JButton ingredientsList = new JButton("Αναζήτηση Υλικών");//creating instance of JButton
        JButton mealByMainIngedient = new JButton("Αναζήτηση με Βασικό Υλικό");//creating instance of JButton
        JButton filterByCategory = new JButton("Αναζήτηση με Κατηγορία");//creating instance of JButton


        //Μορφοποίηση του κουμπιού anazitisiGeymatos
        anazitisiGeymatos.setBounds(0, 20, BUTTONWIDTH, BUTTONHEIGHT);
        anazitisiGeymatos.setFont(new Font("Arial", Font.PLAIN, 14));
        anazitisiGeymatos.setBorder(BorderFactory.createLineBorder(Color.white));
        anazitisiGeymatos.setContentAreaFilled(false);
        anazitisiGeymatos.setOpaque(false);
        anazitisiGeymatos.setForeground(Color.white);

        //Μορφοποίηση του κουμπιού anazitisiKatigorias
        anazitisiKatigorias.setBounds(0, 70, BUTTONWIDTH, BUTTONHEIGHT);
        anazitisiKatigorias.setFont(new Font("Arial", Font.PLAIN, 14));
        anazitisiKatigorias.setBorder(BorderFactory.createLineBorder(Color.white));
        anazitisiKatigorias.setContentAreaFilled(false);
        anazitisiKatigorias.setOpaque(false);
        anazitisiKatigorias.setForeground(Color.white);

        filterByFirstLetter.setBounds(0, 120, BUTTONWIDTH, BUTTONHEIGHT);
        filterByFirstLetter.setFont(new Font("Arial", Font.PLAIN, 14));
        filterByFirstLetter.setBorder(BorderFactory.createLineBorder(Color.white));
        filterByFirstLetter.setContentAreaFilled(false);
        filterByFirstLetter.setOpaque(false);
        filterByFirstLetter.setForeground(Color.white);
        filterByFirstLetter.setEnabled(false);

        mealByID.setBounds(0, 170, BUTTONWIDTH, BUTTONHEIGHT);
        mealByID.setFont(new Font("Arial", Font.PLAIN, 14));
        mealByID.setBorder(BorderFactory.createLineBorder(Color.white));
        mealByID.setContentAreaFilled(false);
        mealByID.setOpaque(false);
        mealByID.setForeground(Color.white);
        mealByID.setEnabled(false);

        randomMeal.setBounds(0, 220, BUTTONWIDTH, BUTTONHEIGHT);
        randomMeal.setFont(new Font("Arial", Font.PLAIN, 14));
        randomMeal.setBorder(BorderFactory.createLineBorder(Color.white));
        randomMeal.setContentAreaFilled(false);
        randomMeal.setOpaque(false);
        randomMeal.setForeground(Color.white);
        randomMeal.setEnabled(false);

        areaList.setBounds(0, 270, BUTTONWIDTH, BUTTONHEIGHT);
        areaList.setFont(new Font("Arial", Font.PLAIN, 14));
        areaList.setBorder(BorderFactory.createLineBorder(Color.white));
        areaList.setContentAreaFilled(false);
        areaList.setOpaque(false);
        areaList.setForeground(Color.white);
        areaList.setEnabled(false);

        ingredientsList.setBounds(0, 320, BUTTONWIDTH, BUTTONHEIGHT);
        ingredientsList.setFont(new Font("Arial", Font.PLAIN, 14));
        ingredientsList.setBorder(BorderFactory.createLineBorder(Color.white));
        ingredientsList.setContentAreaFilled(false);
        ingredientsList.setOpaque(false);
        ingredientsList.setForeground(Color.white);
        ingredientsList.setEnabled(false);

        mealByMainIngedient.setBounds(0, 370, BUTTONWIDTH, BUTTONHEIGHT);
        mealByMainIngedient.setFont(new Font("Arial", Font.PLAIN, 14));
        mealByMainIngedient.setBorder(BorderFactory.createLineBorder(Color.white));
        mealByMainIngedient.setContentAreaFilled(false);
        mealByMainIngedient.setOpaque(false);
        mealByMainIngedient.setEnabled(false);
        mealByMainIngedient.setForeground(Color.white);

        filterByCategory.setBounds(0, 420, BUTTONWIDTH, BUTTONHEIGHT);
        filterByCategory.setFont(new Font("Arial", Font.PLAIN, 14));
        filterByCategory.setBorder(BorderFactory.createLineBorder(Color.white));
        filterByCategory.setContentAreaFilled(false);
        filterByCategory.setOpaque(false);
        filterByCategory.setForeground(Color.white);
        filterByCategory.setEnabled(false);

        //Μορφοποίηση του κουμπιού ektypwshStatistikwn
        ektypwshStatistikwn.setBounds(0, 470, BUTTONWIDTH, BUTTONHEIGHT);
        ektypwshStatistikwn.setFont(new Font("Arial", Font.PLAIN, 14));
        ektypwshStatistikwn.setBorder(BorderFactory.createLineBorder(Color.white));
        ektypwshStatistikwn.setContentAreaFilled(false);
        ektypwshStatistikwn.setOpaque(false);
        ektypwshStatistikwn.setForeground(Color.white);

        //Μορφοποίηση του κουμπιού eksodos
        eksodos.setBounds(0, 520, BUTTONWIDTH, BUTTONHEIGHT);
        eksodos.setFont(new Font("Arial", Font.PLAIN, 14));
        eksodos.setBorder(BorderFactory.createLineBorder(Color.white));
        eksodos.setContentAreaFilled(false);
        eksodos.setOpaque(false);
        eksodos.setForeground(Color.white);

        //Προσθήκη των κουμπιών στο panel1
        getContentPane().add(splitPane);
        panel1.add(anazitisiGeymatos);
        panel1.add(anazitisiKatigorias);
        panel1.add(filterByFirstLetter);
        panel1.add(filterByCategory);
        panel1.add(mealByMainIngedient);
        panel1.add(ingredientsList);
        panel1.add(areaList);
        panel1.add(randomMeal);
        panel1.add(mealByID);
        panel1.add(anazitisiKatigorias);
        panel1.add(ektypwshStatistikwn);
        panel1.add(eksodos);

//        ορισμός icon της εφαρμογής
        Image icon = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + "/src/assets/Icon.jpg");
        setIconImage(icon);

//εισαγωγή logo στο πανελ
        ImageIcon logo = new ImageIcon(System.getProperty("user.dir") + "/src/assets/logo.png");
        System.out.println(logo);
        JLabel panel1Content = new JLabel(logo);

//        panel1Content.setIcon(imgThisImg);
        panel1Content.setBounds(0, 520, logo.getIconWidth(), logo.getIconHeight());
//        panel1Content.setFont(new Font("Serif", Font.PLAIN, 28));
        panel1Content.setVisible(true);
        panel1.add(panel1Content);


//      Μορφοποίηση του κεντρικού παραθύρου διαλόγου
        setSize(1400, 900);
        setResizable(false);
        setLayout(null);// no layout managers
        setLocationRelativeTo(null);
        setVisible(true);// frame visible
    }


}