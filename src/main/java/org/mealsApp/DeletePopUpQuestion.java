package org.mealsApp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import javax.swing.*;

public class DeletePopUpQuestion extends JOptionPane {

    private JOptionPane jop;
    private final String mealName;



    public DeletePopUpQuestion (String mealName){
        this.mealName =mealName;

        int result = showConfirmDialog(null,"Are you sure you want to delete "+mealName+"?","Delete "+mealName,JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION){
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
            EntityManager em = emf.createEntityManager();

            em.getTransaction().begin();

            Query selectMeal = em.createNamedQuery("Meal.findByMeal", Meal.class);
            selectMeal.setParameter("meal",mealName);
            Meal m = (Meal) selectMeal.getSingleResult();
            em.remove(m);
            em.getTransaction().commit();

            em.close();
            emf.close();
        }
    }
}
