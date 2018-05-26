package at.tugraz.recipro.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

import at.tugraz.recipro.recipro.R;

public class RecipeIngredient implements Serializable {
    @SerializedName("ingredient")
    private Ingredient ingredient;
    @SerializedName("quantity")
    private float quantity;
    @SerializedName("unit")
    private Unit unit;

    public RecipeIngredient(Ingredient ingredient, float quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = Unit.NONE;
    }

    public RecipeIngredient(Ingredient ingredient, float quantity, Unit unit) {
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

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
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
