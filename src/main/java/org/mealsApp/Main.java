package org.mealsApp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        getMeal();
    }

    static String getMeal (){
        String urlToCall =
                " https://www.themealdb.com/api/json/v1/1/filter.php?c=Beef";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(urlToCall).build();

        try (
                Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseString = response.body().string();
                System.out.println(responseString);
                return responseString;
            }
        } catch (
                IOException e) {
            e.printStackTrace();
            return urlToCall;
        }

        return urlToCall;
    }
}