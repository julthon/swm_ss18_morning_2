/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.recipro.recipes.entity;

/**
 *
 * @author Dominik
 */
public enum RecipeType {    
    DESSERT("DESSERT"),
    SNACK("SNACK"),
    MAIN_COURSE("MAIN_COURSE");

    private String name;
    
    RecipeType(String name) {
        this.name = name;
    }

    public static RecipeType fromString(String type) {
    for (RecipeType t : RecipeType.values())
        if (t.name.equalsIgnoreCase(type))
          return t;
    return null;
    }
}
