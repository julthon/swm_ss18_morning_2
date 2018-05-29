package at.tugraz.recipro.recipes.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

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
    private long id;
    
    private String title;
    private int preparationTime;
    private List<RecipeType> recipeTypes;
    private String description;
    private List<RecipeIngredient> ingredients;
    private double rating;
    private int servings;
    
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
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }
    
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }
   
}
