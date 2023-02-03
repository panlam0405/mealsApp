package org.mealsApp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ApiCalls {


    public String makeRequest(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        String jsonString = null;

        try (
                Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseString = response.body().string();
                jsonString = responseString;
            }
        } catch (
                IOException e) {
            e.printStackTrace();

        }

        return jsonString;
    }

    public String getMeal(String mealName) {
        String URLBASE = "https://www.themealdb.com/api/json/v1/1/search.php?s=";
        String URLWithMealQuery = URLBASE + mealName;
        return makeRequest(URLWithMealQuery);
    }

    public String showCategories() {
        String URLBASE =   "https://www.themealdb.com/api/json/v1/1/list.php?c=list";
        return makeRequest(URLBASE);
    }

    public String getCategory(String categoryName) {
        String URLBase = "https://www.themealdb.com/api/json/v1/1/filter.php?c=%s".formatted(categoryName);
        return makeRequest(URLBase);
    }

}
