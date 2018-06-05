package at.tugraz.recipro.data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RecipeIngredientTest {

    private RecipeIngredient recipe_ingredient;

    private Ingredient ingredient;

    private Unit unit;

    private final float QUANTITY = 999.99f;
    private final int ID = 666;
    private final String NAME = "SATAN";

    @Before
    public void setUpRecipeIngredient() {

        this.ingredient = new Ingredient(ID, NAME);

        this.recipe_ingredient = new RecipeIngredient(ingredient, QUANTITY, Unit.MILLILITER);

        this.unit = Unit.MILLILITER;
    }

    @Test
    public void testRecipeIngredientStuff() {
        assertEquals(Unit.MILLILITER, this.recipe_ingredient.getUnit());

        this.recipe_ingredient.setUnit(Unit.GRAM);

        assertEquals(Unit.GRAM, this.recipe_ingredient.getUnit());
    }
}