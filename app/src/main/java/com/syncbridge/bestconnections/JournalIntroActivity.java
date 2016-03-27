package com.syncbridge.bestconnections;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import Classes.MusicManager;


public class JournalIntroActivity extends MainActivity {

    private FrameLayout frameLayout;
    public static Handler h;
    public Context m_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_intro);

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

        activeActivity = 9;
        set(m_context);
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
        getMenuInflater().inflate(R.menu.menu_journal_intro, menu);
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

        if (id == R.id.action_menu_back) {
            SECTION_ID = "";
            activeActivity = 0;
            currentActivity = 0;
            this.finish();
        }

        if(id == R.id.core_beliefs) {
            startActivity("CB");
        }

        if(id == R.id.core_goals) {
            startActivity("CG");
        }

        if(id == R.id.core_status) {
            startActivity("CS");
        }

        if(id == R.id.body) {
            startActivity("LFBO");
        }

        if(id == R.id.soul) {
            startActivity("LFSO");
        }

        if(id == R.id.spirit) {
            startActivity("LFSP");
        }

        if(id == R.id.unconnected) {
            startActivity("UC");
        }

        if(id == R.id.disconnected) {
            startActivity("DC");
        }

        if(id == R.id.my_journal) {
            //startActivity("JN");
            Intent i = new Intent(JournalIntroActivity.this, MyJournalActivity.class);
            i.putExtra("SECTION", "JN");
            startActivity(i);
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            setResult(RESULT_OK);
            finish();
        }
    }

    private void startActivity(String Section) {
        Intent i = new Intent(JournalIntroActivity.this, JournalActivity.class);
        i.putExtra("SECTION", Section);
        startActivity(i);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }
}
