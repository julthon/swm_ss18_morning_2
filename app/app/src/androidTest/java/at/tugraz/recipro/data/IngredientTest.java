package at.tugraz.recipro.data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IngredientTest {
    private Ingredient ingredient;
    private final String NAME = "Milk";

    @Before
    public void setUp() throws Exception {
        this.ingredient = new Ingredient(NAME);
    }

    @Test
    public void testIngredientSetter(){
        String name = "Chocolate";
        this.ingredient.setName(name);
        assertEquals(this.ingredient.getName(), name);
    }

    @Test
    public void testIngredientGetter(){
        assertEquals(ingredient.getName(), NAME);
    }
}
