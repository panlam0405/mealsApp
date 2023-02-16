package org.mealsApp;

import jakarta.persistence.*;

import java.io.Serializable;


@Entity
@Table(name = "Meal")
@NamedQueries({
        @NamedQuery(name = "Meal.findByMeal", query = "SELECT m FROM Meal m WHERE m.meal = :meal")
})

public class Meal implements Serializable {

    private static final long serialVersionUID = 1L;

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


    public Meal() {
    }

    public Meal(Integer id) {
        this.id = id;
    }

    public Meal(Integer id, String meal) {
        this.id = id;
        this.meal = meal;
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

    public void setDataBaseNewInsert (String mealName, String categoryName, String areaName, String Instrunctions) {
        setMeal(mealName);
        setCategory(categoryName);
        setArea(areaName);
        setInstructions(Instrunctions);
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(this);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Meal)) {
            return false;
        }
        Meal other = (Meal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

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