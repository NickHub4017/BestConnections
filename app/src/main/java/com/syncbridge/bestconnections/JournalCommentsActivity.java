package com.syncbridge.bestconnections;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.sql.SQLException;
import java.util.List;

import DBHelper.Comment;
import DBHelper.CommentDataSource;
import DBHelper.Question;


public class JournalCommentsActivity extends Activity {

    public Context m_context;
    public static String SECTION;

    public static CommentDataSource commentDataSource;
    public static List<Comment> commentList;
    public static EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_comments);

        m_context = this.getApplicationContext();
        SECTION = getIntent().getExtras().getString("SECTION");
        String content = "";
        try {
            commentDataSource = new CommentDataSource(m_context);
            commentDataSource.open();
            commentList = commentDataSource.getAllEntries(SECTION);

            if (SECTION.equals("CS")) {
                String[] sub = new String[]{"CSS", "CSH", "CSM"};
                for (int i = 0; i < sub.length; i++) {
                    for (Comment comment : commentDataSource.getAllEntries(sub[i])) {
                        commentList.add(comment);
                    }
                }
            } else {
                commentList = commentDataSource.getAllEntries(SECTION);
            }

            for (Comment comment : commentList) {
                content += comment.getComment() + "\n\n";
            }

            editText = (EditText) findViewById(R.id.editTextJournalComment);
            editText.setText(content);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_journal_comments, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void reload() {

        commentList = commentDataSource.getAllEntries(SECTION);

        if (SECTION.equals("CS")) {
            String[] sub = new String[]{"CSS", "CSH", "CSM"};
            for (int i = 0; i < sub.length; i++) {
                for (Comment comment : commentDataSource.getAllEntries(sub[i])) {
                    commentList.add(comment);
                }
            }
        } else {
            commentList = commentDataSource.getAllEntries(SECTION);
        }

        String content = "";

        for (Comment comment : commentList) {
            content += comment.getComment() + "\n\n";
        }

        editText.setText("");
        editText.setText(content);

    }
}
