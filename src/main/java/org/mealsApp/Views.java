package org.mealsApp;

import jakarta.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "Views")
@NamedQueries({

        @NamedQuery(name = "Views.findByMeal", query = "SELECT v FROM Views v WHERE v.views = :views")
})

public class Views implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq_gen")
    @SequenceGenerator(name = "my_seq_gen", sequenceName = "my_sequence", allocationSize = 1)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "MEAL")
    private String meal;
    @Column(name = "VIEWS")
    private Integer views;
    public Views() {
    }

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


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Views)) {
            return false;
        }
        Views other = (Views) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ID: "+id+"\n"
                +"Meal: "+meal+"\n"
                +"Views: "+views+"\n"
                +"-----------------------------------------------------";
    }

}