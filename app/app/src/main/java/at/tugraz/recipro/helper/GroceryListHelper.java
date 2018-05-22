package at.tugraz.recipro.helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.recipro.GroceryListActivity;

public class GroceryListHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DB_NAME = "recipro.db";
    public static final String TABLE_NAME = "grocery";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";

    public GroceryListHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                   COLUMN_ID +       " INTEGER PRIMARY KEY, " +
                   COLUMN_NAME +     " TEXT,                " +
                   COLUMN_QUANTITY + " TEXT,             " +
                   COLUMN_UNIT +     " TEXT                 " +
                   ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addIngredient(RecipeIngredient ingredient) {
        // check if ingredient is already there
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.query(TABLE_NAME,
                new String[]{COLUMN_ID},
                COLUMN_ID + "=?",
                new String[]{Integer.toString(ingredient.getIngredient().getId())},
                null,
                null,
                null);
        if(cur.moveToNext()) {
            // found element
            int oldValue = cur.getInt(cur.getColumnIndexOrThrow(COLUMN_ID));

            ContentValues cv = new ContentValues();
            cv.put(COLUMN_QUANTITY, ingredient.getQuantity() + oldValue);
            db.update(TABLE_NAME, cv, COLUMN_ID + "=?", null);
        } else {
            // nothing found, insert new element
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(" +
                    ingredient.getIngredient().getId() + ", '" +
                    ingredient.getIngredient().getName() + "', '" +
                    ingredient.getQuantity() + "', '" +
                    ingredient.getUnit() + "');");
        }
    }

    public void removeIngredient(RecipeIngredient ingredient) {
        getWritableDatabase().delete(TABLE_NAME,
                "id=?",
                new String[]{Integer.toString(ingredient.getIngredient().getId())});
    }

    public boolean isPresent(RecipeIngredient ingredient) {
        return getReadableDatabase().query(TABLE_NAME,
                                           new String[]{COLUMN_ID},
                                           "id=?",
                                           new String[]{Integer.toString(ingredient.getIngredient().getId())},
                                           null,
                                           null,
                                           null)
                                    .moveToNext();
    }

    public List<RecipeIngredient> getIngredients() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.query(TABLE_NAME,
                              new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_QUANTITY, COLUMN_UNIT},
                              null,
                              null,
                              null,
                              null,
                              COLUMN_NAME);
        ArrayList<RecipeIngredient> ingList = new ArrayList<>();
        while(cur.moveToNext()) {
            int id = cur.getInt(cur.getColumnIndexOrThrow(COLUMN_ID));
            String name = cur.getString(cur.getColumnIndexOrThrow(COLUMN_NAME));
            String quantity = cur.getString(cur.getColumnIndexOrThrow(COLUMN_QUANTITY));
            String unit = cur.getString(cur.getColumnIndexOrThrow(COLUMN_UNIT));

            ingList.add(new RecipeIngredient(new Ingredient(id, name), quantity, unit));
        }
        return ingList;
    }
}
