package com.syncbridge.bestconnections;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.List;

import Classes.CommonFunctions;
import Classes.JournalQuizListAdapter;
import DBHelper.Quiz;
import DBHelper.QuizDataSource;


public class JournalResultsActivity extends Activity {

    String SECTION;
    public Context m_context;
    QuizDataSource quizDataSource;
    int section = 0;
    String[] sectionArray = new String[]{"CSS", "CSH", "CSM"};

    RelativeLayout relativeLayoutContent;
    ListView listView;
    TextView textViewTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_results);

        m_context = this.getApplicationContext();
        SECTION = getIntent().getExtras().getString("SECTION");

        relativeLayoutContent = (RelativeLayout) findViewById(R.id.relativeLayoutContent);
        listView = (ListView) findViewById(R.id.listView);
        textViewTitle = (TextView) findViewById(R.id.textViewResultsTitle);

        if(SECTION.equals("CS")) {

            findViewById(R.id.imageButtonPrevious).setOnClickListener(buttonPrevious);
            findViewById(R.id.imageButtonNext).setOnClickListener(buttonNext);

            loadDataToControl(0);
        } else {
            findViewById(R.id.linearLayout).setVisibility(View.GONE);
            loadDataToControlDefault();
        }
    }

    View.OnClickListener buttonPrevious = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            loadDataToControl(1);
        }
    };

    View.OnClickListener buttonNext = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            loadDataToControl(2);
        }
    };

    private void loadDataToControl(int action) {
        if (action == -1) {
            section = 0;
        } else if (action == 1) {
            if(section > 0) {
                section--;
            } else {
                section = 0;
            }
        } else if (action == 2) {
            if(section < 2) {
                section++;
            } else {
                section = 2;
            }
        }

        textViewTitle.setText(getSectionTitle(section));
        int result = 0;
        for(Quiz quiz : getQuizList(sectionArray[section])) {
            result += quiz.getValue();
        }
        ((TextView)findViewById(R.id.textView1)).setText(Integer.toString(result));
        setResultDes(section, result);
    }

    private void loadDataToControlDefault() {
        section = -1;
        textViewTitle.setText(getSectionTitle(section));
        int result = 0;
        for(Quiz quiz : getQuizList(SECTION)) {
            result += quiz.getValue();
        }
        ((TextView)findViewById(R.id.textView1)).setText(Integer.toString(result));
        setResultDesDefault(result);
    }

    private String getSectionTitle(int section) {
        switch (section) {
            case 0:
                return "SECURITY";
            case 1:
                return "HAPPINESS";
            case 2:
                return "MOTIVATION";
            default:
                return "RESULTS";
        }
    }

    private void setResultDes(int section, int result) {
        switch (section) {
            case 0:
                if(7 <= result && result <= 30) {
                    ((TextView) findViewById(R.id.textView3)).setText("7 - 30: There are a few areas of vulnerability to think about. Maybe a good idea to have a chat with a close friend or have an exploratory meeting with a counsellor.");
                } else if(31 <= result && result <= 55) {
                    ((TextView) findViewById(R.id.textView3)).setText("31 - 55: You are with the majority... good in many areas but some areas are still giving a bit of grief!");
                } else if(56 <= result && result <= 70) {
                    ((TextView) findViewById(R.id.textView3)).setText("56 - 70: You are a very secure person and have much to be thankful for. Help someone who may have a low score!");
                }
                break;
            case 1:
                if(10 <= result && result <= 30) {
                    ((TextView) findViewById(R.id.textView3)).setText("10-30 Life could be better! Need to change focus and speak to someone for help");
                } else if(31 <= result && result <= 60) {
                    ((TextView) findViewById(R.id.textView3)).setText("31-60 Getting on OK though with some ups and downs. Try some action points on the list");
                } else if(61 <= result) {
                    ((TextView) findViewById(R.id.textView3)).setText("61 plus This is a happy season of life. Celebrate the good things and share the harvest!");
                }
                break;
            case 2:
                if(1 <= result && result <= 30) {
                    ((TextView) findViewById(R.id.textView3)).setText("TOP THREE: These are your \u2018drivers\u2019. Do you have specific goals for them? (link to Core Goals)");
                } else if(31 <= result && result <= 50) {
                    ((TextView) findViewById(R.id.textView3)).setText("FOURTH AND FIFTH: Does this mean that these are in the \u2018right sync\u2019? Is your \u2018motivational mix\u2019 in balance?");
                } else if(51 <= result && result <= 70) {
                    ((TextView) findViewById(R.id.textView3)).setText("BOTTOM TWO: Are these less important for you than they used to be? Are you giving them fair attention?");
                }
                break;
        }
    }

    private void setResultDesDefault(int result) {

        ((TextView)findViewById(R.id.textView3)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

        if(SECTION.equals("LFBO")) {
            if(10 <= result && result <= 39) {
                ((TextView) findViewById(R.id.textView3)).setText(R.string.string_lfbo_result_1);
            } else if(40 <= result && result <= 69) {
                ((TextView) findViewById(R.id.textView3)).setText(R.string.string_lfbo_result_2);
            } else if(70 <= result && result <= 100) {
                ((TextView) findViewById(R.id.textView3)).setText(R.string.string_lfbo_result_3);
            }
        } else if(SECTION.equals("LFSO")) {
            if(10 <= result && result <= 39) {
                ((TextView) findViewById(R.id.textView3)).setText(R.string.string_lfso_result_1);
            } else if(40 <= result && result <= 69) {
                ((TextView) findViewById(R.id.textView3)).setText(R.string.string_lfso_result_2);
            } else if(70 <= result && result <= 100) {
                ((TextView) findViewById(R.id.textView3)).setText(R.string.string_lfso_result_3);
            }
        } else if(SECTION.equals("LFSP")) {
            if(10 <= result && result <= 39) {
                ((TextView) findViewById(R.id.textView3)).setText(R.string.string_lfsp_result_1);
            } else if(40 <= result && result <= 69) {
                ((TextView) findViewById(R.id.textView3)).setText(R.string.string_lfsp_result_2);
            } else if(70 <= result && result <= 100) {
                ((TextView) findViewById(R.id.textView3)).setText(R.string.string_lfsp_result_3);
            }
        } else if(SECTION.equals("UN")) {
            if(10 <= result && result <= 20) {
                ((TextView) findViewById(R.id.textView3)).setText(R.string.string_unconnected_result_1);
            } else if(21 <= result && result <= 60) {
                ((TextView) findViewById(R.id.textView3)).setText(R.string.string_unconnected_result_2);
            } else if(61 <= result && result <= 100) {
                ((TextView) findViewById(R.id.textView3)).setText(
                        CommonFunctions.getSpannableStringBuilder(m_context,
                                m_context.getResources().getString(R.string.string_unconnected_result_3)));
            }
        } else if(SECTION.equals("DS")) {
            if(10 <= result && result <= 20) {
                ((TextView) findViewById(R.id.textView3)).setText(R.string.string_disconnected_result_1);
            } else if(21 <= result && result <= 60) {
                ((TextView) findViewById(R.id.textView3)).setText(R.string.string_disconnected_result_2);
            } else if(61 <= result && result <= 100) {
                ((TextView) findViewById(R.id.textView3)).setText(
                        CommonFunctions.getSpannableStringBuilder(m_context,
                                m_context.getResources().getString(R.string.string_disconnected_result_3)));
            }
        } else if(SECTION.equals("RS")) {
            if(10 <= result && result <= 20) {
                ((TextView) findViewById(R.id.textView3)).setText(R.string.string_resources_result_1);
            } else if(21 <= result && result <= 60) {
                ((TextView) findViewById(R.id.textView3)).setText(R.string.string_resources_result_2);
            } else if(61 <= result && result <= 100) {
                ((TextView) findViewById(R.id.textView3)).setText(R.string.string_resources_result_3);
            }
        }
    }

    private List<Quiz> getQuizList(String section) {
        try {
            quizDataSource = new QuizDataSource(m_context);
            quizDataSource.open();
            return quizDataSource.getAllEntries(section);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_journal_results, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
