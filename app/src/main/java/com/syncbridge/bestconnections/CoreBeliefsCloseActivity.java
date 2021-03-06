package com.syncbridge.bestconnections;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLException;

import Classes.CommonFunctions;
import Classes.MusicManager;


public class CoreBeliefsCloseActivity extends MainActivity {

    public Context m_context;
    public static Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_beliefs_close);

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

        SECTION_ID = "CB";
        activeActivity = 1;
        set(m_context);

        ((TextView)findViewById(R.id.textViewClose1)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView)findViewById(R.id.textViewClose2)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView)findViewById(R.id.textViewClose3)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView)findViewById(R.id.textViewClose4)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

        findViewById(R.id.textViewClose4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lastQuoteId = "";
                try {
                    sectionDataSource.open();
                    lastQuoteId = sectionDataSource.getEntry("CG").getLast_quote();
                } catch (SQLException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
                Intent i = new Intent(CoreBeliefsCloseActivity.this, CoreGoalsIntroActivity.class);
                i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                startActivity(i);
                //finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lastQuoteId = "";
                try {
                    sectionDataSource.open();
                    lastQuoteId = sectionDataSource.getEntry("CG").getLast_quote();
                } catch (SQLException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
                Intent i = new Intent(CoreBeliefsCloseActivity.this, CoreGoalsIntroActivity.class);
                i.putExtra("LAST_QUOTE_ID", lastQuoteId);
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
            SECTION_ID = "";
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
