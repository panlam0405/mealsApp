package org.mealsApp;

import jakarta.persistence.*;

import javax.swing.*;

public class ModifyConfirmation extends JOptionPane {
    private JOptionPane jop;
    private final String mealName;
    private final String newMealName;
    private final String mealArea;
    private final String mealCategory;
    private final String mealInstructions;

    public ModifyConfirmation(String newMealName,
                              String mealArea,
                              String mealCategory,
                              String mealInstructions,String mealName) {
        this.newMealName=newMealName;
        this.mealName = mealName;
        this.mealArea = mealArea;
        this.mealCategory = mealCategory;
        this.mealInstructions = mealInstructions;

        int result = showConfirmDialog(null, "Are you sure you want to modify " + mealName + "?",
                "Modify " + mealName, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
            EntityManager em = emf.createEntityManager();

            em.getTransaction().begin();
            try {

                Query selectMeal = em.createNamedQuery("Meal.findByMeal", Meal.class);
                selectMeal.setParameter("meal", mealName);
                Meal m = (Meal) selectMeal.getSingleResult();
//                int Id = m.getId();
//                Query selectMealById = em.createNamedQuery("Meal.findById", Meal.class);
//                selectMeal.setParameter("id", Id);
//                Meal m = (Meal) selectMeal.getSingleResult();
                m.setMeal(newMealName);
                m.setArea(mealArea);
                m.setCategory(mealCategory);
                m.setInstructions(mealInstructions);
                em.getTransaction().commit();
            } catch (NoResultException ex) {
                System.out.println("No result Found!");
            }

            em.close();
            emf.close();
        }

    }
}
