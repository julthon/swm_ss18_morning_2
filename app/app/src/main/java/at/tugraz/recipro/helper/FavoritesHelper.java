package at.tugraz.recipro.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FavoritesHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DB_NAME = "recipro.db";
    public static final String TABLE_NAME = "favorites";

    public static final String COLUMN_RECIPE = "recipe_id";

    public FavoritesHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("FAVORITES", "CREATE");
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_RECIPE +   " INTEGER PRIMARY KEY " +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
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

        if(!cur.moveToNext()) {
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
