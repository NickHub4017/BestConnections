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
public class SectionDataSource {
    private SQLiteDatabase database;
    private DBSqliteHelper dbHelper;
    private String[] allColumns = {
            DBSqliteHelper.COL_ID,
            DBSqliteHelper.COL_SECTION_ID,
            DBSqliteHelper.COL_SECTION_DES,
            DBSqliteHelper.COL_LAST_QUOTE,
            DBSqliteHelper.COL_SECTION_FINISHED,
            DBSqliteHelper.COL_LEFT_VIEW
    };

    public SectionDataSource(Context context) {
        dbHelper = new DBSqliteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Section createNewEntry(String section_id, String des){
        ContentValues values = new ContentValues();
        values.put(DBSqliteHelper.COL_SECTION_ID, section_id);
        values.put(DBSqliteHelper.COL_SECTION_DES, des);
        values.put(DBSqliteHelper.COL_LAST_QUOTE, "");
        values.put(DBSqliteHelper.COL_SECTION_FINISHED, "false");
        values.put(DBSqliteHelper.COL_LEFT_VIEW, -1);

        long insertId = database.insert(DBSqliteHelper.TABLE_SECTION, null, values);
        if(insertId != -1) {
            Cursor cursor = database.query(DBSqliteHelper.TABLE_SECTION,
                    allColumns, DBSqliteHelper.COL_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            Section newEntry = cursorToRow(cursor);
            cursor.close();
            return newEntry;
        } else {
            return null;
        }
    }

    public void deleteAllEntries() {
        database.delete(DBSqliteHelper.TABLE_SECTION, null, null);
    }

    public void deleteEntry(Section entry) {
        long id = entry.getId();
        System.out.println("Resources deleted with id: " + id);
        database.delete(DBSqliteHelper.TABLE_SECTION, DBSqliteHelper.COL_ID
                + " = " + id, null);
    }

    public void updateEntry(Section entry) {

        ContentValues values = new ContentValues();
        values.put(DBSqliteHelper.COL_LAST_QUOTE, entry.getLast_quote());
        // Commented since once section is finished it keep updating as false from here
        // values.put(DBSqliteHelper.COL_SECTION_FINISHED, Boolean.toString(entry.getSection_finished()));

        String whereClause = DBSqliteHelper.COL_SECTION_ID + " = ?";
        String[] whereArgs = new String[]{
                entry.getSection_id()
        };

        int result = database.update(DBSqliteHelper.TABLE_SECTION, values, whereClause, whereArgs);
    }

    public void updateEntry(String sectionId, boolean isFinished) {

        ContentValues values = new ContentValues();
        values.put(DBSqliteHelper.COL_SECTION_FINISHED, Boolean.toString(isFinished));

        String whereClause = DBSqliteHelper.COL_SECTION_ID + " = ?";
        String[] whereArgs = new String[]{
                sectionId
        };

        int result = database.update(DBSqliteHelper.TABLE_SECTION, values, whereClause, whereArgs);
    }

    public void updateEntry(String sectionId, int leftView) {

        ContentValues values = new ContentValues();
        values.put(DBSqliteHelper.COL_LEFT_VIEW, Integer.toString(leftView));

        String whereClause = DBSqliteHelper.COL_SECTION_ID + " = ?";
        String[] whereArgs = new String[]{
                sectionId
        };

        int result = database.update(DBSqliteHelper.TABLE_SECTION, values, whereClause, whereArgs);
    }

    public List<Section> getAllEntries() {
        List<Section> entryList = new ArrayList<Section>();

        Cursor cursor = database.query(DBSqliteHelper.TABLE_SECTION, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Section entry = cursorToRow(cursor);
            entryList.add(entry);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return entryList;
    }

    public Section getEntry(String section_id) {
        Section entry = null;

        String whereClause = DBSqliteHelper.COL_SECTION_ID + " = ?";
        String[] whereArgs = new String[]{
                section_id
        };

        Cursor cursor = database.query(DBSqliteHelper.TABLE_SECTION,
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

    private Section cursorToRow(Cursor cursor) {
        Section row = new Section();
        row.setId(cursor.getInt(0));
        row.setSection_id(cursor.getString(1));
        row.setSection_des(cursor.getString(2));
        row.setLast_quote(cursor.getString(3));
        row.setSection_finished(Boolean.parseBoolean(cursor.getString(4)));
        row.setLeft_view(cursor.getInt(5));
        return row;
    }
}
