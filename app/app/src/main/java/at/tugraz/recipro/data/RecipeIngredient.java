package at.tugraz.recipro.data;

import java.io.Serializable;

public class RecipeIngredient implements Serializable{
    private Ingredient ingredient;
    private String quantity;

    public RecipeIngredient(Ingredient ingredient, String quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
