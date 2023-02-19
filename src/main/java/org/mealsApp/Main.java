package org.mealsApp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.apache.derby.iapi.sql.ResultSet;


import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        //        meal.setMeal("Beef");
//        meal.setCategory("Meat");
//        meal.setArea("America");
//        meal.setInstructions("Bake it for 20'");
//        meal.setViews(1);
//        System.out.println(meal);
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        em.merge(meal);
//        em.getTransaction().commit();
//        em.close();
//        emf.close();
//        System.out.println("Done");
        System.setProperty("derby.language.sequence.preallocator", "1");
        GUI gui = new GUI("Meals App");
        gui.Simple();
    }

}