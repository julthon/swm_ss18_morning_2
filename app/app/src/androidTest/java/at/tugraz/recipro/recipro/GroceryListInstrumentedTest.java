package at.tugraz.recipro.recipro;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.data.Unit;
import at.tugraz.recipro.helper.GroceryListHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class GroceryListInstrumentedTest {
    private GroceryListHelper helper;
    private List<String> ingredientNames = new ArrayList<>();

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initTestData() {
        this.helper = new GroceryListHelper(mActivityRule.getActivity());
        this.helper.clear();

        for (int i = 1; i <= 10; i++) {
            RecipeIngredient ingredient = new RecipeIngredient(new Ingredient(i, "Ingredient" + i), i, Unit.GRAM);
            this.helper.addIngredient(ingredient);
            ingredientNames.add(ingredient.getIngredient().getName());
        }

        onView(withId(R.id.dlDrawer)).perform(DrawerActions.open());
        onView(withText("Grocery List")).perform(click());
    }

    @After
    public void tearDown() throws Exception {
        this.helper.clear();
    }

    @Test
    public void ingredientsInit() {
        Assert.assertEquals(10, this.helper.getIngredients().size());
    }

    @Test
    public void insertIngredient() {
        this.helper.addIngredient(new RecipeIngredient(new Ingredient(11, "inserted ing"), 11f, Unit.GRAM));
        Assert.assertEquals(11, this.helper.getIngredients().size());
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

    @Test
    public void allIngredientsVisible() {
        this.ingredientNames.forEach(in -> {
            onView(withText(in)).check(matches(isDisplayed()));
        });
    }
}
