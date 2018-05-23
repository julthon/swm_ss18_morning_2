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
    private String quantity;
    
    @Id
    @GeneratedValue
    @JsonbTransient
    private long id;

    public RecipeIngredient() {
    }

    
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

    public long getId() {
        return id;
    }
}
