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
public class QuestionDataSource {
    private SQLiteDatabase database;
    private DBSqliteHelper dbHelper;
    private String[] allColumns = {
            DBSqliteHelper.COL_ID,
            DBSqliteHelper.COL_UNIQUE_ID,
            DBSqliteHelper.COL_SECTION_ID,
            DBSqliteHelper.COL_QUESTION,
            DBSqliteHelper.COL_ANSWER
    };

    public QuestionDataSource(Context context) {
        dbHelper = new DBSqliteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Question createNewEntry(String unique_id, String section_id, String question, String answer){
        ContentValues values = new ContentValues();
        values.put(DBSqliteHelper.COL_UNIQUE_ID, unique_id);
        values.put(DBSqliteHelper.COL_SECTION_ID, section_id);
        values.put(DBSqliteHelper.COL_QUESTION, question);
        values.put(DBSqliteHelper.COL_ANSWER, answer);

        long insertId = database.insert(DBSqliteHelper.TABLE_QUESTION, null, values);
        if(insertId != -1) {
            Cursor cursor = database.query(DBSqliteHelper.TABLE_QUESTION,
                    allColumns, DBSqliteHelper.COL_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            Question newEntry = cursorToRow(cursor);
            cursor.close();
            return newEntry;
        } else {
            return null;
        }
    }

    public void updateEntry(Question entry) {

        ContentValues values = new ContentValues();
        values.put(DBSqliteHelper.COL_QUESTION, entry.getQuestion());

        if(!entry.getAnswer().trim().isEmpty()) {
            values.put(DBSqliteHelper.COL_ANSWER, entry.getAnswer());
        }

        String whereClause = DBSqliteHelper.COL_UNIQUE_ID + " = ? AND " + DBSqliteHelper.COL_SECTION_ID + " = ? AND " + DBSqliteHelper.COL_ID + " = ?";
        String[] whereArgs = new String[]{
                entry.getUnique_id(),
                entry.getSection_id(),
                Integer.toString(entry.getId())
        };

        database.update(DBSqliteHelper.TABLE_QUESTION, values, whereClause, whereArgs);
    }

    public void deleteAllEntries() {
        database.delete(DBSqliteHelper.TABLE_QUESTION, null, null);
    }

    public void deleteEntry(Question entry) {
        String id = entry.getUnique_id();
        System.out.println("Resources deleted with id: " + id);
        database.delete(DBSqliteHelper.TABLE_QUESTION, DBSqliteHelper.COL_UNIQUE_ID
                + " = " + id, null);
    }

    public List<Question> getAllEntries() {
        List<Question> entryList = new ArrayList<Question>();

        Cursor cursor = database.query(DBSqliteHelper.TABLE_QUESTION, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Question entry = cursorToRow(cursor);
            entryList.add(entry);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return entryList;
    }

    public List<Question> getAllEntries(String section_id, boolean isAnswered) {
        List<Question> entryList = new ArrayList<Question>();

        String whereClause;
        String[] whereArgs;

        if(isAnswered) {
            whereClause = DBSqliteHelper.COL_SECTION_ID + " = ? AND " + DBSqliteHelper.COL_ANSWER + " != ''";
            whereArgs = new String[]{
                    section_id
            };
        } else {
            whereClause = DBSqliteHelper.COL_SECTION_ID + " = ?";
            whereArgs = new String[]{
                    section_id
            };
        }

        Cursor cursor = database.query(DBSqliteHelper.TABLE_QUESTION,
                allColumns, whereClause, whereArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Question entry = cursorToRow(cursor);
            entryList.add(entry);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return entryList;
    }

    public List<Question> getAllEntries(String unique_id) {
        List<Question> entryList = new ArrayList<Question>();

        String whereClause = DBSqliteHelper.COL_UNIQUE_ID + " = ?";
        String[] whereArgs = new String[]{
                unique_id
        };

        Cursor cursor = database.query(DBSqliteHelper.TABLE_QUESTION,
                allColumns, whereClause, whereArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Question entry = cursorToRow(cursor);
            entryList.add(entry);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return entryList;
    }

    public Question getEntry(String unique_id) {
        Question entry = null;

        String whereClause = DBSqliteHelper.COL_UNIQUE_ID + " = ?";
        String[] whereArgs = new String[]{
                unique_id
        };

        Cursor cursor = database.query(DBSqliteHelper.TABLE_QUESTION,
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

    public Question getEntry(String section_id, String question) {
        Question entry = null;

        String whereClause = DBSqliteHelper.COL_SECTION_ID + " = ? AND " + DBSqliteHelper.COL_QUESTION + " = ?";
        String[] whereArgs = new String[]{
                section_id,
                question
        };

        Cursor cursor = database.query(DBSqliteHelper.TABLE_QUESTION,
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

    private Question cursorToRow(Cursor cursor) {
        Question row = new Question();
        row.setId(cursor.getInt(0));
        row.setUnique_id(cursor.getString(1));
        row.setSection_id(cursor.getString(2));
        row.setQuestion(cursor.getString(3));
        row.setAnswer(cursor.getString(4));
        return row;
    }
}
