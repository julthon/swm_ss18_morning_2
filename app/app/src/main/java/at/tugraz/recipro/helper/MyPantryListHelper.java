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

    public final String table_name = "mypantry";
    public static final String db_name = "recipro.mypantry";

    protected final String[] columns = {"id", "name", "quantity", "unit"};
    protected final String[] columns_type = {"INTEGER PRIMARY KEY", "TEXT", "FLOAT", "TEXT"};

    public MyPantryListHelper(Context context) {
        super(context, db_name);
        addIngredient(new RecipeIngredient(new Ingredient(1, "Banane"), 12));
    }

    protected String[] getColumnNames(){
        return columns;
    }

    protected String[] getColumnTypes(){
        return columns_type;
    }

    protected String getTableName(){
        return table_name;
    }

    public void addIngredient(RecipeIngredient ingredient) {
        // check if ingredient is already there
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.query(table_name,
                new String[]{columns[0]},
                columns[0] + "=?",
                new String[]{Integer.toString(ingredient.getIngredient().getId())},
                null,
                null,
                null);
        if(cur.moveToNext()) {
            // found element
            int oldValue = cur.getInt(cur.getColumnIndexOrThrow(columns[0]));

            ContentValues cv = new ContentValues();
            cv.put(columns[2], ingredient.getQuantity() + oldValue);
            db.update(table_name, cv, columns[0] + "=?", null);
        } else {
            // nothing found, insert new element
            db.execSQL("INSERT INTO " + table_name + " VALUES(" +
                    ingredient.getIngredient().getId() + ", '" +
                    ingredient.getIngredient().getName() + "', '" +
                    ingredient.getQuantity() + "', '" +
                    ingredient.getUnit() + "');");
        }
    }

    public void removeIngredient(RecipeIngredient ingredient) {
        getWritableDatabase().delete(table_name,
                "id=?",
                new String[]{Integer.toString(ingredient.getIngredient().getId())});
    }

    public boolean isPresent(RecipeIngredient ingredient) {
        return getReadableDatabase().query(table_name,
                                           new String[]{columns[0]},
                                           "id=?",
                                           new String[]{Integer.toString(ingredient.getIngredient().getId())},
                                           null,
                                           null,
                                           null)
                                    .moveToNext();
    }

    public List<RecipeIngredient> getIngredients() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.query(table_name,
                              new String[]{columns[0], columns[1], columns[2], columns[3]},
                              null,
                              null,
                              null,
                              null,
                columns[1]);
        ArrayList<RecipeIngredient> ingList = new ArrayList<>();
        while(cur.moveToNext()) {
            int id = cur.getInt(cur.getColumnIndexOrThrow(columns[0]));
            String name = cur.getString(cur.getColumnIndexOrThrow(columns[1]));
            float quantity = cur.getFloat(cur.getColumnIndexOrThrow(columns[2]));
            String unit = cur.getString(cur.getColumnIndexOrThrow(columns[3]));

            ingList.add(new RecipeIngredient(new Ingredient(id, name), quantity, Unit.valueOf(unit)));
        }
        return ingList;
    }
}
