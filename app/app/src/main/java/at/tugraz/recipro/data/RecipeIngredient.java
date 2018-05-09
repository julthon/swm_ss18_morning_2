package at.tugraz.recipro.data;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredient that = (RecipeIngredient) o;
        return Objects.equals(ingredient, that.ingredient) &&
                Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient, quantity);
    }
}
