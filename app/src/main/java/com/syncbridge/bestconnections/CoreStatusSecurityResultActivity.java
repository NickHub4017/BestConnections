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


public class CoreStatusSecurityResultActivity extends MainActivity {

    String RESULT;
    public Context m_context;
    public static Handler h;
    public String lastQuoteId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_status_security_result);

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

        if(getIntent().getExtras().getString("LAST_QUOTE_ID") != null) {
            lastQuoteId = getIntent().getExtras().getString("LAST_QUOTE_ID");
        }

        if(!lastQuoteId.isEmpty()) {
            Intent i = new Intent(CoreStatusSecurityResultActivity.this, CoreStatusActivity.class);
            i.putExtra("LAST_QUOTE_ID", lastQuoteId);
            startActivity(i);
            //overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }

        m_context = this.getApplicationContext();

        SECTION_ID = "CS";
        activeActivity = 3;
        set(CoreStatusSecurityResultActivity.this);

        CommonFunctions.updateLeftView(m_context, "CSS", 6);

        ((TextView)findViewById(R.id.textView3)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

        RESULT = getIntent().getExtras().getString("RESULT");

        if(RESULT == null || RESULT.isEmpty() || RESULT.equals("0")) {
            RESULT = CommonFunctions.getResultString(m_context, "CSS");
        }

        if(7 <= Integer.parseInt(RESULT) && Integer.parseInt(RESULT) <= 30) {
            ((TextView) findViewById(R.id.textView3)).setText("7 - 30: There are a few areas of vulnerability to think about. Maybe a good idea to have a chat with a close friend or have an exploratory meeting with a counsellor.");
        } else if(31 <= Integer.parseInt(RESULT) && Integer.parseInt(RESULT) <= 55) {
            ((TextView) findViewById(R.id.textView3)).setText("31 - 55: You are with the majority... good in many areas but some areas are still giving a bit of grief!");
        } else if(56 <= Integer.parseInt(RESULT) && Integer.parseInt(RESULT) <= 70) {
            ((TextView) findViewById(R.id.textView3)).setText("56 - 70: You are a very secure person and have much to be thankful for. Help someone who may have a low score!");
        }

        ((TextView) findViewById(R.id.textView1)).setText(RESULT);

        findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CoreStatusSecurityResultActivity.this, CoreStatusActivity.class);
                i.putExtra("SECTION", "CSS");
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
        CommonFunctions.updateLeftView(m_context, "CSS", 6);

        super.onResume();
    }
}
