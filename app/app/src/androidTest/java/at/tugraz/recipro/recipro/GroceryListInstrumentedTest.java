package at.tugraz.recipro.grocerylist;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.thoughtworks.xstream.converters.extended.SqlDateConverter;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.helper.GroceryListHelper;

public class GroceryListTest {

    GroceryListHelper helper;

    @Before
    public void insertTestData() {
        helper = new GroceryListHelper(null);
        SQLiteDatabase db = helper.getWritableDatabase();
    }

    @Test
    public void insertIngredientsManual() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        for(int i = 0; i < 10; i++) {
            ContentValues values = new ContentValues();
            values.put(helper.COLUMN_ID, i);
            values.put(helper.COLUMN_NAME, "Ingredient" + i);
            values.put(helper.COLUMN_QUANTITY, i);
            values.put(helper.COLUMN_UNIT, Character.toString((char) ('a' + i)));
            db.insert(helper.TABLE_NAME, null, values);
        }
        db.endTransaction();
        Assert.assertEquals(helper.getIngredients().size(), 10);
    }

    @Test
    public void insertIngredient() {
        helper.addIngredient(new RecipeIngredient(new Ingredient(11, "inserted ing"), "11", "zzz"));
        Assert.assertEquals(helper.getIngredients().size(), 11);
    }

    @Test
    public void removeIngredient() {
        helper.removeIngredient(new RecipeIngredient(new Ingredient(10, "don't care"), "only id is important", "not even this one"));
        helper.removeIngredient(new RecipeIngredient(new Ingredient(11, "don't care"), "only id is important", "not even this one"));
        Assert.assertEquals(helper.getIngredients().size(), 9);
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
