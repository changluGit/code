package com.six.entity;

import java.util.HashSet;
import java.util.Set;


/**
 * DishesCategory entity. @author MyEclipse Persistence Tools
 */

public class DishesCategory  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private Store store;
     private String name;
     private Set disheses = new HashSet(0);


    // Constructors

    /** default constructor */
    public DishesCategory() {
    }

    
    /** full constructor */
    public DishesCategory(Store store, String name, Set disheses) {
        this.store = store;
        this.name = name;
        this.disheses = disheses;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public Store getStore() {
        return this.store;
    }
    
    public void setStore(Store store) {
        this.store = store;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Set getDisheses() {
        return this.disheses;
    }
    
    public void setDisheses(Set disheses) {
        this.disheses = disheses;
    }
   








}