package at.tugraz.recipro.helper;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.data.Unit;

@RunWith(RobolectricTestRunner.class)
public class GroceryListHelperTest {
    private GroceryListHelper helper;

    @Before
    public void initTestData() {
        this.helper = new GroceryListHelper(RuntimeEnvironment.application);
        this.helper.clear();

        for (int i = 1; i <= 10; i++) {
            RecipeIngredient ingredient = new RecipeIngredient(new Ingredient(i, "Ingredient" + i), i, Unit.GRAM);
            this.helper.addIngredient(ingredient);
        }
    }

    @After
    public void tearDown() {
        this.helper.clear();
    }

    @Test
    public void checkInitialIngredients() {
        Assert.assertEquals(10, this.helper.getIngredients().size());
    }

    @Test
    public void insertIngredient() {
        this.helper.addIngredient(new RecipeIngredient(new Ingredient(11, "inserted ing"), 11f, Unit.GRAM));
        Assert.assertEquals(11, this.helper.getIngredients().size());
    }

    @Test
    public void updateIngredient() {
        RecipeIngredient updated_ingredient = new RecipeIngredient(new Ingredient(2, "Ingredient2"), 300.0f, Unit.GRAM);
        this.helper.addIngredient(updated_ingredient);
        Assert.assertEquals(10, this.helper.getIngredients().size());

        for (RecipeIngredient ingredient : this.helper.getIngredients()) {
            if (ingredient.getIngredient().getId() == 2) {
                Assert.assertEquals(302.0, ingredient.getQuantity(), 0.01);
            }
        }
    }

    @Test
    public void removeIngredient() {
        this.helper.removeIngredient(new RecipeIngredient(new Ingredient(9, "don't care"), 1f, Unit.GRAM));
        this.helper.removeIngredient(new RecipeIngredient(new Ingredient(10, "don't care"), 1f, Unit.GRAM));
        Assert.assertEquals(8, this.helper.getIngredients().size());
    }

    @Test
    public void ingredientExists() {
        Assert.assertTrue(this.helper.exists(new RecipeIngredient(new Ingredient(9, "don't care"), 1f, Unit.GRAM)));
    }

    @Test
    public void ingredientNotExists() {
        Assert.assertFalse(this.helper.exists(new RecipeIngredient(new Ingredient(32794, "don't care"), 1f, Unit.GRAM)));
    }
}