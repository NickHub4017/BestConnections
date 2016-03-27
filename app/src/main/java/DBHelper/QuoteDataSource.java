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
public class QuoteDataSource {
    private SQLiteDatabase database;
    private DBSqliteHelper dbHelper;
    private String[] allColumns = {
            DBSqliteHelper.COL_ID,
            DBSqliteHelper.COL_SECTION_ID,
            DBSqliteHelper.COL_QUOTE_ID,
            DBSqliteHelper.COL_UNIQUE_ID,
            DBSqliteHelper.COL_QUOTE_TYPE,
            DBSqliteHelper.COL_QUOTE,
            DBSqliteHelper.COL_PERSON_SOURCE,
            DBSqliteHelper.COL_DATE,
            DBSqliteHelper.COL_IMAGE,
            DBSqliteHelper.COL_ANSWERED,
            DBSqliteHelper.COL_IS_ACTIVE,
            DBSqliteHelper.COL_VERSION
    };

    public QuoteDataSource(Context context) {
        dbHelper = new DBSqliteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Quote createNewEntry(String section_id, int quote_id, String unique_id, String quote_type, String quote,
                                String person_source, String date, String image, boolean answered, boolean is_active, int version){
        ContentValues values = new ContentValues();
        values.put(DBSqliteHelper.COL_SECTION_ID, section_id);
        values.put(DBSqliteHelper.COL_QUOTE_ID, quote_id);
        values.put(DBSqliteHelper.COL_UNIQUE_ID, unique_id);
        values.put(DBSqliteHelper.COL_QUOTE_TYPE, quote_type);
        values.put(DBSqliteHelper.COL_QUOTE, quote);
        values.put(DBSqliteHelper.COL_PERSON_SOURCE, person_source);
        values.put(DBSqliteHelper.COL_DATE, date);
        values.put(DBSqliteHelper.COL_IMAGE, image);
        values.put(DBSqliteHelper.COL_ANSWERED, Boolean.toString(answered));
        values.put(DBSqliteHelper.COL_IS_ACTIVE, Boolean.toString(is_active));
        values.put(DBSqliteHelper.COL_VERSION, version);

        long insertId = database.insert(DBSqliteHelper.TABLE_QUOTE, null, values);
        if(insertId != -1) {
            Cursor cursor = database.query(DBSqliteHelper.TABLE_QUOTE,
                    allColumns, DBSqliteHelper.COL_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            Quote newEntry = cursorToRow(cursor);
            cursor.close();
            return newEntry;
        } else {
            return null;
        }
    }

    public void updateEntry(Quote entry) {

        ContentValues values = new ContentValues();
        values.put(DBSqliteHelper.COL_IS_ACTIVE, entry.getIs_active());
        values.put(DBSqliteHelper.COL_QUOTE_TYPE, entry.getQuote_type());
        values.put(DBSqliteHelper.COL_QUOTE, entry.getQuote());
        values.put(DBSqliteHelper.COL_PERSON_SOURCE, entry.getPerson_source());
        values.put(DBSqliteHelper.COL_DATE, entry.getDate());
        values.put(DBSqliteHelper.COL_IMAGE, entry.getImage());
        values.put(DBSqliteHelper.COL_ANSWERED, Boolean.toString(entry.getAnswered()));
        values.put(DBSqliteHelper.COL_IS_ACTIVE, Boolean.toString(entry.getIs_active()));
        values.put(DBSqliteHelper.COL_VERSION, entry.getVersion());


        String whereClause = DBSqliteHelper.COL_UNIQUE_ID + " = ? AND " + DBSqliteHelper.COL_SECTION_ID + " = ?";
        String[] whereArgs = new String[]{
                entry.getUnique_id(),
                entry.getSection_id()
        };

        database.update(DBSqliteHelper.TABLE_QUOTE, values, whereClause, whereArgs);
    }

    public void flushAllEntries() {
        database.delete(DBSqliteHelper.TABLE_QUOTE, null, null);
    }

    public void flushEntry(Quote entry) {
        long id = entry.getId();
        System.out.println("Resources deleted with id: " + id);
        database.delete(DBSqliteHelper.TABLE_QUOTE, DBSqliteHelper.COL_ID
                + " = " + id, null);
    }

    public void deleteEntry(Quote entry) {
        Quote updateEntry = entry;
        updateEntry.setIs_active(false);

        ContentValues values = new ContentValues();
        values.put(DBSqliteHelper.COL_IS_ACTIVE, Boolean.toString(updateEntry.getIs_active()));

        String whereClause = DBSqliteHelper.COL_UNIQUE_ID + " = ? AND " + DBSqliteHelper.COL_SECTION_ID + " = ?";
        String[] whereArgs = new String[]{
                updateEntry.getUnique_id(),
                updateEntry.getSection_id()
        };

        database.update(DBSqliteHelper.TABLE_QUOTE, values, whereClause, whereArgs);
    }

    public List<Quote> getAllEntries() {
        List<Quote> entryList = new ArrayList<Quote>();

        Cursor cursor = database.query(DBSqliteHelper.TABLE_QUOTE, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Quote entry = cursorToRow(cursor);
            entryList.add(entry);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return entryList;
    }

    public List<Quote> getAllEntries(String section_id) {
        List<Quote> entryList = new ArrayList<Quote>();

        String whereClause = DBSqliteHelper.COL_SECTION_ID + " = ?";
        String[] whereArgs = new String[]{
                section_id
        };

        Cursor cursor = database.query(DBSqliteHelper.TABLE_QUOTE,
                allColumns, whereClause, whereArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Quote entry = cursorToRow(cursor);
            entryList.add(entry);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return entryList;
    }

    public List<Quote> getAllEntries(String section_id, boolean answered) {
        List<Quote> entryList = new ArrayList<Quote>();

        String whereClause = DBSqliteHelper.COL_SECTION_ID + " = ? AND " + DBSqliteHelper.COL_ANSWERED + " = ?";
        String[] whereArgs = new String[]{
                section_id,
                Boolean.toString(answered)
        };

        Cursor cursor = database.query(DBSqliteHelper.TABLE_QUOTE,
                allColumns, whereClause, whereArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Quote entry = cursorToRow(cursor);
            entryList.add(entry);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return entryList;
    }

    public Quote getEntry(String unique_id) {
        Quote entry = null;

        String whereClause = DBSqliteHelper.COL_UNIQUE_ID + " = ?";
        String[] whereArgs = new String[]{
                unique_id
        };

        Cursor cursor = database.query(DBSqliteHelper.TABLE_QUOTE,
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

    public Quote getEntry(int quote_id) {
        Quote entry = null;

        String whereClause = DBSqliteHelper.COL_QUOTE_ID + " = ?";
        String[] whereArgs = new String[]{
                Integer.toString(quote_id)
        };

        Cursor cursor = database.query(DBSqliteHelper.TABLE_QUOTE,
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

    private Quote cursorToRow(Cursor cursor) {
        Quote row = new Quote();
        row.setId(cursor.getInt(0));
        row.setSection_id(cursor.getString(1));
        row.setQuote_id(cursor.getInt(2));
        row.setUnique_id(cursor.getString(3));
        row.setQuote_type(cursor.getString(4));
        row.setQuote(cursor.getString(5));
        row.setPerson_source(cursor.getString(6));
        row.setDate(cursor.getString(7));
        row.setImage(cursor.getString(8));
        row.setAnswered(Boolean.parseBoolean(cursor.getString(9)));
        row.setIs_active(Boolean.parseBoolean(cursor.getString(10)));
        row.setVersion(cursor.getInt(11));
        return row;
    }
}
