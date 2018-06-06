package at.tugraz.recipro.data;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class IngredientTest {

    private Ingredient ingredient;

    private final int ID = 666;
    private final String NAME = "SATAN";
    private final List<Allergen> ALLERGENS = new ArrayList<Allergen>() {{
        add(new Allergen("allergenShortName", "allergenName", "allergenDescription"));
    }};

    @Before
    public void setUpIngredient() {
        this.ingredient = new Ingredient(1, "Primus", ALLERGENS);
    }

    @Test
    public void testIngredientsGetterStuff() {
        int ingredient_id = this.ingredient.getId();
        String ingredient_name = this.ingredient.getName();
        List<Allergen> allergen = this.ingredient.getAllergens();

        assertEquals(1, ingredient_id);
        assertEquals("Primus", ingredient_name);
        assertThat(allergen.size(), is(1));
    }

    @Test
    public void testIngredientsSetterStuff() {
        List<Allergen> newAllergens = new ArrayList<>();
        newAllergens.add(new Allergen("shortName1", "name1", "description1"));
        newAllergens.add(new Allergen("shortName2", "name2", "description2"));

        this.ingredient.setId(ID);
        this.ingredient.setName(NAME);
        this.ingredient.setAllergens(newAllergens);

        int ingredient_id = this.ingredient.getId();
        String ingredient_name = this.ingredient.getName();

        assertEquals(666, ingredient_id);
        assertEquals("SATAN", ingredient_name);
        assertThat(this.ingredient.getAllergens().size(), is(2));
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