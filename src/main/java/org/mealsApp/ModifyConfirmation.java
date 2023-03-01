package org.mealsApp;

import jakarta.persistence.*;

import javax.swing.*;
//Κατασκευή παραθύρου διαλόγου με επέκταση του JOptionPane
public class ModifyConfirmation extends JOptionPane {
    //Δημιουργία των χαρακτηριστικών των γευμάτων

    private final String mealName;
    private final String newMealName;
    private final String mealArea;
    private final String mealCategory;
    private final String mealInstructions;

    //Κατασκευή του constructor
    public ModifyConfirmation(String newMealName,
                              String mealArea,
                              String mealCategory,
                              String mealInstructions,String mealName) {
        this.newMealName=newMealName;
        this.mealName = mealName;
        this.mealArea = mealArea;
        this.mealCategory = mealCategory;
        this.mealInstructions = mealInstructions;


        //Δημιουργία και μορφοποίηση του πλαισίου επιβεβαίωσης αλλαγών εγγραφής
        Object[] options = {"Ναι", "Όχι"};
        int result= JOptionPane.showOptionDialog(null,
                "Θέλετε σίγουρα να οριστικοποιήσετε τις αλλαγές στο "+mealName+"?",
                "Διαγραφή εγγραφής",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //χρησιμοποιείται το default Icon
                options,  //Οι τίτλοι των κουμπιών
                options[0]); //Οι προκαθορισμένοι τίτλοι των κουμπιών
        if (result == JOptionPane.YES_OPTION) {
            //Δημιουργία EntityManagerFactory
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
            //Δημιουργία EntityManager
            EntityManager em = emf.createEntityManager();
            //Εκκίνηση συναλλαγών
            em.getTransaction().begin();
            try {
                //Δημιουργία query
                Query selectMeal = em.createNamedQuery("Meal.findByMeal", Meal.class);
                //Εισαγωγή παραμέτρου στην αναζήτηση
                selectMeal.setParameter("meal", mealName);
                //Αποθήκευση επιστρεφόμενων αποτελεσμάτων σε μεταβλητή Meal
                Meal m = (Meal) selectMeal.getSingleResult();
                //Αλλαγή των πεδίων της εγγραφής με τα καινούργια
                m.setMeal(newMealName);
                m.setArea(mealArea);
                m.setCategory(mealCategory);
                m.setInstructions(mealInstructions);
                //Ολοκλήρωση συναλλαγής με τον πίνακα της Βάσης Δεδομένων
                em.getTransaction().commit();
            } catch (NoResultException ex) {
                System.out.println("No result Found!");
            }
            //Κλείσιμο του EntityManager
            em.close();
            //Κλείσιμο του EntityManagerFactory
            emf.close();
        }

    }
}
