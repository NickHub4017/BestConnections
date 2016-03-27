package com.syncbridge.bestconnections;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLException;

import Classes.CommonFunctions;
import Classes.MusicManager;
import DBHelper.Quiz;
import DBHelper.QuizDataSource;


public class CoreStatusHappinessResultActivity extends MainActivity {

    String RESULT;
    public Context m_context;
    public static Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_status_happiness_result);

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

        SECTION_ID = "CS";
        activeActivity = 3;
        set(CoreStatusHappinessResultActivity.this);

        CommonFunctions.updateLeftView(m_context, "CSH", 6);

        RESULT = getIntent().getExtras().getString("RESULT");

        if(RESULT == null) {
            int result = 0;

            try {
                final QuizDataSource quizDataSource = new QuizDataSource(m_context);
                quizDataSource.open();

                for(Quiz q : quizDataSource.getAllEntries("CSH")) {
                    result += q.getValue();
                }

                RESULT = Integer.toString(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        ((TextView)findViewById(R.id.textView3)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

        if(10 <= Integer.parseInt(RESULT) && Integer.parseInt(RESULT) <= 30) {
            ((TextView) findViewById(R.id.textView3)).setText("10-30 Life could be better! Need to change focus and speak to someone for help");
        } else if(31 <= Integer.parseInt(RESULT) && Integer.parseInt(RESULT) <= 60) {
            ((TextView) findViewById(R.id.textView3)).setText("31-60 Getting on OK though with some ups and downs. Try some action points on the list");
        } else if(61 <= Integer.parseInt(RESULT)) {
            ((TextView) findViewById(R.id.textView3)).setText("61 plus This is a happy season of life. Celebrate the good things and share the harvest!");
        }

        ((TextView) findViewById(R.id.textView1)).setText(RESULT);

        findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CoreStatusHappinessResultActivity.this, CoreStatusActivity.class);
                i.putExtra("SECTION", "CSH");
                startActivity(i);
                //finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

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

        activeActivity = 3;
        currentActivity = 3;
        CommonFunctions.updateLeftView(m_context, "CSH", 6);

        super.onResume();
    }
}
