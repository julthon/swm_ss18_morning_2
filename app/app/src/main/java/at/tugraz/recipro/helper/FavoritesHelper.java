package at.tugraz.recipro.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavoritesHelper extends DatabaseHelper {
    public static final String TABLE_NAME = "favorites";
    public static final String COLUMN_RECIPE = "recipe_id";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_RECIPE + " INTEGER PRIMARY KEY" +
            ")";

    public FavoritesHelper(Context context) {
        super(context);
    }

    public void addFavorite(long recipeId) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.query(TABLE_NAME,
                new String[]{COLUMN_RECIPE},
                COLUMN_RECIPE + "=?",
                new String[]{Long.toString(recipeId)},
                null,
                null,
                null);

        if (!cur.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_RECIPE, recipeId);

            db.insert(TABLE_NAME, null, values);
        }
    }

    public void removeFavorite(long recipeId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME,
                COLUMN_RECIPE + "=?",
                new String[]{Long.toString(recipeId)});
    }

    public boolean exists(long recipeId) {
        SQLiteDatabase db = getWritableDatabase();
        return db.query(TABLE_NAME,
                new String[]{COLUMN_RECIPE},
                COLUMN_RECIPE + "=?",
                new String[]{Long.toString(recipeId)},
                null,
                null,
                null).moveToNext();
    }

    public List<Long> getFavorites() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.query(TABLE_NAME,
                new String[]{COLUMN_RECIPE},
                null,
                null,
                null,
                null,
                COLUMN_RECIPE);

        List<Long> recipeList = new ArrayList<>();
        while (cur.moveToNext()) {
            long recipeId = cur.getLong(cur.getColumnIndexOrThrow(COLUMN_RECIPE));

            recipeList.add(recipeId);
        }

        return recipeList;
    }
}
