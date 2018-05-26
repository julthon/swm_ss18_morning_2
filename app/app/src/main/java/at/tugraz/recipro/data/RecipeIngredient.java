package at.tugraz.recipro.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class RecipeIngredient implements Serializable {
    @SerializedName("ingredient")
    private Ingredient ingredient;
    @SerializedName("quantity")
    private String quantity;
    @SerializedName("unit")
    private String unit;

    public RecipeIngredient(Ingredient ingredient, String quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public RecipeIngredient(Ingredient ingredient, String quantity, String unit) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredient that = (RecipeIngredient) o;
        return Objects.equals(ingredient, that) &&
                Objects.equals(quantity, that.quantity);
    }
}
