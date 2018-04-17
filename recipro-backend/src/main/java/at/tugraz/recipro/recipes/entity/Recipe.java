/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.recipro.recipes.entity;

import java.io.Serializable;
import java.util.List;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dominik
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Recipe.FIND_ALL, query = "SELECT r FROM Recipe r"),
    @NamedQuery(name = Recipe.FIND_BY_TITLE, query = "SELECT r FROM Recipe r WHERE lower(r.title) like :title")
})
public class Recipe {
    
    @Id
    @GeneratedValue
    @JsonbTransient
    private long id;
    
    private String title;
    private int preparationTime;
    private List<RecipeType> recipeTypes;
    
    static final String PREFIX = "recipes.entity.Recipe.";
    public static final String FIND_ALL = PREFIX + "findAll";
    public static final String FIND_BY_TITLE = PREFIX + "findByTitle";

    public Recipe() {
    }

    public Recipe(String title, int preparationTime) {
        this.title = title;
        this.preparationTime = preparationTime;
    } 

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public List<RecipeType> getRecipeTypes() {
        return recipeTypes;
    }

    public void setRecipeTypes(List<RecipeType> recipeTypes) {
        this.recipeTypes = recipeTypes;
    }
}
