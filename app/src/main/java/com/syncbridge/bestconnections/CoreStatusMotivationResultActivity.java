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


public class CoreStatusMotivationResultActivity extends MainActivity {

    String RESULT;
    public Context m_context;
    public static Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_status_motivation_result);

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
        set(CoreStatusMotivationResultActivity.this);

        CommonFunctions.updateLeftView(m_context, "CSM", 6);

        RESULT = getIntent().getExtras().getString("RESULT");

        if(RESULT == null) {
            int result = 0;

            try {
                final QuizDataSource quizDataSource = new QuizDataSource(m_context);
                quizDataSource.open();

                for(Quiz q : quizDataSource.getAllEntries("CSM")) {
                    result += q.getValue();
                }

                RESULT = Integer.toString(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        ((TextView)findViewById(R.id.textView3)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

        if(1 <= Integer.parseInt(RESULT) && Integer.parseInt(RESULT) <= 30) {
            ((TextView) findViewById(R.id.textView3)).setText("TOP THREE: These are your \u2018drivers\u2019. Do you have specific goals for them? (link to Core Goals)");
        } else if(31 <= Integer.parseInt(RESULT) && Integer.parseInt(RESULT) <= 50) {
            ((TextView) findViewById(R.id.textView3)).setText("FOURTH AND FIFTH: Does this mean that these are in the \u2018right sync\u2019? Is your \u2018motivational mix\u2019 in balance?");
        } else if(51 <= Integer.parseInt(RESULT) && Integer.parseInt(RESULT) <= 70) {
            ((TextView) findViewById(R.id.textView3)).setText("BOTTOM TWO: Are these less important for you than they used to be? Are you giving them fair attention?");
        }

        ((TextView) findViewById(R.id.textView1)).setText(RESULT);

        findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CoreStatusMotivationResultActivity.this, CoreStatusActivity.class);
                i.putExtra("SECTION", "CSM");
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
        CommonFunctions.updateLeftView(m_context, "CSM", 6);

        super.onResume();
    }
}
