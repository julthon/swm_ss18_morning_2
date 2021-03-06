package at.tugraz.recipro.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.data.RecipeIngredient;
import at.tugraz.recipro.data.Unit;

public class MyPantryListHelper extends AbstractListHelper {
    private static final String DB_NAME = "recipro.mypantry";
    private final String TABLE_NAME = "mypantry";

    private final String COLUMN_ID = "recipe_id";
    private final String COLUMN_NAME = "name";
    private final String COLUMN_QUANTITY = "quantity";
    private final String COLUMN_UNIT = "unit";

    public final String[] COLUMN_TYPES = {"INTEGER PRIMARY KEY", "TEXT", "FLOAT", "TEXT"};

    public MyPantryListHelper(Context context) {
        super(context, DB_NAME);
    }

    protected String[] getColumnNames() {
        return new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_QUANTITY, COLUMN_UNIT};
    }

    protected String[] getColumnTypes() {
        return COLUMN_TYPES;
    }

    protected String getTableName() {
        return TABLE_NAME;
    }

    public void addIngredient(RecipeIngredient ingredient) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.query(TABLE_NAME,
                new String[]{COLUMN_ID},
                COLUMN_ID + "=?",
                new String[]{Integer.toString(ingredient.getIngredient().getId())},
                null,
                null,
                null);
        if (cur.moveToNext()) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_QUANTITY, ingredient.getQuantity());
            db.update(TABLE_NAME, cv, COLUMN_ID + "=?", new String[]{Integer.toString(ingredient.getIngredient().getId())});
        } else {
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(" +
                    ingredient.getIngredient().getId() + ", '" +
                    ingredient.getIngredient().getName() + "', '" +
                    ingredient.getQuantity() + "', '" +
                    ingredient.getUnit().name() + "');");
        }
        cur.close();
        db.close();
    }

    public void removeIngredient(RecipeIngredient ingredient) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME,
                COLUMN_ID + "=?",
                new String[]{Integer.toString(ingredient.getIngredient().getId())});
        db.close();
    }

    public boolean exists(RecipeIngredient ingredient) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.query(TABLE_NAME,
                new String[]{COLUMN_ID},
                COLUMN_ID + "=?",
                new String[]{Integer.toString(ingredient.getIngredient().getId())},
                null,
                null,
                null);

        boolean exists = cur.moveToNext();
        cur.close();
        db.close();
        return exists;
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
        while (cur.moveToNext()) {
            int id = cur.getInt(cur.getColumnIndexOrThrow(COLUMN_ID));
            String name = cur.getString(cur.getColumnIndexOrThrow(COLUMN_NAME));
            float quantity = cur.getFloat(cur.getColumnIndexOrThrow(COLUMN_QUANTITY));
            String unit = cur.getString(cur.getColumnIndexOrThrow(COLUMN_UNIT));

            ingList.add(new RecipeIngredient(new Ingredient(id, name), quantity, Unit.valueOf(unit)));
        }
        cur.close();
        db.close();
        return ingList;
    }
}
