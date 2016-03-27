package com.syncbridge.bestconnections;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Classes.CommonFunctions;
import Classes.JournalAnswersListAdapter;
import DBHelper.Question;
import DBHelper.QuestionDataSource;
import DBHelper.Quiz;


public class JournalAnswersActivity extends Activity {

    public Context m_context;
    public static String SECTION = "";
    public static JournalAnswersListAdapter journalAnswersListAdapter;
    public static QuestionDataSource questionDataSource;
    public static List<Question> questionList = new ArrayList<>();

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_answers);

        SECTION = getIntent().getExtras().getString("SECTION");
        m_context = getApplicationContext();

        listView = (ListView) findViewById(R.id.listView);

        try {
            questionDataSource = new QuestionDataSource(m_context);
            questionDataSource.open();
            if(SECTION.equals("CS")) {
                String[] sub= new String[] {"CSS", "CSH", "CSM"};
                for(int i = 0; i < sub.length; i++) {
                    for(Question question : questionDataSource.getAllEntries(sub[i], true)) {
                        questionList.add(question);
                    }
                }
            } else {
                questionList = questionDataSource.getAllEntries(SECTION, true);
            }
            journalAnswersListAdapter = new JournalAnswersListAdapter(
                    this, R.layout.layout_journal_answers_list_item, questionList);

            listView.setAdapter(journalAnswersListAdapter);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_journal_answers, menu);
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

        questionList.clear();
        questionList = questionDataSource.getAllEntries(SECTION, true);

        journalAnswersListAdapter.clear();
        journalAnswersListAdapter.addAll(questionList);
        journalAnswersListAdapter.notifyDataSetChanged();
    }
}
