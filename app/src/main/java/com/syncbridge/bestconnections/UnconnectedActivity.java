package com.syncbridge.bestconnections;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Classes.CommonFunctions;
import Classes.MusicManager;
import DBHelper.Comment;
import DBHelper.CommentDataSource;
import DBHelper.Option;
import DBHelper.OptionDataSource;
import DBHelper.Question;
import DBHelper.QuestionDataSource;
import DBHelper.Quiz;
import DBHelper.QuizDataSource;
import DBHelper.Quote;
import DBHelper.QuoteDataSource;
import DBHelper.SectionDataSource;


public class UnconnectedActivity extends MainActivity {

    private static String SECTION_ID = "UC";
    private static String PakegeName;

    private static Context m_context;
    private static ArrayAdapter<String> adapter;

    private static View layoutIntro, layoutStats, layoutResult, layoutQuotes, layoutClose1, layoutClose2  ;
    private static FrameLayout layoutContent;
    private static int statsResult = 0;
    private static int mStackLevel = 0;

    private static View layoutOption;
    private static FrameLayout frameLayout;
    private static ScrollView scrollView;

    private static boolean isLast = false;

    private static LayoutInflater inflater;
    private static RelativeLayout relativeLayout;
    private static TextView textViewQuote;
    private static TextView textViewSourceNDate;
    private static TextView textViewQuestion;
    private static ImageView imageViewCartoon;
    private static ImageView imageViewFirst;
    private static ImageView imageViewPrevious;
    private static ImageView imageViewNext;
    private static ImageView imageViewLast;

    private static String previousIdentity = "";
    private static String currentIdentity = "";
    private static String nextIdentity = "";

    private static int previousPosition = 0;
    private static int currentPosition = 0;
    private static int nextPosition = 0;

    private static List<Quote> quoteList;
    private static QuoteDataSource quoteDataSource;

    private static String lastQuoteId = "";

