package at.tugraz.recipro.data;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable{

    private String title;
    private int time;
    private double rating;
    private  String description;
    private List<RecipeIngredient> ingredients;

    public Recipe(String title, int time, double rating, List<RecipeIngredient> ingredients, String description) {
        this.title = title;
        this.time = time;
        this.rating = rating;
        this.ingredients = ingredients;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredientList) {
        this.ingredients = ingredientList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
