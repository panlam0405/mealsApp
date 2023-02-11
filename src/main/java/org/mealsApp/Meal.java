package org.mealsApp;

import jakarta.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "Meal")
@NamedQueries({
        @NamedQuery(name = "Meal.findAll", query = "SELECT m FROM Meal m"),
        @NamedQuery(name = "Meal.findById", query = "SELECT m FROM Meal m WHERE m.id = :id"),
        @NamedQuery(name = "Meal.findByMeal", query = "SELECT m FROM Meal m WHERE m.meal = :meal"),
        @NamedQuery(name = "Meal.findByCategory", query = "SELECT m FROM Meal m WHERE m.category = :category"),
        @NamedQuery(name = "Meal.findByArea", query = "SELECT m FROM Meal m WHERE m.area = :area"),
        @NamedQuery(name = "Meal.findByInstructions", query = "SELECT m FROM Meal m WHERE m.instructions = :instructions"),
        @NamedQuery(name = "Meal.findByViews", query = "SELECT m FROM Meal m WHERE m.views = :views")})
//Επερώτηση που επιστρέφει όλα τα πεδία κάθε εγγραφης
//@NamedQuery(name = "Mealsdb.findAllDetails", query = "SELECT m FROM Mealsdb ("  ")
public class Meal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "MEAL")
    private String meal;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "AREA")
    private String area;
    @Column(name = "INSTRUCTIONS")
    private String instructions;
    @Column(name = "VIEWS")
    private Integer views;

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

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
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
        return "model.meal[ id= " + id + " ]";
    }

}