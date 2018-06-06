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
public class MyPantryListHelperTest {
    private MyPantryListHelper helper;

    @Before
    public void fillSearchResultList() {
        this.helper = new MyPantryListHelper(RuntimeEnvironment.application);
        this.helper.clear();
        this.helper.addIngredient(new RecipeIngredient(new Ingredient(1, "Predefined 1"), 100, Unit.GRAM));
        this.helper.addIngredient(new RecipeIngredient(new Ingredient(2, "Predefined 2"), 200, Unit.GRAM));
    }

    @After
    public void tearDown() {
        this.helper.clear();
    }

    @Test
    public void checkInitialIngredients() {
        Assert.assertEquals(2, this.helper.getIngredients().size());
    }

    @Test
    public void insertIngredient() {
        this.helper.addIngredient(new RecipeIngredient(new Ingredient(3, "inserted ing"), 11f, Unit.GRAM));
        Assert.assertEquals(3, this.helper.getIngredients().size());
    }

    @Test
    public void updateIngredient() {
        RecipeIngredient updated_ingredient = new RecipeIngredient(new Ingredient(2, "Predefined 2"), 300, Unit.GRAM);
        this.helper.addIngredient(updated_ingredient);
        Assert.assertEquals(2, this.helper.getIngredients().size());

        for (RecipeIngredient ingredient : this.helper.getIngredients()) {
            if (ingredient.getIngredient().getId() == 2) {
                Assert.assertEquals(300.0, ingredient.getQuantity(), 0.01);
            }
        }
    }

    @Test
    public void removeIngredient() {
        this.helper.removeIngredient(new RecipeIngredient(new Ingredient(1, "don't care"), 1f, Unit.GRAM));
        Assert.assertEquals(1, this.helper.getIngredients().size());
    }

    @Test
    public void ingredientExists() {
        Assert.assertTrue(this.helper.exists(new RecipeIngredient(new Ingredient(1, "don't care"), 1f, Unit.GRAM)));
        Assert.assertTrue(this.helper.exists(new RecipeIngredient(new Ingredient(2, "don't care"), 1f, Unit.GRAM)));
    }

    @Test
    public void ingredientNotExists() {
        Assert.assertFalse(this.helper.exists(new RecipeIngredient(new Ingredient(32794, "don't care"), 1f, Unit.GRAM)));
    }
}