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
public class QuizDataSource {
    private SQLiteDatabase database;
    private DBSqliteHelper dbHelper;
    private String[] allColumns = {
            DBSqliteHelper.COL_ID,
            DBSqliteHelper.COL_SECTION_ID,
            DBSqliteHelper.COL_QUIZ,
            DBSqliteHelper.COL_VALUE
    };

    public QuizDataSource(Context context) {
        dbHelper = new DBSqliteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Quiz createNewEntry(String section_id, String quiz, int value){
        ContentValues values = new ContentValues();
        values.put(DBSqliteHelper.COL_SECTION_ID, section_id);
        values.put(DBSqliteHelper.COL_QUIZ, quiz);
        values.put(DBSqliteHelper.COL_VALUE, Integer.toString(value));

        long insertId = database.insert(DBSqliteHelper.TABLE_QUIZ, null, values);
        if(insertId != -1) {
            Cursor cursor = database.query(DBSqliteHelper.TABLE_QUIZ,
                    allColumns, DBSqliteHelper.COL_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            Quiz newEntry = cursorToRow(cursor);
            cursor.close();
            return newEntry;
        } else {
            return null;
        }
    }

    public void updateEntry(Quiz entry){
        ContentValues values = new ContentValues();
        values.put(DBSqliteHelper.COL_VALUE, Integer.toString(entry.getValue()));

        String whereClause = DBSqliteHelper.COL_SECTION_ID + " = ? AND " + DBSqliteHelper.COL_ID + " = ?";
        String[] whereArgs = new String[]{
                entry.getSection_id(),
                Integer.toString(entry.getId())
        };

        long insertId = database.update(DBSqliteHelper.TABLE_QUIZ, values, whereClause, whereArgs);
    }

    public void deleteAllEntries() {
        database.delete(DBSqliteHelper.TABLE_QUIZ, null, null);
    }

    public void deleteEntry(Quiz entry) {
        long id = entry.getId();
        System.out.println("Resources deleted with id: " + id);
        database.delete(DBSqliteHelper.TABLE_QUIZ, DBSqliteHelper.COL_ID
                + " = " + id, null);
    }

    public List<Quiz> getAllEntries() {
        List<Quiz> entryList = new ArrayList<Quiz>();

        Cursor cursor = database.query(DBSqliteHelper.TABLE_QUIZ, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Quiz entry = cursorToRow(cursor);
            entryList.add(entry);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return entryList;
    }

    public List<Quiz> getAllEntries(String section_id) {
        List<Quiz> entryList = new ArrayList<Quiz>();

        String whereClause = DBSqliteHelper.COL_SECTION_ID + " = ?";
        String[] whereArgs = new String[]{
                section_id
        };

        Cursor cursor = database.query(DBSqliteHelper.TABLE_QUIZ,
                allColumns, whereClause, whereArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Quiz entry = cursorToRow(cursor);
            entryList.add(entry);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return entryList;
    }

    public Quiz getEntry(String section_id) {
        Quiz entry = null;

        String whereClause = DBSqliteHelper.COL_SECTION_ID + " = ?";
        String[] whereArgs = new String[]{
                section_id
        };

        Cursor cursor = database.query(DBSqliteHelper.TABLE_QUIZ,
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

    public Quiz getEntry(String section_id, String quiz) {
        Quiz entry = null;

        String whereClause = DBSqliteHelper.COL_SECTION_ID + " = ? AND " + DBSqliteHelper.COL_QUIZ + " = ?";
        String[] whereArgs = new String[]{
                section_id,
                quiz
        };

        Cursor cursor = database.query(DBSqliteHelper.TABLE_QUIZ,
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

    private Quiz cursorToRow(Cursor cursor) {
        Quiz row = new Quiz();
        row.setId(cursor.getInt(0));
        row.setSection_id(cursor.getString(1));
        row.setQuiz(cursor.getString(2));
        row.setValue(cursor.getInt(3));
        return row;
    }
}
