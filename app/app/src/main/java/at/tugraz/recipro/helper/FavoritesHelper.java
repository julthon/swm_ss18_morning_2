package at.tugraz.recipro.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavoritesHelper extends AbstractListHelper {
    private static final String DB_NAME = "recipro.favorites";
    private final String TABLE_NAME = "favorites";

    private final String COLUMN_ID = "recipe_id";
    public final String[] COLUMN_TYPES = {"INTEGER PRIMARY KEY"};

    public FavoritesHelper(Context context) {
        super(context, DB_NAME);
    }

    @Override
    protected String[] getColumnNames() {
        return new String[] {COLUMN_ID};
    }

    @Override
    protected String[] getColumnTypes() {
        return COLUMN_TYPES;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public void addFavorite(long recipeId) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.query(TABLE_NAME,
                new String[]{COLUMN_ID},
                COLUMN_ID + "=?",
                new String[]{Long.toString(recipeId)},
                null,
                null,
                null);

        if (!cur.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, recipeId);

            db.insert(TABLE_NAME, null, values);
        }
        cur.close();
        db.close();
    }

    public void removeFavorite(long recipeId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME,
                COLUMN_ID + "=?",
                new String[]{Long.toString(recipeId)});
        db.close();
    }

    public boolean exists(long recipeId) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.query(TABLE_NAME,
                new String[]{COLUMN_ID},
                COLUMN_ID + "=?",
                new String[]{Long.toString(recipeId)},
                null,
                null,
                null);
        boolean exists = cur.moveToNext();
        cur.close();
        db.close();
        return exists;
    }

    public List<Long> getFavorites() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.query(TABLE_NAME,
                new String[]{COLUMN_ID},
                null,
                null,
                null,
                null,
                COLUMN_ID);

        List<Long> recipeList = new ArrayList<>();
        while (cur.moveToNext()) {
            long recipeId = cur.getLong(cur.getColumnIndexOrThrow(COLUMN_ID));

            recipeList.add(recipeId);
        }

        cur.close();
        db.close();

        return recipeList;
    }
}
