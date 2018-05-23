package at.tugraz.recipro.recipro;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.rule.ActivityTestRule;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.helper.GroceryListHelper;

public class GroceryListInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);
    GroceryListHelper helper;

    @Before
    public void initTestData() {
        helper = new GroceryListHelper(mActivityRule.getActivity());
        // clear db
        helper.onDowngrade(helper.getWritableDatabase(), 0, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        for(int i = 1; i <= 10; i++) {
            ContentValues values = new ContentValues();
            values.put(helper.COLUMN_ID, i);
            values.put(helper.COLUMN_NAME, "Ingredient" + i);
            values.put(helper.COLUMN_QUANTITY, i);
            values.put(helper.COLUMN_UNIT, Character.toString((char) ('a' + i)));
            db.insert(helper.TABLE_NAME, null, values);
        }
    }

    @Test
    public void checkIngredientsInit() {
        Assert.assertEquals(10, helper.getIngredients().size());
    }

    @Test
    public void insertIngredient() {
        helper.addIngredient(new RecipeIngredient(new Ingredient(11, "inserted ing"), "11", "zzz"));
        Assert.assertEquals(11, helper.getIngredients().size());
    }

    @Test
    public void removeIngredient() {
        helper.removeIngredient(new RecipeIngredient(new Ingredient(9, "don't care"), "only id is important", "not even this one"));
        helper.removeIngredient(new RecipeIngredient(new Ingredient(10, "don't care"), "only id is important", "not even this one"));
        Assert.assertEquals(8, helper.getIngredients().size());
    }

    @Test
    public void checkIngredientExistIfExists() {
        Assert.assertTrue(helper.isPresent(new RecipeIngredient(new Ingredient(9, "don't care"), "even here we only check the id", "blaaaaa")));
    }

    @Test
    public void checkIngredientExistIfNotExists() {
        Assert.assertFalse(helper.isPresent(new RecipeIngredient(new Ingredient(32794, "don't care"), "even here we only check the id", "blaaaaa")));
    }
}
