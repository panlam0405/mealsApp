package org.mealsApp;

import jakarta.persistence.*;

import java.io.Serializable;

//Ορίζουμε ότι πρόκειται για ένα POJO που συνδέεται με τον πίνακα Meal.
@Entity
@Table(name = "Meal")
//Καθορίζουμε τα NamedQueries που θα χρειαστούμε
@NamedQueries({
        @NamedQuery(name = "Meal.findByMeal", query = "SELECT m FROM Meal m WHERE m.meal = :meal"),
        @NamedQuery(name = "Meal.findById", query = "SELECT m FROM Meal m WHERE m.id = :id")

})

public class Meal implements Serializable {

    private static final long serialVersionUID = 1L;

    //Ορίζουμε πεδία τα οποία αντιστοιχούν στις στήλες του πίνακα.
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq_gen")
    @SequenceGenerator(name = "my_seq_gen", sequenceName = "my_sequence", allocationSize = 1)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "MEAL")
    private String meal;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "AREA")
    private String area;
    @Lob
    @Column(name = "INSTRUCTIONS")
    private String instructions;


    //Δημιουργούμε constructors του POJO
    public Meal() {
    }

    public Meal(Integer id) {
        this.id = id;
    }

    public Meal(Integer id, String meal) {
        this.id = id;
        this.meal = meal;
    }

    //Δημιουργούμε μεθόδους Getters - Setters για τα πεδία
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }


    //Δημιουργούμε μέθοδο που επιστρέφει τις αναζήτηση ενός γεύματος με το όνομα του.
    public Meal getDatafromDatabase (String mealName){
        try {
            //Δημιουργία EntityManagerFactory για την δημιουργία EntityManager
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
            //Δημιουργία EntityΜanager για τη διαχείριση του πίνακα
            EntityManager em = emf.createEntityManager();
            //Δημιουργία query
            Query selectMeal = em.createNamedQuery("Meal.findByMeal", Meal.class);
            //Ορισμός παραμέτρου βάσει του οποίου θα γίνει η αναζήτηση
            selectMeal.setParameter("meal", mealName);
            //Εκκίνηση συναλλαγής
            em.getTransaction().begin();
            //Αποθήκευση των επιστρεφόμενων εγγραφών σε μεταβλητή Meal
            Meal meal = (Meal) selectMeal.getSingleResult();
            //Ολοκλήρωση συναλλαγής
            em.getTransaction().commit();
            //Κλείσιμο EntityManager μετά τη διεκπεραίωση της συναλλαγής
            em.close();
            //Κλείσιμο του EntityManagerFactor μετά την ολοκλήρωση της διαχείριση του πίνακα από τον EntityManager
            emf.close();
            //Επιστροφή ανακτηθέντων δεδομένων.
            return meal;
        } catch (Exception e) {
            return null;
        }
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

    //Η μέθοδος equals εξετάζει αν δύο αντικείμενα είναι ίδια και επιστρέφει false αν δεν είναι, αλλιώς true
    @Override
    public boolean equals(Object object) {
        //  Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Meal)) {
            return false;
        }
        Meal other = (Meal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    //Κάνουμε override την toString για να είναι μορφοποιημένη έξοδος των στοιχείων του γεύματος
    @Override
    public String toString() {
        return "ID: " + id + "\n"
                + "Meal: " + meal + "\n"
                + "Category: " + category + "\n"
                + "Area: " + area + "\n"
                + "Instructions: " + instructions + "\n"
                + "-----------------------------------------------------";
    }

}