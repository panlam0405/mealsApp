package org.mealsApp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        /*Meal meal = new Meal();
        meal.setMeal("Beef");
        meal.setCategory("Meat");
        meal.setArea("America");
        meal.setInstructions("Bake it for 20'");
        meal.setViews(1);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(meal);
        em.getTransaction().commit();
        em.close();
        emf.close();
        System.out.println("Done");*/
        ApiCalls api = new ApiCalls();
            api.getMeal("Beef");
    }


}