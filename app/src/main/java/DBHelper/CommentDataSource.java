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
public class CommentDataSource {
    private SQLiteDatabase database;
    private DBSqliteHelper dbHelper;
    private String[] allColumns = {
            DBSqliteHelper.COL_ID,
            DBSqliteHelper.COL_UNIQUE_ID,
            DBSqliteHelper.COL_SECTION_ID,
            DBSqliteHelper.COL_COMMENT
    };

    public CommentDataSource(Context context) {
        dbHelper = new DBSqliteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Comment createNewEntry(String section_id, String unique_id, String comment){
        ContentValues values = new ContentValues();
        values.put(DBSqliteHelper.COL_SECTION_ID, section_id);
        values.put(DBSqliteHelper.COL_UNIQUE_ID, unique_id);
        values.put(DBSqliteHelper.COL_COMMENT, comment);

        long insertId = database.insert(DBSqliteHelper.TABLE_COMMENT, null, values);
        if(insertId != -1) {
            Cursor cursor = database.query(DBSqliteHelper.TABLE_COMMENT,
                    allColumns, DBSqliteHelper.COL_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            Comment newEntry = cursorToRow(cursor);
            cursor.close();
            return newEntry;
        } else {
            return null;
        }
    }

    public void updateEntry(Comment entry) {

        ContentValues values = new ContentValues();
        values.put(DBSqliteHelper.COL_COMMENT, entry.getComment());


        String whereClause = DBSqliteHelper.COL_UNIQUE_ID + " = ? AND " + DBSqliteHelper.COL_SECTION_ID + " = ?";
        String[] whereArgs = new String[]{
                entry.getUnique_id(),
                entry.getSection_id()
        };

        database.update(DBSqliteHelper.TABLE_COMMENT, values, whereClause, whereArgs);
    }

    public void deleteAllEntries() {
        database.delete(DBSqliteHelper.TABLE_COMMENT, null, null);
    }

    public void deleteEntry(Comment entry) {
        long id = entry.getId();
        System.out.println("Resources deleted with id: " + id);
        database.delete(DBSqliteHelper.TABLE_COMMENT, DBSqliteHelper.COL_ID
                + " = " + id, null);
    }

    public List<Comment> getAllEntries() {
        List<Comment> entryList = new ArrayList<Comment>();

        Cursor cursor = database.query(DBSqliteHelper.TABLE_COMMENT, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Comment entry = cursorToRow(cursor);
            entryList.add(entry);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return entryList;
    }

    public List<Comment> getAllEntries(String section_id) {
        List<Comment> entryList = new ArrayList<Comment>();

        String whereClause = DBSqliteHelper.COL_SECTION_ID + " = ?";
        String[] whereArgs = new String[]{
                section_id
        };

        Cursor cursor = database.query(DBSqliteHelper.TABLE_COMMENT,
                allColumns, whereClause, whereArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Comment entry = cursorToRow(cursor);
            entryList.add(entry);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return entryList;
    }

    public Comment getEntry(String section_id, String unique_id) {
        Comment entry = null;

        String whereClause = DBSqliteHelper.COL_SECTION_ID + " = ? AND " + DBSqliteHelper.COL_UNIQUE_ID + " = ?";
        String[] whereArgs = new String[]{
                section_id,
                unique_id
        };

        Cursor cursor = database.query(DBSqliteHelper.TABLE_COMMENT,
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

    public Comment getEntryByComment(String section_id, String comment) {
        Comment entry = null;

        String whereClause = DBSqliteHelper.COL_SECTION_ID + " = ? AND " + DBSqliteHelper.COL_QUESTION + " = ?";
        String[] whereArgs = new String[]{
                section_id,
                comment
        };

        Cursor cursor = database.query(DBSqliteHelper.TABLE_COMMENT,
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

    private Comment cursorToRow(Cursor cursor) {
        Comment row = new Comment();
        row.setId(cursor.getInt(0));
        row.setUnique_id(cursor.getString(1));
        row.setSection_id(cursor.getString(2));
        row.setComment(cursor.getString(3));
        return row;
    }
}
