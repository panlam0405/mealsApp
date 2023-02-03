package org.mealsApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;

public class Main {
    public static void main(String[] args) {

        String Meals = getMeal("Sagano");
        
    }

    public static String getMeal (String meal){

        String urlToCall =
                "https://www.themealdb.com/api/json/v1/1/search.php?s="+meal;


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(urlToCall).build();

        try (

                Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseString = response.body().string();
                //System.out.println(responseString);

                JSONObject jsonObject = new JSONObject(responseString);
                JSONArray meal1 = jsonObject.getJSONArray("meals");
                System.out.println("There is/are "+meal1.length()+" dish with "+meal);
                for(int i =0; i < meal1.length();i++){
                    System.out.println("----------------------------------------------------------------------------");
                    JSONObject jsonObject1 = meal1.getJSONObject(i);
                    String strArea = jsonObject1.getString("strArea");
                    System.out.println("The dish is "+strArea);
                    String strCategory = jsonObject1.getString("strCategory");
                    System.out.println("The dish is category of "+strCategory);
                    String strMeal = jsonObject1.getString("strMeal");
                    System.out.println("The name of the dish is "+strMeal);
                    String strInstructions = jsonObject1.getString("strInstructions");
                    System.out.println("The instructions to cook are ");
                    System.out.println(strInstructions);
                }

                return responseString;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return urlToCall;
        }catch (JSONException ex){
            System.out.println("The dish you requested does not exist is the DataBase");
        }

        return urlToCall;
    }

//    public static void createTable(Gson file){
//
//        try {
//            JsonElement fileElement = JsonParser.parseReader(new FileReader(String.valueOf(file)));
//            JsonObject fileObject = fileElement.getAsJsonObject();
//            //Ανάκτηση συγκεκριμένου πεδίου
//            String strMeal = String.valueOf(fileObject.get("strMeal"));
//            System.out.println("Meal " + strMeal);
//        }catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
}