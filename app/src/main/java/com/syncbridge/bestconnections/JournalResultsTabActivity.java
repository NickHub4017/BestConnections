package com.syncbridge.bestconnections;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Classes.JournalAnswersListAdapter;
import Classes.JournalQuizListAdapter;
import DBHelper.Quiz;
import DBHelper.QuizDataSource;


public class JournalResultsTabActivity extends Activity {

    String SECTION;
    public Context m_context;
    QuizDataSource quizDataSource;

    ListView listView;

    List<Quiz> quizList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_results_tab);

        m_context = this.getApplicationContext();
        SECTION = getIntent().getExtras().getString("SECTION");
        listView = (ListView) findViewById(R.id.listView);

        try {
            quizDataSource = new QuizDataSource(m_context);
            quizDataSource.open();
            quizList = quizDataSource.getAllEntries(SECTION);

            JournalQuizListAdapter journalQuizListAdapter = new JournalQuizListAdapter(
                    this, R.layout.layout_journal_results_list_item, quizList);

            listView.setAdapter(journalQuizListAdapter);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_journal_results_tab, menu);
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
}
