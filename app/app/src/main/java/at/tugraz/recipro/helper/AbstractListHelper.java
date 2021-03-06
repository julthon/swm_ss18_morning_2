package at.tugraz.recipro.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class AbstractListHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    private static final String LOG_PREFIX = AbstractListHelper.class.getName();

    private AbstractListHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    AbstractListHelper(Context context, String name) {
        this(context, name, null, DATABASE_VERSION);
    }

    protected abstract String[] getColumnNames();

    protected abstract String[] getColumnTypes();

    protected abstract String getTableName();

    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] columnNames = getColumnNames();
        String[] columnTypes = getColumnTypes();

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ").append(getTableName()).append(" (");
        for (int i = 0; i < columnNames.length; i++) {
            sb.append(columnNames[i]);
            sb.append(" ");
            sb.append(columnTypes[i]);
            if (i <= (columnNames.length - 2)) {
                sb.append(",");
            }
        }
        sb.append(")");
        Log.i(LOG_PREFIX, sb.toString());
        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + getTableName());
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void clear() {
        onUpgrade(this.getWritableDatabase(), 0, 1);
    }

}
