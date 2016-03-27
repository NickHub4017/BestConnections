package com.syncbridge.bestconnections;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import Classes.CommonFunctions;


public class AboutC4LActivity extends ActionBarActivity {

    public static Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_c4_l);h = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch(msg.what) {

                    case 0:
                        finish();
                        break;
                }
            }
        };

        ((TextView)findViewById(R.id.textView7)).setTypeface(CommonFunctions.getTypeFace(AboutC4LActivity.this, 4));
        ((TextView)findViewById(R.id.textView15)).setTypeface(CommonFunctions.getTypeFace(AboutC4LActivity.this, 3));
        ((TextView)findViewById(R.id.textView16)).setTypeface(CommonFunctions.getTypeFace(AboutC4LActivity.this, 2));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_c4_l, menu);
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
