package DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Pasan Eramusugoda on 3/29/2015.
 * Update on 04/29/2015 for new structure
 */
public class DBSqliteHelper extends SQLiteOpenHelper {

    //Tables
    public static final String TABLE_SECTION = "tbl_section";
    public static final String TABLE_QUOTE = "tbl_quote";
    public static final String TABLE_QUESTION = "tbl_question";
    public static final String TABLE_QUIZ = "tbl_quiz";
    public static final String TABLE_OPTION = "tbl_option";
    public static final String TABLE_COMMENT = "tbl_comment";

    //common cols
    public static final String COL_ID = "id";
    public static final String COL_UNIQUE_ID = "unique_id";
    public static final String COL_SECTION_ID = "section_id";

    //section
    //public static final String COL_ID = "id";
    public static final String COL_SECTION_DES = "section_des";
    //public static final String COL_SECTION_ID = "section_id";
    public static final String COL_LAST_QUOTE = "last_quote";
    public static final String COL_SECTION_FINISHED = "section_finished";
    public static final String COL_LEFT_VIEW = "left_view";

    //quote
    //public static final String COL_ID = "id";
    //public static final String COL_SECTION_ID = "section_id";
    public static final String COL_QUOTE_ID = "quote_id";
    //public static final String COL_UNIQUE_ID = "unique_id";
    public static final String COL_QUOTE_TYPE = "quote_type"; //options / question - string - O/Q
    public static final String COL_QUOTE = "quote";
    public static final String COL_PERSON_SOURCE = "person_source";
    public static final String COL_DATE = "date";
    public static final String COL_IMAGE = "image";
    public static final String COL_ANSWERED = "answered"; //true / false
    public static final String COL_IS_ACTIVE = "is_active"; //true / false
    public static final String COL_VERSION = "version"; //INT

    //questions
    //public static final String COL_ID = "id";
    //public static final String COL_UNIQUE_ID = "unique_id";
    //public static final String COL_SECTION_ID = "section_id";
    public static final String COL_QUESTION = "question";
    public static final String COL_ANSWER = "answer";

    //quiz
    //public static final String COL_ID = "id";
    //public static final String COL_SECTION_ID = "section_id";
    public static final String COL_QUIZ = "quiz";
    public static final String COL_VALUE = "value";

    //option
    //public static final String COL_ID = "id";
    //public static final String COL_UNIQUE_ID = "unique_id";
    public static final String COL_IS_LIKE = "is_like"; //true / false
    public static final String COL_IS_BULLSEYE = "is_bullseye"; //true / false

    //comments
    //public static final String COL_ID = "id";
    //public static final String COL_UNIQUE_ID = "unique_id";
    //public static final String COL_SECTION_ID = "section_id";
    public static final String COL_COMMENT = "comment";

    private static final String CREATE_TABLE_SECTION = "create table "
            + TABLE_SECTION + "("
            + COL_ID + " integer primary key autoincrement, "
            + COL_SECTION_ID + " text unique, "
            + COL_SECTION_DES + " text, "
            + COL_LAST_QUOTE + " text, "
            + COL_SECTION_FINISHED + " text, "
            + COL_LEFT_VIEW + " integer);";

    private static final String CREATE_TABLE_QUOTE = "create table "
            + TABLE_QUOTE + "("
            + COL_ID + " integer primary key autoincrement, "
            + COL_UNIQUE_ID + " text unique, "
            + COL_SECTION_ID + " text, "
            + COL_QUOTE_ID + " integer, "
            + COL_QUOTE_TYPE + " text, "
            + COL_QUOTE + " text, "
            + COL_PERSON_SOURCE + " text, "
            + COL_DATE + " text, "
            + COL_IMAGE + " text, "
            + COL_ANSWERED + " text, "
            + COL_IS_ACTIVE + " text, "
            + COL_VERSION + " integer);";

    private static final String CREATE_TABLE_QUESTION = "create table "
            + TABLE_QUESTION + "("
            + COL_ID + " integer primary key autoincrement, "
            + COL_UNIQUE_ID + " text, " //FK - QUOTE
            + COL_SECTION_ID + " text, "
            + COL_QUESTION + " text, "
            + COL_ANSWER + " text);";

    private static final String CREATE_TABLE_QUIZ = "create table "
            + TABLE_QUIZ + "("
            + COL_ID + " integer primary key autoincrement, "
            + COL_SECTION_ID + " text, "
            + COL_QUIZ + " text, "
            + COL_VALUE + " integer);";

    private static final String CREATE_TABLE_COMMENT = "create table "
            + TABLE_COMMENT + "("
            + COL_ID + " integer primary key autoincrement, "
            + COL_UNIQUE_ID + " text, "
            + COL_SECTION_ID + " text, "
            + COL_COMMENT + " text);";

    // unconnected Table creation sql statement
    private static final String CREATE_TABLE_OPTION = "create table "
            + TABLE_OPTION + "("
            + COL_ID + " integer primary key autoincrement, "
            + COL_UNIQUE_ID + " text, "
            + COL_IS_LIKE + " text, "
            + COL_IS_BULLSEYE + " text);";

    private static final String DATABASE_NAME = "Best_Connections.db";
    private static final int DATABASE_VERSION = 1;

    public DBSqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_SECTION);
        database.execSQL(CREATE_TABLE_QUESTION);
        database.execSQL(CREATE_TABLE_QUIZ);
        database.execSQL(CREATE_TABLE_QUOTE);
        database.execSQL(CREATE_TABLE_COMMENT);
        database.execSQL(CREATE_TABLE_OPTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBSqliteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTION);
        onCreate(db);
    }
}

