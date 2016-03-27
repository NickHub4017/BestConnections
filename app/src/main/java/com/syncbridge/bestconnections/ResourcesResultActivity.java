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

import Classes.CommonFunctions;
import Classes.MusicManager;


public class ResourcesResultActivity extends MainActivity {

    public Context m_context;
    String RESULT;
    public static Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources_result);

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

        CommonFunctions.updateLeftView(m_context, SECTION_ID, 6);

        RESULT = getIntent().getExtras().getString("RESULT");

        ((TextView)findViewById(R.id.textView3)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

        if(RESULT == null || RESULT.isEmpty() || RESULT.equals("0")) {
            RESULT = CommonFunctions.getResultString(m_context, SECTION_ID);
        }

        if(10 <= Integer.parseInt(RESULT) && Integer.parseInt(RESULT) <= 20) {
            ((TextView) findViewById(R.id.textView3)).setText(R.string.string_resources_result_1);
        } else if(21 <= Integer.parseInt(RESULT) && Integer.parseInt(RESULT) <= 60) {
            ((TextView) findViewById(R.id.textView3)).setText(R.string.string_resources_result_2);
        } else if(61 <= Integer.parseInt(RESULT) && Integer.parseInt(RESULT) <= 100) {
            ((TextView) findViewById(R.id.textView3)).setText(R.string.string_resources_result_3);
        }

        ((TextView) findViewById(R.id.textView1)).setText(RESULT);

        findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResourcesResultActivity.this, ResourcesClose1Activity.class);
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

        activeActivity = 10;
        currentActivity = 10;
        CommonFunctions.updateLeftView(m_context, SECTION_ID, 6);

        super.onResume();
    }
}
