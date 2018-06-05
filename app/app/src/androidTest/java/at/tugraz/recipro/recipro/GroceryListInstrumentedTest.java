package at.tugraz.recipro.recipro;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;

import junit.framework.Assert;

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

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);
    private GroceryListHelper helper;

    private List<String> ingredientNames = new ArrayList<>();

    @Before
    public void initTestData() {
        helper = new GroceryListHelper(mActivityRule.getActivity());
        // clear db
        helper.onDowngrade(helper.getWritableDatabase(), 0, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        for (int i = 1; i <= 10; i++) {
            ContentValues values = new ContentValues();
            values.put(helper.columns[0], i);
            values.put(helper.columns[1], "Ingredient" + i);
            values.put(helper.columns[2], i);
            values.put(helper.columns[3], Unit.GRAM.name());
            db.insert(helper.table_name, null, values);
            ingredientNames.add("Ingredient" + i);
        }
        onView(withId(R.id.dlDrawer)).perform(DrawerActions.open());
        onView(withText("Grocery List")).perform(click());
    }

    @Test
    public void checkIngredientsInit() {
        Assert.assertEquals(10, helper.getIngredients().size());
    }

    @Test
    public void insertIngredient() {
        helper.addIngredient(new RecipeIngredient(new Ingredient(11, "inserted ing"), 11f, Unit.GRAM));
        Assert.assertEquals(11, helper.getIngredients().size());
    }

    @Test
    public void removeIngredient() {
        helper.removeIngredient(new RecipeIngredient(new Ingredient(9, "don't care"), 1f, Unit.GRAM));
        helper.removeIngredient(new RecipeIngredient(new Ingredient(10, "don't care"), 1f, Unit.GRAM));
        Assert.assertEquals(8, helper.getIngredients().size());
    }

    @Test
    public void checkIngredientExistIfExists() {
        Assert.assertTrue(helper.isPresent(new RecipeIngredient(new Ingredient(9, "don't care"), 1f, Unit.GRAM)));
    }

    @Test
    public void checkIngredientExistIfNotExists() {
        Assert.assertFalse(helper.isPresent(new RecipeIngredient(new Ingredient(32794, "don't care"), 1f, Unit.GRAM)));
    }


    @Test
    public void checkAllIngredientsVisible() {
        ingredientNames.forEach(in -> {
            onView(withText(in)).check(matches(isDisplayed()));
        });
    }
}
