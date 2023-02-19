package org.mealsApp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import javax.swing.*;

public class ModifyConfirmation extends JOptionPane {
    private JOptionPane jop;
    private final String mealName;
    private final String mealArea;
    private final String mealCategory;
    private final String mealInstructions;

    public ModifyConfirmation (String mealName,
                               String mealArea,
                               String mealCategory,
                               String mealInstructions){
        this.mealName = mealName;
        this. mealArea = mealArea;
        this. mealCategory = mealCategory;
        this.mealInstructions = mealInstructions;
        int result = showConfirmDialog(null,"Are you sure you want to modify "+mealName+"?",
                "Modify "+mealName,JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION){
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
            EntityManager em = emf.createEntityManager();

            em.getTransaction().begin();

            Query selectMeal = em.createNamedQuery("Meal.findByMeal", Meal.class);
            selectMeal.setParameter("meal",mealName);
            Meal m = (Meal) selectMeal.getSingleResult();
            m.setMeal(mealName);
            m.setArea(mealArea);
            m.setCategory(mealCategory);
            m.setInstructions(mealInstructions);
            System.out.println(m);
            em.getTransaction().commit();

            em.close();
            emf.close();
        }

    }
}
