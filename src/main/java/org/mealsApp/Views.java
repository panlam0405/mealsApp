package org.mealsApp;

import jakarta.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "Views")
@NamedQueries(value = {
        @NamedQuery(name = "Views.findAll", query = "SELECT v FROM Views v"),
        @NamedQuery(name = "Views.findByMeal", query = "SELECT v FROM Views v WHERE v.meal = :meal")
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

    public Views getDatafromDatabase (String mealName){
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
            EntityManager em = emf.createEntityManager();

            Query selectMeal = em.createNamedQuery("Views.findByMeal", Views.class);
            selectMeal.setParameter("meal", mealName);
            Views view = (Views) selectMeal.getSingleResult();

            return view;
        } catch (NoResultException e) {
            return null;
        }
    }

    public void setDataBaseNewInsert (String mealName) {
        setMeal(mealName);
        setViews(this.views == null ? 1 : this.views + 1);


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