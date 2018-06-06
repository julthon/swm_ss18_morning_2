package at.tugraz.recipro.recipes.entity;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author Edith
 */
@Entity
public class RecipeIngredient {
    
    private Ingredient ingredient;
    private float quantity;
    private Unit unit;
    
    @Id
    @GeneratedValue
    @JsonbTransient
    private long id;

    public RecipeIngredient() {
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

    public long getId() {
        return id;
    }
}
