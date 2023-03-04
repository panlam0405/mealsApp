package org.mealsApp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import javax.swing.*;

//Δημιουργία κλάσης για το παράθυρο διαλόγου επιβεβαίωσης διαγραφής μια καταχώρησης. Η κλαση επεκτείνει την κλάση JOptionPane
public class DeletePopUpQuestion extends JOptionPane {
    //Δημιουργία του πεδίου mealName
    private String mealName;

    //Δημιουργία constructor κλάσης με είσοδο το όνομα του γεύματος που θα διαγραφεί
    public DeletePopUpQuestion(String mealName) {
        this.mealName = mealName;
    }

    public void showConfirmDialog() {
        //Η μέθοδος showConfirmDialog δημιουργεί ένα pop-up παράθυρο διαλόγου. Η μέθοδος επιστρέφει έναν ακέραιο ο οποίος αντιπροσωπεύει την επιλογή του χρήστη
        Object[] options = {"Ναι", "Όχι"};
        int result = JOptionPane.showOptionDialog(null,
                "Θέλετε σίγουρα να διαγράψετε το " + mealName + "?",
                "Διαγραφή εγγραφής",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //χρησιμοποιείται το default Icon
                options,  //Οι τίτλοι των κουμπιών
                options[0]); //Οι προκαθορισμένοι τίτλοι των κουμπιών

        //Ελέγχεται αν η επιλογή του χρήστη είναι θετική
        if (result == JOptionPane.YES_OPTION) {
            //Δημιουργία EntityManagerFactory
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
            //Δημιουργία EntityManager
            EntityManager em = emf.createEntityManager();
            //Εκκίνηση συναλλαγών
            em.getTransaction().begin();
            //Δημιουργία query
            Query selectMeal = em.createNamedQuery("Meal.findByMeal", Meal.class);
            selectMeal.setParameter("meal", mealName);
            //Ανάκτηση δεδομένων
            Meal m = (Meal) selectMeal.getSingleResult();
            //Διαγραφή εγγραφής
            em.remove(m);
            //Ολοκλήρωση συναλλαγής
            em.getTransaction().commit();
            //Κλείσιμο EnityManager
            em.close();
            //Κλεισίμο EntityManagerFactory
            emf.close();
        }


    }
}
