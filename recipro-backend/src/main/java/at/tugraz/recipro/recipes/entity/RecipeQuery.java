/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.recipro.recipes.entity;

import java.util.List;

/**
 *
 * @author Edith
 */
public class RecipeQuery {
    
    private String title;
    private List<RecipeType> recipeTypes;

    public RecipeQuery() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<RecipeType> getRecipeTypes() {
        return recipeTypes;
    }

    public void setRecipeTypes(List<RecipeType> recipeTypes) {
        this.recipeTypes = recipeTypes;
    }
    
    
}
