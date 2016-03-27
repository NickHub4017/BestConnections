package DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pasan Eramusugoda on 4/20/2015.
 */
public class OptionDataSource {
    private SQLiteDatabase database;
    private DBSqliteHelper dbHelper;
    private String[] allColumns = {
            DBSqliteHelper.COL_ID,
            DBSqliteHelper.COL_UNIQUE_ID,
            DBSqliteHelper.COL_IS_LIKE,
            DBSqliteHelper.COL_IS_BULLSEYE
    };

    public OptionDataSource(Context context) {
        dbHelper = new DBSqliteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Option createNewEntry(String unique_id, boolean is_like, boolean is_bullseye){
        ContentValues values = new ContentValues();
        values.put(DBSqliteHelper.COL_UNIQUE_ID, unique_id);
        values.put(DBSqliteHelper.COL_IS_LIKE, Boolean.toString(is_like));
        values.put(DBSqliteHelper.COL_IS_BULLSEYE, Boolean.toString(is_bullseye));

        long insertId = database.insert(DBSqliteHelper.TABLE_OPTION, null, values);
        if(insertId != -1) {
            Cursor cursor = database.query(DBSqliteHelper.TABLE_OPTION,
                    allColumns, DBSqliteHelper.COL_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            Option newEntry = cursorToRow(cursor);
            cursor.close();
            return newEntry;
        } else {
            return null;
        }
    }

    public void updateEntry(Option entry){

        ContentValues values = new ContentValues();
        values.put(DBSqliteHelper.COL_IS_LIKE, Boolean.toString(entry.getIs_like()));
        values.put(DBSqliteHelper.COL_IS_BULLSEYE, Boolean.toString(entry.getIs_bullseye()));

        String whereClause = DBSqliteHelper.COL_UNIQUE_ID + " = ?";
        String[] whereArgs = new String[]{
                entry.getUnique_id()
        };

        database.update(DBSqliteHelper.TABLE_OPTION, values, whereClause, whereArgs);
    }

    public void deleteAllEntries() {
        database.delete(DBSqliteHelper.TABLE_OPTION, null, null);
    }

    public void deleteEntry(Option entry) {
        long id = entry.getId();
        System.out.println("Resources deleted with id: " + id);
        database.delete(DBSqliteHelper.TABLE_OPTION, DBSqliteHelper.COL_ID
                + " = " + id, null);
    }

    public List<Option> getAllEntries() {
        List<Option> entryList = new ArrayList<Option>();

        Cursor cursor = database.query(DBSqliteHelper.TABLE_OPTION, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Option entry = cursorToRow(cursor);
            entryList.add(entry);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return entryList;
    }

    public Option getEntry(String unique_id) {
        Option entry = null;

        String whereClause = DBSqliteHelper.COL_UNIQUE_ID+ " = ?";
        String[] whereArgs = new String[]{
                unique_id
        };

        Cursor cursor = database.query(DBSqliteHelper.TABLE_OPTION,
                allColumns, whereClause, whereArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entry = cursorToRow(cursor);
            break;
        }
        // make sure to close the cursor
        cursor.close();
        return entry;
    }

    private Option cursorToRow(Cursor cursor) {
        Option row = new Option();
        row.setId(cursor.getInt(0));
        row.setUnique_id(cursor.getString(1));
        row.setIs_like(Boolean.parseBoolean(cursor.getString(2)));
        row.setIs_bullseye(Boolean.parseBoolean(cursor.getString(3)));
        return row;
    }
}
