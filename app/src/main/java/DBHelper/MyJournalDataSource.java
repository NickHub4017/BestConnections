package DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pasan Eramusugoda on 4/24/2015.
 */
public class MyJournalDataSource {
    private SQLiteDatabase database;
    private DBSqliteHelper dbHelper;
    private String[] allColumns = {
            DBSqliteHelper.COL_ID,
//            DBSqliteHelper.COL_IDENTITY,
//            DBSqliteHelper.COL_TYPE,
//            DBSqliteHelper.COL_QUESTION_TEXT,
//            DBSqliteHelper.COL_QUESTION_TEXT2,
//            DBSqliteHelper.COL_QUOTE_TEXT,
//            DBSqliteHelper.COL_PERSON_SOURCE,
//            DBSqliteHelper.COL_DATE,
//            DBSqliteHelper.COL_CHOSEN_OPTION,
//            DBSqliteHelper.COL_IMAGE_PATH
    };

    public MyJournalDataSource(Context context) {
        dbHelper = new DBSqliteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public MyJournal createMyJournal(String identity, int type, String quote_text, String person_source,
                                    String date, String question_text, String question_text2, int chosen_option, String image_path){
//        ContentValues values = new ContentValues();
//        values.put(DBSqliteHelper.COL_IDENTITY, identity);
//        values.put(DBSqliteHelper.COL_TYPE, type);
//        values.put(DBSqliteHelper.COL_QUESTION_TEXT, question_text);
//        values.put(DBSqliteHelper.COL_QUESTION_TEXT2, question_text2);
//        values.put(DBSqliteHelper.COL_QUOTE_TEXT, quote_text);
//        values.put(DBSqliteHelper.COL_PERSON_SOURCE, person_source);
//        values.put(DBSqliteHelper.COL_DATE, date);
//        values.put(DBSqliteHelper.COL_CHOSEN_OPTION, chosen_option);
//        values.put(DBSqliteHelper.COL_IMAGE_PATH, image_path);
//
//        long insertId = database.insert(DBSqliteHelper.TABLE_MY_JOURNAL, null, values);
//        if(insertId != -1) {
//            Cursor cursor = database.query(DBSqliteHelper.TABLE_MY_JOURNAL,
//                    allColumns, DBSqliteHelper.COL_ID + " = " + insertId, null,
//                    null, null, null);
//            cursor.moveToFirst();
//            MyJournal newMyJournal = cursorToMyJournal(cursor);
//            cursor.close();
//            return newMyJournal;
//        } else {
            return null;
//        }
    }

    public void deleteAllMyJournals() {
//        database.delete(DBSqliteHelper.TABLE_MY_JOURNAL, DBSqliteHelper.COL_TYPE
//                + " = 0 OR " + DBSqliteHelper.COL_TYPE
//                + " = 1" , null);
    }

    public void deleteMyJournal(MyJournal myjournal) {
//        long id = myjournal.getId();
//        System.out.println("MyJournal deleted with id: " + id);
//        database.delete(DBSqliteHelper.TABLE_MY_JOURNAL, DBSqliteHelper.COL_ID
//                + " = " + id, null);
    }

    public List<MyJournal> getAllMyJournals() {
        List<MyJournal> myjournals = new ArrayList<MyJournal>();

//        Cursor cursor = database.query(DBSqliteHelper.TABLE_MY_JOURNAL, allColumns, null, null, null, null, null);
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            MyJournal myjournal = cursorToMyJournal(cursor);
//            myjournals.add(myjournal);
//            cursor.moveToNext();
//        }
//        // make sure to close the cursor
//        cursor.close();
        return myjournals;
    }

    public MyJournal getMyJournal(String identity) {
        MyJournal myjournal = new MyJournal();

//        String whereClause = DBSqliteHelper.COL_IDENTITY + " = ?";
//        String[] whereArgs = new String[]{
//                identity
//        };
//
//        Cursor cursor = database.query(DBSqliteHelper.TABLE_MY_JOURNAL,
//                allColumns, whereClause, whereArgs, null, null, null);
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            myjournal = cursorToMyJournal(cursor);
//            break;
//        }
//        // make sure to close the cursor
//        cursor.close();
        return myjournal;
    }

    public MyJournal getMyJournal(String quote, int type) {
        MyJournal myjournal = new MyJournal();

//        String whereClause = DBSqliteHelper.COL_QUOTE_TEXT + " = ? AND " + DBSqliteHelper.COL_TYPE + " = ?";
//        String[] whereArgs = new String[]{
//                quote, Integer.toString(type)
//        };
//
//        Cursor cursor = database.query(DBSqliteHelper.TABLE_MY_JOURNAL,
//                allColumns, whereClause, whereArgs, null, null, null);
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            myjournal = cursorToMyJournal(cursor);
//            break;
//        }
//        // make sure to close the cursor
//        cursor.close();
        return myjournal;
    }

    private MyJournal cursorToMyJournal(Cursor cursor) {
        MyJournal myjournals = new MyJournal();
        myjournals.setId(cursor.getInt(0));
        myjournals.setIdentity(cursor.getString(1));
        myjournals.setType(cursor.getInt(2));
        myjournals.setQuestion_text(cursor.getString(3));
        myjournals.setQuestion_text2(cursor.getString(4));
        myjournals.setQuote_text(cursor.getString(5));
        myjournals.setPerson_source(cursor.getString(6));
        myjournals.setDate(cursor.getString(7));
        myjournals.setChosen_option(cursor.getInt(8));
        myjournals.setImage_path(cursor.getString(9));
        return myjournals;
    }
}
