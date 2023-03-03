package org.mealsApp;

import jakarta.persistence.*;
import java.io.Serializable;

//Δημιουργία POJO κλάσης που συσχετίζεται με τον πίνακα Views
@Entity
@Table(name = "Views")
//Δημιουργία NamedQueries
@NamedQueries(value = {
        @NamedQuery(name = "Views.findAll", query = "SELECT v FROM Views v"),
        @NamedQuery(name = "Views.findByMeal", query = "SELECT v FROM Views v WHERE v.meal = :meal")
})

public class Views implements Serializable {

    private static final long serialVersionUID = 1L;
    //Δημιουργία πεδίων τα οποία αντιστοιχίζονται με στήλες του πίνακα

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq_gen")
    @SequenceGenerator(name = "my_seq_gen", sequenceName = "my_sequence", allocationSize = 1)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "MEAL")
    private String meal;
    @Column(name = "VIEWS")
    private Integer views;
    //Δημιουργια constructor
    public Views() {
    }

    //Δημιουργία Getters - Setters για τα πεδία της κλάσης
    public Views(Integer id) {
        this.id = id;
    }

    public Integer getViews() {
        return views;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Views(Integer id, String meal) {
        this.id = id;
        this.meal = meal;
    }

    //Δημιουργία μεθόδου για τον εντοπισμό εγγραφής βάση ονόματος
    public Views getDatafromDatabase (String mealName){

        try {
            //Δημιουργία EntityManagerFactory για την δημιουργία EntityManager
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
            //Δημιουργία EntityΜanager για τη διαχείριση του πίνακα
            EntityManager em = emf.createEntityManager();
            //Δημιουργία query
            Query selectMeal = em.createNamedQuery("Views.findByMeal", Views.class);
            //Ορισμός παραμέτρου βάσει του οποίου θα γίνει η αναζήτηση
            selectMeal.setParameter("meal", mealName);
            //Εκκίνηση συναλλαγής
            em.getTransaction().begin();
            //Αποθήκευση της επιστροφόμενης τιμής
            Views view = (Views) selectMeal.getSingleResult();
            //Ολοκλήρωση συναλλαγής
            em.getTransaction().commit();
            //Κλείσιμο EntityManager μετά τη διεκπεραίωση της συναλλαγής
            em.close();
            //Κλείσιμο του EntityManagerFactor μετά την ολοκλήρωση της διαχείριση του πίνακα από τον EntityManager
            emf.close();
            //Επιστροφή ανακτηθέντων δεδομένων.
            return view;
        } catch (NoResultException e) {
            return null;
        }
    }
    //Δημιουργία μεθόδου για την εισαγωγή δεδομένων στον πίνακα.

    public void setDataBaseNewInsert (String mealName) {
        setMeal(mealName);
        setViews(this.views == null ? 1 : this.views + 1);

        //Δημιουργία EntityManagerFactory για την δημιουργία EntityManager
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        //Δημιουργία EntityΜanager για τη διαχείριση του πίνακα
        EntityManager em = emf.createEntityManager();
        //Εκκίνηση συναλλαγής
        em.getTransaction().begin();
        //Αποθήκευση της τιμής
        em.persist(this);
        //Ολοκλήρωση συναλλαγής
        em.getTransaction().commit();
        //Κλείσιμο του EntityManagerFactor μετά την ολοκλήρωση της διαχείριση του πίνακα από τον EntityManager
        em.close();
        //Επιστροφή ανακτηθέντων δεδομένων.
        emf.close();
    }
    /*Η μέθοδος hashCode παράγει ένα integer μέσω του hashing algorithm, το οποίο αντιστοιχείται σε αντικείμενα.
    Χρησιμοποιείται σε collections και είναι ένας πιο αποδοτικός τρόπος διαχείριση του collection. Η μέθοδος επιστρέφει
    ότι και η equals() για αντικείμενα με το ίδιο hash code*/
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    //Η μέθοδος equals εξετάζει αν δύο αντικείμενα είναι ίδια και επιστρέφει false αν δεν είναι
    @Override
    public boolean equals(Object object) {
        //  Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Views)) {
            return false;
        }
        Views other = (Views) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    //Κάνουμε override την toString για να είναι μορφοποιημένη έξοδος των στοιχείων του γεύματος
    @Override
    public String toString() {
        return "ID: "+id+"\n"
                +"Meal: "+meal+"\n"
                +"Views: "+views+"\n"
                +"-----------------------------------------------------";
    }

}