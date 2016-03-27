package com.syncbridge.bestconnections;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Classes.CommonFunctions;
import Classes.MusicManager;
import DBHelper.Quiz;
import DBHelper.QuizDataSource;


public class ResourcesQuizActivity extends MainActivity {

    public Context m_context;
    ArrayAdapter<String> adapter;
    public static Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources_quiz);

        h = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch(msg.what) {

                    case 0:
                        finish();
                        break;
                }
            }
        };

        m_context = this.getApplicationContext();

        SECTION_ID = "RS";
        activeActivity = 10;
        set(m_context);

        CommonFunctions.updateLeftView(m_context, SECTION_ID, 5);

        ((TextView)findViewById(R.id.textView)).setTypeface(CommonFunctions.getTypeFace(m_context, 3));
        ((TextView)findViewById(R.id.textView2)).setTypeface(CommonFunctions.getTypeFace(m_context, 7));

        // you need to have a list of data that you want the spinner to display
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("--");
        spinnerArray.add("01");
        spinnerArray.add("02");
        spinnerArray.add("03");
        spinnerArray.add("04");
        spinnerArray.add("05");
        spinnerArray.add("06");
        spinnerArray.add("07");
        spinnerArray.add("08");
        spinnerArray.add("09");
        spinnerArray.add("10");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = 0;

                try {
                    final QuizDataSource quizDataSource = new QuizDataSource(m_context);
                    quizDataSource.open();

                    for(Quiz q : quizDataSource.getAllEntries("RS")) {
                        result += q.getValue();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(ResourcesQuizActivity.this, ResourcesResultActivity.class);
                i.putExtra("RESULT", Integer.toString(result));
                startActivity(i);
                //finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        ((ScrollView) findViewById(R.id.scrollView3)).addView(handleQuiz());
        handleSpeakerIcon();
    }

    private void handleSpeakerIcon() {

        if (!continueMusic) {
            ((ImageView) findViewById(R.id.imageViewSpeaker)).setImageResource(R.mipmap.ic_mute);
        } else {
            ((ImageView) findViewById(R.id.imageViewSpeaker)).setImageResource(R.mipmap.ic_unmute);
        }

        ((ImageView) findViewById(R.id.imageViewSpeaker)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mDrawerLayout.closeDrawers();
                if (continueMusic) {
                    ((ImageView) findViewById(R.id.imageViewSpeaker)).setImageResource(R.mipmap.ic_mute);
                    ((ImageView) findViewById(R.id.imageViewSpeaker)).requestLayout();
                    MusicManager.pause();
                    continueMusic = false;
                } else {
                    currentActivity = activeActivity;
                    ((ImageView) findViewById(R.id.imageViewSpeaker)).setImageResource(R.mipmap.ic_unmute);
                    ((ImageView) findViewById(R.id.imageViewSpeaker)).requestLayout();
                    MusicManager.start(m_context, getMusic(), true);
                    continueMusic = true;
                }
            }
        });
    }


    private View handleQuiz() {
        LayoutInflater inflater = getLayoutInflater();
        TableLayout tableLayoutQuiz = (TableLayout) inflater.inflate(R.layout.layout_quiz_table, null);

        try {
            final QuizDataSource quizDataSource = new QuizDataSource(m_context);
            quizDataSource.open();

            List<Quiz> quizList = quizDataSource.getAllEntries("RS");

            for(final Quiz quiz : quizList) {
                TableRow layoutQuizItem = (TableRow)inflater.inflate(R.layout.layout_quiz_item, null);

                ((TextView) layoutQuizItem.findViewById(R.id.textViewQuestion)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
                ((TextView) layoutQuizItem.findViewById(R.id.textViewQuestion)).setText(quiz.getQuiz());

                final Spinner spinnerQuiz = (Spinner) layoutQuizItem.findViewById(R.id.spinnerQuiz);
                spinnerQuiz.setAdapter(adapter);
                spinnerQuiz.setSelection(quiz.getValue(), true);
                spinnerQuiz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        quiz.setValue(spinnerQuiz.getSelectedItemPosition());
                        quizDataSource.updateEntry(quiz);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                tableLayoutQuiz.addView(layoutQuizItem);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tableLayoutQuiz;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_global, menu);
        return true;
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

        if(id == R.id.action_menu_back) {
            activeActivity = 0;
            currentActivity = 0;
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {

        activeActivity = 10;
        currentActivity = 10;
        CommonFunctions.updateLeftView(m_context, SECTION_ID, 5);

        super.onResume();
    }
}
