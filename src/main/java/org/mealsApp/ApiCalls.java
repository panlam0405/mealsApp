package org.mealsApp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;


//Δημιουργία class ApiCalls για την κλήση του API της ιστοσελίδας https://www.themealdb.com
public class ApiCalls {


    //Δημιουργία μεθόδου για την κλήση του API και επιστροφή δεδομένων με είσοδο URL
    public String makeRequest(String url) {

        //Δημιουργία κλάσης OkHttpClient. Κλάση αυτή παρέχει έναν τρόπο για να γίνονται ΗΤΤP requests και
        //να διαχειριζόμαστε τα ΗTTP connections.
        OkHttpClient client = new OkHttpClient();
        //Δημιουργία ενός HTTP request χρησιμοποιώντας την class Request από την βιβλιοθήκη OkHttp.
        Request request = new Request.Builder().url(url).build();
        //Δημιουργούμε string στο οποίο θα αποθηκευτούν τα επιστρεφόμενα δεδομένα
        String jsonString = null;

        try (

                Response response = client.newCall(request).execute()) {
            //Έλεγχος αν η επιστροφή από το request είναι επιτυχής και διάφορη του κενού
            if (response.isSuccessful() && response.body() != null) {
                //Μετατροπή του response σε String
                String responseString = response.body().string();
                jsonString = responseString;
            }

        } catch (
                IOException e) {
            e.printStackTrace();

        }

        return jsonString;
    }

    //Η μέθοδος getMeal επιστρέφει τα δεδομένα του γεύματος που αναζητούμε με είσοδο το όνομα
    //του γεύματος.
    public String getMeal(String mealName) {
        String URLBASE = "https://www.themealdb.com/api/json/v1/1/search.php?s=";
        //Δημιουργία του URL αναζήτησης
        String URLWithMealQuery = URLBASE + mealName;
        //Κλήση της makeRequest και επιστροφή των δεδομένων του γεύματος
        return makeRequest(URLWithMealQuery);
    }

    //Δημιουργία μεθόδου με επιστροφή κατηγοριών των γευμάτων
    public String showCategories() {
        String URLBASE =   "https://www.themealdb.com/api/json/v1/1/list.php?c=list";
        return makeRequest(URLBASE);
    }

    //Δημιουργία μεθόδου με επιστροφή γεύματων συγκεκριμένης κατηγορίας με είσοδο την κατηγορία
    public String getCategory(String categoryName) {
        String URLBase = "https://www.themealdb.com/api/json/v1/1/filter.php?c=%s".formatted(categoryName);
        return makeRequest(URLBase);
    }

}
