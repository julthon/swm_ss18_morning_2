package at.tugraz.recipro.data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class IngredientTest {

    private Ingredient ingredient;

    private final int ID = 666;
    private final String NAME = "SATAN";

    @Before
    public void setUpIngredient() {
        this.ingredient = new Ingredient(1, "Primus");
    }

    @Test
    public void testIngredientsGetterStuff() {
        int ingredient_id = this.ingredient.getId();
        String ingredient_name = this.ingredient.getName();

        assertEquals(1, ingredient_id);
        assertEquals("Primus", ingredient_name);
    }

    @Test
    public void testIngredientsSetterStuff() {
        this.ingredient.setId(ID);
        this.ingredient.setName(NAME);

        int ingredient_id = this.ingredient.getId();
        String ingredient_name = this.ingredient.getName();

        assertEquals(666, ingredient_id);
        assertEquals("SATAN", ingredient_name);
    }

    @Test
    public void testIngredientsToStringStuff() {

        assertEquals("Primus", ingredient.toString());
    }

    @Test
    public void testIngredientsEqualsStuff() {
        this.ingredient.setId(ID);
        this.ingredient.setName(NAME);

        Ingredient special_snowflake = new Ingredient(1, "nothing");

        assertNotEquals(this.ingredient, special_snowflake);

        assertEquals(this.ingredient, this.ingredient);
        assertNotEquals("", this.ingredient);

        special_snowflake.setId(ID);
        special_snowflake.setName(NAME);

        assertEquals(this.ingredient, special_snowflake);
    }

    @Test
    public void testIngredientHashCodeStuff() {
        this.ingredient.setId(ID);
        this.ingredient.setName(NAME);

        Ingredient special_snowflake = new Ingredient(1, "nothing");

        special_snowflake.setId(ID);
        special_snowflake.setName(NAME);

        int hash_code_one = this.ingredient.hashCode();
        int hash_code_two = this.ingredient.hashCode();

        assertEquals(hash_code_one, hash_code_two);
    }
}