    public static Handler h;
    private static boolean mOverride = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unconnected);

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

        activeActivity = 7;

        PakegeName = getPackageName();
        m_context = UnconnectedActivity.this;

        set(m_context);

        handleSpeakerIcon();

        layoutContent = (FrameLayout) findViewById(R.id.content_frame);

        inflater = getLayoutInflater();
        layoutIntro = inflater.inflate(R.layout.layout_unconnected_intro, null);
        layoutStats = inflater.inflate(R.layout.layout_unconnected_quiz, null);
        layoutResult = inflater.inflate(R.layout.layout_unconnected_result, null);
        layoutQuotes = inflater.inflate(R.layout.layout_unconnected_quotes, null);
        layoutClose1 = inflater.inflate(R.layout.layout_unconnected_close1, null);
        layoutClose2 = inflater.inflate(R.layout.layout_unconnected_close2, null);

        layoutContent.removeAllViews();


        int leftView = CommonFunctions.getLeftView(m_context, SECTION_ID);

        switch (leftView) {
            case 1:
                controlIntro(layoutIntro);
                layoutContent.addView(layoutIntro);
                isLast = true;
                break;
            case 5:
                controlStats(layoutStats, 1, 1, 10);
                layoutContent.addView(layoutStats);
                break;
            case 6:
                controlResult(layoutResult, 1, 1, 10);
                layoutContent.addView(layoutResult);
                break;
            case 13:
                controlStats(layoutStats, 2, 11, 15);
                layoutContent.addView(layoutStats);
                break;
            case 17:
                controlResult(layoutResult, 2, 11, 15);
                layoutContent.addView(layoutResult);
                break;
            case 14:
                controlStats(layoutStats, 3, 16, 25);
                layoutContent.addView(layoutStats);
                break;
            case 18:
                controlResult(layoutResult, 3, 16, 25);
                layoutContent.addView(layoutResult);
                break;
            case 9:
                controlQuotes(layoutQuotes);
                layoutContent.addView(layoutQuotes);
                break;
            case 10:
                controlClose(layoutClose1, 1);
                layoutContent.addView(layoutClose1);
                break;
            case 11:
                controlClose(layoutClose2, 2);
                layoutContent.addView(layoutClose2);
                break;
            default:
                controlIntro(layoutIntro);
                layoutContent.addView(layoutIntro);
                isLast = true;
                break;
        }

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

    //region control methods

    private static void controlIntro(View view) {

        isLast = false;
        CommonFunctions.updateLeftView(m_context, SECTION_ID, 1);

        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayoutUnconnectedIntro);
        relativeLayout.startAnimation(AnimationUtils.loadAnimation(m_context, R.anim.fadein));

        ImageView imageViewNextButton = (ImageView) view.findViewById(R.id.imageViewNext);

        imageViewNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutContent.removeAllViews();

                controlQuotes(layoutQuotes);
                layoutContent.addView(layoutQuotes);
            }
        });
    }

    //region onClicks

    private static View.OnClickListener onImageView1Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String sQuestion = "Do you feel the ground under you is solid or soft?";

            final Dialog dialog = new Dialog(m_context);
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

                            Question question = questionDataSource.getEntry(SECTION_ID, sQuestion);

                            if(question != null) {
                                question.setAnswer(content);
                                questionDataSource.updateEntry(question);
                            } else {
                                questionDataSource.createNewEntry("", SECTION_ID, sQuestion, content);
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

                Question question = questionDataSource.getEntry(SECTION_ID, sQuestion);

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

    private static View.OnClickListener onImageView2Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String sQuestion = "What do you consider your life foundations to be?";

            final Dialog dialog = new Dialog(m_context);
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

                            Question question = questionDataSource.getEntry(SECTION_ID, sQuestion);

                            if(question != null) {
                                question.setAnswer(content);
                                questionDataSource.updateEntry(question);
                            } else {
                                questionDataSource.createNewEntry("", SECTION_ID, sQuestion, content);
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

                Question question = questionDataSource.getEntry(SECTION_ID, sQuestion);

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

    //endregion

    private static void controlStats(View view, final int page, final int itemsFrom, final int itemsTo) {

        isLast = false;

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

        adapter = null;
        adapter = new ArrayAdapter<String>(m_context, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if(page == 1) {
            CommonFunctions.updateLeftView(m_context, SECTION_ID, 5);

            ((TextView) view.findViewById(R.id.title)).setText(m_context.getResources().getString(R.string.string_unconnected_quiz_title_1));
            ((TextView) view.findViewById(R.id.subtitle)).setText(m_context.getResources().getString(R.string.string_unconnected_quiz_subtitle_1));

        } else if (page == 2) {
            CommonFunctions.updateLeftView(m_context, SECTION_ID, 13);

            ((TextView) view.findViewById(R.id.title)).setText(m_context.getResources().getString(R.string.string_unconnected_quiz_title_2));
            ((TextView) view.findViewById(R.id.subtitle)).setText(m_context.getResources().getString(R.string.string_unconnected_quiz_subtitle_2));
        } else if (page == 3) {
            CommonFunctions.updateLeftView(m_context, SECTION_ID, 14);

            ((TextView) view.findViewById(R.id.title)).setText(m_context.getResources().getString(R.string.string_unconnected_quiz_title_3));
            ((TextView) view.findViewById(R.id.subtitle)).setText(m_context.getResources().getString(R.string.string_unconnected_quiz_subtitle_3));
        }

        view.findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                statsResult = CommonFunctions.getResultInt(m_context, SECTION_ID);

                layoutContent.removeAllViews();
                controlResult(layoutResult, page, itemsFrom, itemsTo);
                layoutContent.addView(layoutResult);
                //overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        ((ScrollView) view.findViewById(R.id.scrollView3)).removeAllViews();
        ((ScrollView) view.findViewById(R.id.scrollView3)).addView(handleQuiz(itemsFrom, itemsTo));
        view.refreshDrawableState();
    }

    private static View handleQuiz(int itemsFrom, int itemsTo) {

        isLast = false;
        TableLayout tableLayoutQuiz = (TableLayout) inflater.inflate(R.layout.layout_quiz_table, null);

        try {
            final QuizDataSource quizDataSource = new QuizDataSource(m_context);
            quizDataSource.open();

            List<Quiz> quizList = quizDataSource.getAllEntries(SECTION_ID);

            int intItem = 1;
            for(final Quiz quiz : quizList) {
                if(intItem >= itemsFrom && intItem <= itemsTo) {
                    TableRow layoutQuizItem = (TableRow) inflater.inflate(R.layout.layout_quiz_item, null);

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
                intItem++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tableLayoutQuiz;
    }

    private static void controlResult(View view, int page, int itemsFrom, int itemsTo) {

        isLast = false;
        statsResult = 0;
        if(page == 1) {
            CommonFunctions.updateLeftView(m_context, SECTION_ID, 6);

            ((TextView) view.findViewById(R.id.textView3)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

            if (statsResult <= 0) {
                statsResult = CommonFunctions.getResultInt(m_context, SECTION_ID, itemsFrom, itemsTo);
            }

            if (10 <= statsResult && statsResult <= 39) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_unconnected_result_1);
            } else if (40 <= statsResult && statsResult <= 69) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_unconnected_result_2);
            } else if (70 <= statsResult && statsResult <= 100) {
                ((TextView) view.findViewById(R.id.textView3)).setText(
                        CommonFunctions.getSpannableStringBuilder(m_context,
                                m_context.getResources().getString(R.string.string_unconnected_result_3)));
            }

            ((TextView) view.findViewById(R.id.textView1)).setText(Integer.toString(statsResult));

            view.findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutContent.removeAllViews();

                    controlQuotes(layoutQuotes);
                    layoutContent.addView(layoutQuotes);
                }
            });

            view.findViewById(R.id.imageViewCentralButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonFunctions.showDialog(m_context, 1, "Ideas and links to help dimensions:");
                }
            });
        } else if (page==2) {
            CommonFunctions.updateLeftView(m_context, SECTION_ID, 17);

            ((TextView) view.findViewById(R.id.textView3)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

            if (statsResult <= 0) {
                statsResult = CommonFunctions.getResultInt(m_context, SECTION_ID, itemsFrom, itemsTo);
            }

            if (5 <= statsResult && statsResult <= 10) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_unconnected_result_4);
            } else if (11 <= statsResult && statsResult <= 25) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_unconnected_result_5);
            } else if (26 <= statsResult && statsResult <= 50) {
                ((TextView) view.findViewById(R.id.textView3)).setText(
                        CommonFunctions.getSpannableStringBuilder(m_context,
                                m_context.getResources().getString(R.string.string_unconnected_result_6)));
            }

            ((TextView) view.findViewById(R.id.textView1)).setText(Integer.toString(statsResult));

            view.findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutContent.removeAllViews();

                    controlQuotes(layoutQuotes);
                    layoutContent.addView(layoutQuotes);
                }
            });

            view.findViewById(R.id.imageViewCentralButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonFunctions.showDialog(m_context, 1, "Ideas and links to help dimensions:");
                }
            });
        } else if (page==3) {
            CommonFunctions.updateLeftView(m_context, SECTION_ID, 18);

            ((TextView) view.findViewById(R.id.textView3)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

            if (statsResult <= 0) {
                statsResult = CommonFunctions.getResultInt(m_context, SECTION_ID, itemsFrom, itemsTo);
            }

            if (10 <= statsResult && statsResult <= 15) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_unconnected_result_7);
            } else if (16 <= statsResult && statsResult <= 50) {
                ((TextView) view.findViewById(R.id.textView3)).setText(
                        CommonFunctions.getSpannableStringBuilder(m_context,
                                m_context.getResources().getString(R.string.string_unconnected_result_8)));
            } else if (51 <= statsResult && statsResult <= 100) {
                ((TextView) view.findViewById(R.id.textView3)).setText(
                        CommonFunctions.getSpannableStringBuilder(m_context,
                                m_context.getResources().getString(R.string.string_unconnected_result_9)));
            }

            ((TextView) view.findViewById(R.id.textView1)).setText(Integer.toString(statsResult));

            view.findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutContent.removeAllViews();

                    controlClose(layoutClose1, 1);
                    layoutContent.addView(layoutClose1);
                }
            });

            view.findViewById(R.id.imageViewCentralButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonFunctions.showDialog(m_context, 1, "Ideas and links to help dimensions:");
                }
            });
        }
    }

    private static void controlQuotes(View view) {

        isLast = false;
        CommonFunctions.updateLeftView(m_context, SECTION_ID, 9);

        m_context = view.getContext();
        frameLayout = (FrameLayout) view.findViewById(R.id.frameLayoutUser);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayoutQuote);
        textViewQuote = (TextView) view.findViewById(R.id.textViewQuote);
        textViewQuestion = (TextView) view.findViewById(R.id.textViewQuestion);
        textViewSourceNDate = (TextView) view.findViewById(R.id.textViewSourceNDate);
        imageViewCartoon = (ImageView) view.findViewById(R.id.imageViewCartoon);
        imageViewFirst = (ImageView) view.findViewById(R.id.imageButtonFirst);
        imageViewPrevious = (ImageView) view.findViewById(R.id.imageButtonPrevious);
        imageViewNext = (ImageView) view.findViewById(R.id.imageButtonNext);
        imageViewLast = (ImageView) view.findViewById(R.id.imageButtonLast);

        imageViewFirst.setOnClickListener(onClickFirst);
        imageViewPrevious.setOnClickListener(onClickPrevious);
        imageViewNext.setOnClickListener(onClickNext);
        imageViewLast.setOnClickListener(onClickLast);

        quoteDataSource = new QuoteDataSource(m_context);

        try {
            quoteDataSource.open();
            quoteList = quoteDataSource.getAllEntries(SECTION_ID);

            SectionDataSource sectionDataSource = new SectionDataSource(m_context);
            sectionDataSource.open();
            currentIdentity = lastQuoteId = sectionDataSource.getEntry(SECTION_ID).getLast_quote();

            if(currentIdentity.isEmpty()) {
                loadDataToControl(0, view);
            } else {
                loadDataToControl(5, view);
            }

        } catch (SQLException ex) {

        }
    }

    //region button clicks

    private static View.OnClickListener onClickFirst = new View.OnClickListener() {
        public void onClick(View v) {
            loadDataToControl(1, layoutQuotes);//overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    };

    private static View.OnClickListener onClickPrevious = new View.OnClickListener() {
        public void onClick(View v) {
            loadDataToControl(2, layoutQuotes);//overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    };

    private static View.OnClickListener onClickNext = new View.OnClickListener() {
        public void onClick(View v) {
            loadDataToControl(3, layoutQuotes);//overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    };

    private static View.OnClickListener onClickLast = new View.OnClickListener() {
        public void onClick(View v) {
            loadDataToControl(4, layoutQuotes);//overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    };

    //endregion

    private static void loadDataToControl(int action, View view) {
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        relativeLayout.setVisibility(View.INVISIBLE);
        //relativeLayout2.setVisibility(View.INVISIBLE);
        Quote currentQuote = null;

        //region get current quote
        if(currentIdentity.isEmpty()) {

            currentQuote = quoteList.get(0);

            currentPosition = 0;
            previousPosition = 0;
            nextPosition = 1;

            currentIdentity = currentQuote.getUnique_id();
            previousIdentity = quoteList.get(previousPosition).getUnique_id();
            nextIdentity = quoteList.get(nextPosition).getUnique_id();

        } else {
            if(action == 1) {
                currentPosition = 0;
                previousPosition = 0;
                nextPosition = 1;

                currentQuote = quoteList.get(currentPosition);

                currentIdentity = quoteList.get(currentPosition).getUnique_id();
                previousIdentity = quoteList.get(previousPosition).getUnique_id();
                nextIdentity = quoteList.get(nextPosition).getUnique_id();
            } else if(action == 2) {
                if(previousPosition != 0) {
                    //currentCB = coreBeliefList.get(previousPosition);

                    currentPosition = previousPosition;
                    previousPosition = currentPosition - 1;

                    if(nextPosition <= quoteList.size() - 1) {
                        nextPosition = currentPosition + 1;
                    } else {
                        nextPosition = currentPosition;
                    }
                } else {
                    currentPosition = 0;
                    previousPosition = 0;
                    nextPosition = 1;
                }

                currentQuote = quoteList.get(currentPosition);

                currentIdentity = quoteList.get(currentPosition).getUnique_id();
                previousIdentity = quoteList.get(previousPosition).getUnique_id();
                nextIdentity = quoteList.get(nextPosition).getUnique_id();
            } else if (action == 3) {

                if(currentIdentity == nextIdentity) {

                    layoutContent.removeAllViews();

                    controlStats(layoutStats, 3, 16, 25);
                    layoutContent.addView(layoutStats);
                }

                if(nextPosition != quoteList.size() - 1) {

                    currentPosition = nextPosition;
                    previousPosition = currentPosition - 1;

                    if(nextPosition <= quoteList.size() - 1) {
                        nextPosition = currentPosition + 1;
                    } else {
                        nextPosition = currentPosition;
                    }

                } else {
                    currentPosition = nextPosition;
                    previousPosition = currentPosition - 1;
                    nextPosition = currentPosition;
                }

                currentQuote = quoteList.get(currentPosition);

                currentIdentity = quoteList.get(currentPosition).getUnique_id();
                previousIdentity = quoteList.get(previousPosition).getUnique_id();
                nextIdentity = quoteList.get(nextPosition).getUnique_id();

            } else if (action == 4) {
                currentPosition = quoteList.size() - 1;
                previousPosition = currentPosition - 1;
                nextPosition = currentPosition;

                currentQuote = quoteList.get(currentPosition);

                currentIdentity = quoteList.get(currentPosition).getUnique_id();
                previousIdentity = quoteList.get(previousPosition).getUnique_id();
                nextIdentity = quoteList.get(nextPosition).getUnique_id();
            } else if (action == 5) {
                currentPosition = CommonFunctions.getLastSessionQuote(m_context, SECTION_ID, lastQuoteId);
                if(currentPosition == 0) {
                    previousPosition = currentPosition;
                    nextPosition = currentPosition + 1;
                } else if(currentPosition == quoteList.size() -1) {
                    previousPosition = currentPosition - 1;
                    nextPosition = currentPosition;
                } else {
                    previousPosition = currentPosition - 1;
                    nextPosition = currentPosition + 1;
                }


                currentQuote = quoteList.get(currentPosition);

                currentIdentity = quoteList.get(currentPosition).getUnique_id();
                previousIdentity = quoteList.get(previousPosition).getUnique_id();
                nextIdentity = quoteList.get(nextPosition).getUnique_id();
            }
        }

        //endregion

        // TODO: there are layout types based on client document. if provided select which layer should be display and assign values
        relativeLayout.setVisibility(View.VISIBLE);

        if(currentQuote != null) {
            CommonFunctions.updateLastSessionQuote(m_context, SECTION_ID, currentQuote.getUnique_id());

            if(nextPosition != currentPosition && CommonFunctions.contains(new int[] {7, 45}, nextPosition))
                mOverride = true;

            if(mOverride) {
                //show second quiz
                if (currentPosition == 7) {
                    mOverride = false;
                    layoutContent.removeAllViews();

                    controlStats(layoutStats, 1, 1, 10);
                    layoutContent.addView(layoutStats);

                } else if (currentPosition == 45) {
                    mOverride = false;
                    layoutContent.removeAllViews();

                    controlStats(layoutStats, 2, 11, 15);
                    layoutContent.addView(layoutStats);
                }
            }

            //setting quote and source and date
            textViewQuote.setText(currentQuote.getQuote());
            textViewSourceNDate.setText(currentQuote.getPerson_source() + " " + currentQuote.getDate());
            //setting image
            if(!currentQuote.getImage().equals("Caricature")) {
                //Drawable drawable = getResources().getDrawable(CommonFunctions.getResId(currentCB.getImage_path(), this.getClass()));
                Drawable drawable = m_context.getResources().getDrawable(CommonFunctions.getResourceId(m_context, currentQuote.getImage(), "mipmap", PakegeName));
                imageViewCartoon.setImageDrawable(drawable);
            } else {
                imageViewCartoon.setImageDrawable(null);
            }
            //render question or option controls
            frameLayout.removeAllViews();
            if(currentQuote.getQuote_type().equals("O")) {
                frameLayout.addView(loadOptionControl(currentQuote, view));
            } else if(currentQuote.getQuote_type().equals("Q")) {
                frameLayout.addView(loadQuestionControl(currentQuote, view));
            }

            handleCommentControl(currentQuote, view);
        }
    }

    //region handle options control
    private static View loadOptionControl(final Quote quote, View view) {
        layoutOption = inflater.inflate(R.layout.layout_option_bullseye, null);

        final ImageView imageViewLike = (ImageView) layoutOption.findViewById(R.id.imageViewLike);
        final ImageView imageViewUnlike = (ImageView) layoutOption.findViewById(R.id.imageViewUnlike);

        //region onlicks
        imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quote.getAnswered()) {
                    try {
                        OptionDataSource optionDataSource = new OptionDataSource(m_context);
                        optionDataSource.open();

                        Option option = optionDataSource.getEntry(quote.getUnique_id());
                        option.setIs_like(false);
                        option.setIs_bullseye(true);

                        optionDataSource.updateEntry(option);

                        quote.setAnswered(true);
                        quoteDataSource.updateEntry(quote);

                        if(option.getIs_like()) {
                            imageViewLike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_like_selected));
                            imageViewUnlike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_unlike_not_selected));
                        } else {
                            imageViewLike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_like_not_selected));
                            imageViewUnlike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_unlike_selected));
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        OptionDataSource optionDataSource = new OptionDataSource(m_context);
                        optionDataSource.open();

                        Option newOption = optionDataSource.createNewEntry(
                                quote.getUnique_id(),
                                false,
                                true
                        );

                        quote.setAnswered(true);
                        quoteDataSource.updateEntry(quote);

                        if(newOption.getIs_bullseye()) {
                            imageViewLike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_like_selected));
                            imageViewUnlike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_unlike_not_selected));
                        } else {
                            imageViewLike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_like_not_selected));
                            imageViewUnlike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_unlike_selected));
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                loadDataToControl(3, view);
            }
        });


        imageViewUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quote.getAnswered()) {
                    try {
                        OptionDataSource optionDataSource = new OptionDataSource(m_context);
                        optionDataSource.open();

                        Option option = optionDataSource.getEntry(quote.getUnique_id());
                        option.setIs_like(false);
                        option.setIs_bullseye(false);

                        optionDataSource.updateEntry(option);

                        quote.setAnswered(true);
                        quoteDataSource.updateEntry(quote);

                        if (option.getIs_bullseye()) {
                            imageViewLike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_like_selected));
                            imageViewUnlike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_unlike_not_selected));
                        } else {
                            imageViewLike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_like_not_selected));
                            imageViewUnlike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_unlike_selected));
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        OptionDataSource optionDataSource = new OptionDataSource(m_context);
                        optionDataSource.open();

                        Option newOption = optionDataSource.createNewEntry(
                                quote.getUnique_id(),
                                false,
                                false
                        );

                        quote.setAnswered(true);
                        quoteDataSource.updateEntry(quote);

                        if (newOption.getIs_bullseye()) {
                            imageViewLike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_like_selected));
                            imageViewUnlike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_unlike_not_selected));
                        } else {
                            imageViewLike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_like_not_selected));
                            imageViewUnlike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_unlike_selected));
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                loadDataToControl(3, view);
            }
        });
        //endregion

        if(quote.getAnswered()) {
            try {
                OptionDataSource optionDataSource = new OptionDataSource(m_context);
                optionDataSource.open();

                Option option = optionDataSource.getEntry(quote.getUnique_id());

                if(option.getIs_bullseye()) {
                    imageViewLike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_like_selected));
                    imageViewUnlike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_unlike_not_selected));
                } else {
                    imageViewLike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_like_not_selected));
                    imageViewUnlike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_unlike_selected));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            imageViewLike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_like));
            imageViewUnlike.setImageDrawable(m_context.getResources().getDrawable(R.mipmap.ic_cg_unlike));
        }

        return layoutOption;
    }
    //endregion

    //region handle question control
    private static View loadQuestionControl(final Quote quote, View view) {
        LinearLayout layoutQuestion = (LinearLayout)inflater.inflate(R.layout.layout_question, null);

        try {
            QuestionDataSource questionDataSource = new QuestionDataSource(m_context);
            questionDataSource.open();

            List<Question> questionList = questionDataSource.getAllEntries(quote.getUnique_id());

            questionDataSource.close();

            for(final Question question : questionList) {
                View layoutQuestionItem = inflater.inflate(R.layout.layout_question_item, null);
                ((TextView)layoutQuestionItem.findViewById(R.id.textViewQuestion)).setText(question.getQuestion());
                ((ImageView) layoutQuestionItem.findViewById(R.id.imageViewQuestion)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(m_context);
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

                                if (!content.trim().isEmpty()) {
                                    try {
                                        QuestionDataSource questionDataSource = new QuestionDataSource(m_context);
                                        questionDataSource.open();

                                        question.setAnswer(content);
                                        questionDataSource.updateEntry(question);

                                        questionDataSource.close();

                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }

                                dialog.cancel();
                            }
                        });

                        ((EditText) dialog.findViewById(R.id.editTextJournalComment)).setText(question.getAnswer());

                        dialog.show();
                    }
                });
                layoutQuestion.addView(layoutQuestionItem);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return layoutQuestion;
    }
    //endregion

    //region handle comment control
    private static void handleCommentControl(final Quote quote, View view) {
        ImageView imageViewAddToJournal = (ImageView) view.findViewById(R.id.imageViewAddToJournal);
        imageViewAddToJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(m_context);
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

                        if (!content.trim().isEmpty()) {
                            try {
                                CommentDataSource commentDataSource = new CommentDataSource(view.getContext());
                                commentDataSource.open();

                                Comment comment = commentDataSource.getEntry(quote.getSection_id(), quote.getUnique_id());

                                if (comment == null) {
                                    commentDataSource.createNewEntry(
                                            quote.getSection_id(),
                                            quote.getUnique_id(),
                                            content);
                                } else {
                                    comment.setComment(content);
                                    commentDataSource.updateEntry(comment);
                                }
                                commentDataSource.close();

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        dialog.cancel();
                    }
                });

                try {
                    CommentDataSource commentDataSource = new CommentDataSource(view.getContext());
                    commentDataSource.open();

                    Comment comment = commentDataSource.getEntry(quote.getSection_id(), quote.getUnique_id());

                    commentDataSource.close();

                    if (comment != null) {
                        ((EditText) dialog.findViewById(R.id.editTextJournalComment)).setText(comment.getComment());
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                dialog.show();
            }
        });
    }
    //endregion

    private static void controlClose(View view, int page) {

        isLast = false;

        if(page == 1) {
            CommonFunctions.updateLeftView(m_context, SECTION_ID, 10);

            ((TextView) view.findViewById(R.id.textViewClose1)).setTypeface(CommonFunctions.getTypeFace(m_context, 4));
            ((TextView) view.findViewById(R.id.textViewClose2)).setTypeface(CommonFunctions.getTypeFace(m_context, 3));
            ((TextView) view.findViewById(R.id.textViewClose3)).setTypeface(CommonFunctions.getTypeFace(m_context, 3));


            view.findViewById(R.id.imageViewClose1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse("http://connect4life.org.uk/unconnected/unconnected-introduction");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    m_context.startActivity(intent);
                }
            });

            view.findViewById(R.id.imageViewClose2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //http://connect4life.org.uk/church-growth-resources
                    Uri uri = Uri.parse("http://iamredemption.org/stories");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    m_context.startActivity(intent);
                }
            });

            view.findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutContent.removeAllViews();

                    controlClose(layoutClose2, 2);
                    layoutContent.addView(layoutClose2);
                }
            });
        } else if (page ==2) {
            CommonFunctions.updateLeftView(m_context, SECTION_ID, 11);

            ((TextView) view.findViewById(R.id.textViewClose1)).setTypeface(CommonFunctions.getTypeFace(m_context, 1));
            ((TextView) view.findViewById(R.id.textViewClose2)).setTypeface(CommonFunctions.getTypeFace(m_context, 1));

            view.findViewById(R.id.imageViewCloseTwitter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //http://connect4life.org.uk/best_connections_android/"
                    Uri uri = Uri.parse("http://connect4life.org.uk/best_connections_android/");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    m_context.startActivity(intent);
                }
            });

            view.findViewById(R.id.imageViewCloseGoogle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //http://connect4life.org.uk/best_connections_android/"
                    Uri uri = Uri.parse("http://connect4life.org.uk/best_connections_android/");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    m_context.startActivity(intent);
                }
            });

            view.findViewById(R.id.imageViewCloseFacebook).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //http://connect4life.org.uk/best_connections_android/"
                    Uri uri = Uri.parse("http://connect4life.org.uk/best_connections_android/");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    m_context.startActivity(intent);
                }
            });
        }
    }

    //endregion

    //region back navigation method

    public static boolean backControl() {

        if(isLast) {
            return true;
        }

        layoutContent.removeAllViews();

        int leftView = CommonFunctions.getLeftView(m_context, SECTION_ID);


        switch (leftView) {
            case 5:
                controlIntro(layoutIntro);
                layoutContent.addView(layoutIntro);
                isLast = true;
            case 14:
                controlQuotes(layoutQuotes);
                layoutContent.addView(layoutQuotes);
                break;
            case 18:
                controlStats(layoutStats, 3, 16, 25);
                layoutContent.addView(layoutStats);
                break;
            case 10:
                controlResult(layoutResult, 3, 16, 25);
                layoutContent.addView(layoutResult);
                break;
            case 11:
                controlClose(layoutClose1, 1);
                layoutContent.addView(layoutClose1);
                break;
            default:
                controlIntro(layoutIntro);
                layoutContent.addView(layoutIntro);
                isLast = true;
                break;
        }

        return false;
    }

    //endregion

    //region gen
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
            if(backControl()) {
                activeActivity = 0;
                currentActivity = 0;
                this.finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {

        activeActivity = 7;
        currentActivity = 7;
        CommonFunctions.updateLeftView(m_context, SECTION_ID, CommonFunctions.getLeftView(m_context, SECTION_ID));

        super.onResume();
    }
    //endregion
}
