package at.tugraz.recipro.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("preparationTime")
    private int time;

    @SerializedName("servings")
    private int servings;

    @SerializedName("rating")
    private double rating;

    @SerializedName("description")
    private String description;

    @SerializedName("ingredients")
    private List<RecipeIngredient> ingredients;

    public Recipe(long id, String title, int time, int servings, double rating, List<RecipeIngredient> ingredients, String description) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.servings = servings;
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

    public void setServings(int servings) {
        this.servings = servings;
    }

    public double getRating() {
        return rating;
    }

    public int getServings() {
        return servings;
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

    public void setId(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
