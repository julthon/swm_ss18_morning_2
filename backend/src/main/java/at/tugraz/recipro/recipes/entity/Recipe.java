/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.recipro.recipes.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;




/**
 *
 * @author Edith
 */
@Entity
public class Recipe {

        
    @Id
    @GeneratedValue
    private long id;
    
    private String title;

    public Recipe() {
    }

    public Recipe(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
