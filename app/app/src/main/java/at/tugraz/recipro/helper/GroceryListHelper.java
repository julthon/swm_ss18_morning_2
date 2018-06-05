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

public class GroceryListHelper extends DatabaseHelper {
    public static final String TABLE_NAME = "grocery";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                                              COLUMN_ID +       " INTEGER PRIMARY KEY, " +
                                              COLUMN_NAME +     " TEXT,                " +
                                              COLUMN_QUANTITY + " FLOAT,               " +
                                              COLUMN_UNIT +     " TEXT                 " +
                                              ")";

    public GroceryListHelper(Context context) {
        super(context);
    }

    public void addIngredient(RecipeIngredient ingredient) {
        // check if ingredient is already there
        SQLiteDatabase db = getWritableDatabase();
        String[] idArg = {Integer.toString(ingredient.getIngredient().getId())};
        Cursor cur = db.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_QUANTITY},
                COLUMN_ID + "=?",
                idArg,
                null,
                null,
                null);
        if(cur.moveToNext()) {
            // found element
            int oldValue = cur.getInt(cur.getColumnIndexOrThrow(COLUMN_QUANTITY));

            ContentValues cv = new ContentValues();
            cv.put(COLUMN_QUANTITY, ingredient.getQuantity() + oldValue);
            db.update(TABLE_NAME, cv, COLUMN_ID + "=?", idArg);
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
            float quantity = cur.getFloat(cur.getColumnIndexOrThrow(COLUMN_QUANTITY));
            String unit = cur.getString(cur.getColumnIndexOrThrow(COLUMN_UNIT));

            ingList.add(new RecipeIngredient(new Ingredient(id, name), quantity, Unit.valueOf(unit)));
        }
        return ingList;
    }
}
