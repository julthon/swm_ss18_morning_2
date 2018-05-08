package at.tugraz.recipro.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable{

    @SerializedName("title")
    private String title;

    @SerializedName("preparationTime")
    private int time;

    @SerializedName("rating")
    private double rating;

    @SerializedName("description")
    private String description;

    @SerializedName("ingredients")
    private List<RecipeIngredient> ingredients;

    public Recipe(String title, int time, double rating, List<RecipeIngredient> ingredients, String description) {
        this.title = title;
        this.time = time;
        this.rating = rating;
        this.ingredients = ingredients;
        this.description = description;
    }

    public Recipe() {
        ingredients = new ArrayList<>();
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
