package com.syncbridge.bestconnections;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.List;

import Classes.CommonFunctions;
import Classes.MusicManager;
import DBHelper.Comment;
import DBHelper.CommentDataSource;
import DBHelper.Question;
import DBHelper.QuestionDataSource;


public class CoreGoalsIntro2Activity extends MainActivity {

    public Context m_context;
    public static Handler h;
    public String lastQuoteId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_goals_intro2);

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

        m_context = this.getApplicationContext();

        SECTION_ID = "CG";
        activeActivity = 2;
        set(m_context);

        CommonFunctions.updateLeftView(m_context, SECTION_ID, 2);

        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutCoreGoalsIntro);
        final RelativeLayout relativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayoutCoreGoalsIntro2);
        relativeLayout.setVisibility(View.VISIBLE);
        relativeLayout.startAnimation(AnimationUtils.loadAnimation(CoreGoalsIntro2Activity.this, R.anim.fadein));

        findViewById(R.id.imageView1).setOnClickListener(onImageView1Click);
        findViewById(R.id.imageView2).setOnClickListener(onImageView2Click);
        findViewById(R.id.imageView3).setOnClickListener(onImageView3Click);

        findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setVisibility(View.INVISIBLE);
                relativeLayout2.setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.imageViewNext2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CoreGoalsIntro2Activity.this, CoreGoalsActivity.class);
                i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                startActivity(i);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        ((TextView)findViewById(R.id.textView1)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView)findViewById(R.id.textView2)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView)findViewById(R.id.textView3)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

        ((TextView)findViewById(R.id.textViewIntro1)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView)findViewById(R.id.textViewIntro21)).setTypeface(CommonFunctions.getTypeFace(m_context, 6));
        ((TextView)findViewById(R.id.textViewIntro22)).setTypeface(CommonFunctions.getTypeFace(m_context, 6));
        ((TextView)findViewById(R.id.textViewIntro23)).setTypeface(CommonFunctions.getTypeFace(m_context, 6));
        ((TextView)findViewById(R.id.textViewIntro3)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

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

    View.OnClickListener onImageView1Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String sQuestion = getResources().getString(R.string.string_core_goals_intro2_21);

            final Dialog dialog = new Dialog(CoreGoalsIntro2Activity.this);
            dialog.setContentView(R.layout.layout_journal_input);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //dialog.setTitle("Enter text for journal...");
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.cancel();
                }
            });

            ((Button) dialog.findViewById(R.id.buttonAddCommentCancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });

            ((Button) dialog.findViewById(R.id.buttonAddCommentDone)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //save to database
                    String content = ((EditText) dialog.findViewById(R.id.editTextJournalComment)).getText().toString();

                    if(!content.trim().isEmpty()) {
                        try {
                            QuestionDataSource questionDataSource = new QuestionDataSource(m_context);
                            questionDataSource.open();

                            Question question = questionDataSource.getEntry("CG", sQuestion);

                            if(question != null) {
                                question.setAnswer(content);
                                questionDataSource.updateEntry(question);
                            } else {
                                questionDataSource.createNewEntry("", "CG", sQuestion, content);
                            }

                            questionDataSource.close();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    dialog.cancel();
                }
            });

            try {
                QuestionDataSource questionDataSource = new QuestionDataSource(m_context);
                questionDataSource.open();

                Question question = questionDataSource.getEntry("CG", sQuestion);

                if(question != null) {
                    ((EditText) dialog.findViewById(R.id.editTextJournalComment)).setText(question.getAnswer());
                }

                questionDataSource.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            dialog.show();
        }
    };

    View.OnClickListener onImageView2Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String sQuestion = getResources().getString(R.string.string_core_goals_intro2_22);

            final Dialog dialog = new Dialog(CoreGoalsIntro2Activity.this);
            dialog.setContentView(R.layout.layout_journal_input);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //dialog.setTitle("Enter text for journal...");
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.cancel();
                }
            });

            ((Button) dialog.findViewById(R.id.buttonAddCommentCancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });

            ((Button) dialog.findViewById(R.id.buttonAddCommentDone)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //save to database
                    String content = ((EditText) dialog.findViewById(R.id.editTextJournalComment)).getText().toString();

                    if(!content.trim().isEmpty()) {
                        try {
                            QuestionDataSource questionDataSource = new QuestionDataSource(m_context);
                            questionDataSource.open();

                            Question question = questionDataSource.getEntry("CG", sQuestion);

                            if(question != null) {
                                question.setAnswer(content);
                                questionDataSource.updateEntry(question);
                            } else {
                                questionDataSource.createNewEntry("", "CG", sQuestion, content);
                            }

                            questionDataSource.close();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    dialog.cancel();
                }
            });

            try {
                QuestionDataSource questionDataSource = new QuestionDataSource(m_context);
                questionDataSource.open();

                Question question = questionDataSource.getEntry("CG", sQuestion);

                if(question != null) {
                    ((EditText) dialog.findViewById(R.id.editTextJournalComment)).setText(question.getAnswer());
                }

                questionDataSource.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            dialog.show();
        }
    };

    View.OnClickListener onImageView3Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String sQuestion = getResources().getString(R.string.string_core_goals_intro2_23);

            final Dialog dialog = new Dialog(CoreGoalsIntro2Activity.this);
            dialog.setContentView(R.layout.layout_journal_input);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //dialog.setTitle("Enter text for journal...");
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.cancel();
                }
            });

            ((Button) dialog.findViewById(R.id.buttonAddCommentCancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });

            ((Button) dialog.findViewById(R.id.buttonAddCommentDone)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //save to database
                    String content = ((EditText) dialog.findViewById(R.id.editTextJournalComment)).getText().toString();

                    if(!content.trim().isEmpty()) {
                        try {
                            QuestionDataSource questionDataSource = new QuestionDataSource(m_context);
                            questionDataSource.open();

                            Question question = questionDataSource.getEntry("CG", sQuestion);

                            if(question != null) {
                                question.setAnswer(content);
                                questionDataSource.updateEntry(question);
                            } else {
                                questionDataSource.createNewEntry("", "CG", sQuestion, content);
                            }

                            questionDataSource.close();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    dialog.cancel();
                }
            });

            try {
                QuestionDataSource questionDataSource = new QuestionDataSource(m_context);
                questionDataSource.open();

                Question question = questionDataSource.getEntry("CG", sQuestion);

                if(question != null) {
                    ((EditText) dialog.findViewById(R.id.editTextJournalComment)).setText(question.getAnswer());
                }

                questionDataSource.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            dialog.show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            setResult(RESULT_OK);
            finish();
        }
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
    public void onResume() {

        activeActivity = 2;
        currentActivity = 2;
        CommonFunctions.updateLeftView(m_context, SECTION_ID, 2);

        super.onResume();
    }
}
