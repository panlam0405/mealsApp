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

        /*Με την εντολή derby.language.sequence.preallocator, 1 καθορίζουμε ότι η derby για κάθε sequence θα δεσμεύει
        μια θέση μνήμης.
        Sequence είναι αντικείμενα της βάσης δεδομένων που παράγουν μοναδικές τιμές. Αυτό το κάνουμε ώστε η
        βάση δεδομένων να αυξάνει το id κατά ένα.*/
        System.setProperty("derby.language.sequence.preallocator", "1");
        //Εκκινούμε την εφαρμογή meals app δημιουργώντας ένα gui και καλώντας το
        GUI gui = new GUI("Meals App");
        gui.Simple();
    }

}