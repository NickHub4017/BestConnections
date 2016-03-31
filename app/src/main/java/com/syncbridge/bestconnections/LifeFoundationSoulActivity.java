package com.syncbridge.bestconnections;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Classes.CommonFunctions;
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


public class LifeFoundationSoulActivity extends Activity {

    private static String SECTION_ID = "LFSO";
    private static String PakegeName;

    private static Context m_context;
    private static ArrayAdapter<String> adapter;
    private static FragmentManager mFragmentManager;

    private static View layoutIntroduction, layoutIntro, layoutMaslow1, layoutStats, layoutResult,
            layoutQuotes, layoutClose1, layoutMyers;
    private static FrameLayout layoutContent;
    private static int statsResult = 0;

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

    private static boolean mOverride = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_foundation_soul);

        m_context = LifeFoundationSoulActivity.this;
        PakegeName = getPackageName();

        layoutContent = (FrameLayout) findViewById(R.id.layoutContent);

        inflater = getLayoutInflater();
        layoutIntroduction = inflater.inflate(R.layout.layout_life_foundation_soul_introduction, null);
        layoutIntro = inflater.inflate(R.layout.layout_life_foundation_soul_intro, null);
        //layoutMaslow1 = inflater.inflate(R.layout.layout_life_foundation_soul_maslow, null);
        layoutStats = inflater.inflate(R.layout.layout_life_foundation_soul_stats, null);
        layoutResult = inflater.inflate(R.layout.layout_life_foundation_soul_result, null);
        layoutQuotes = inflater.inflate(R.layout.layout_life_foundation_soul_quotes, null);
        layoutClose1 = inflater.inflate(R.layout.layout_life_foundation_soul_close1, null);
        layoutMyers = inflater.inflate(R.layout.layout_life_foundation_soul_myers_briggs, null);

        layoutContent.removeAllViews();

        int leftView = CommonFunctions.getLeftView(m_context, SECTION_ID);

        switch (leftView) {
            case 1:
                controlIntroduction(layoutIntroduction);
                layoutContent.addView(layoutIntroduction);
                isLast = true;
                break;
            case 2:
                controlIntro(layoutIntro);
                layoutContent.addView(layoutIntro);
                break;
            case 5:
                controlStats(layoutStats, 1, 1, 10);
                layoutContent.addView(layoutStats);
                break;
            case 6:
                controlResult(layoutResult, 1, 1, 10);
                layoutContent.addView(layoutResult);
                break;
            case 9:
                controlQuotes(layoutQuotes);
                layoutContent.addView(layoutQuotes);
                break;
            case 10:
                controlClose1(layoutClose1);
                layoutContent.addView(layoutClose1);
                break;
            case 13:
                controlStats(layoutStats, 2, 11, 20);
                layoutContent.addView(layoutStats);
                break;
            case 14:
                controlStats(layoutStats, 3, 21, 30);
                layoutContent.addView(layoutStats);
                break;
            case 15:
                controlStats(layoutStats, 4, 31, 40);
                layoutContent.addView(layoutStats);
                break;
            case 17:
                controlResult(layoutResult, 2, 11, 20);
                layoutContent.addView(layoutResult);
                break;
            case 18:
                controlResult(layoutResult, 3, 21, 30);
                layoutContent.addView(layoutResult);
                break;
            case 19:
                controlResult(layoutResult, 4, 31, 40);
                layoutContent.addView(layoutResult);
                break;
            case 21:
                controlMyers(layoutMyers);
                layoutContent.addView(layoutMyers);
                break;
            default:
                controlIntroduction(layoutIntroduction);
                layoutContent.addView(layoutIntroduction);
                isLast = true;
                break;
        }
        
    }


    //region control methods

    private static void controlIntroduction(View view) {

        CommonFunctions.updateLeftView(m_context, SECTION_ID, 1);

        ((TextView) view.findViewById(R.id.textViewQuote)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

        ImageView imageViewNextButton = (ImageView) view.findViewById(R.id.imageViewNext);

        imageViewNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutContent.removeAllViews();

                controlIntro(layoutIntro);
                layoutContent.addView(layoutIntro);
            }
        });
    }

    private static void controlIntro(View view) {

        isLast = false;
        CommonFunctions.updateLeftView(m_context, SECTION_ID, 2);

        ((TextView) view.findViewById(R.id.textViewQuote)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView) view.findViewById(R.id.textViewSourceNDate)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView) view.findViewById(R.id.textViewQuestion1)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView) view.findViewById(R.id.textViewQuestion2)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

        ImageView imageViewNextButton = (ImageView) view.findViewById(R.id.imageViewNext);

        view.findViewById(R.id.imageViewQuestion1).setOnClickListener(onImageView1Click);
        view.findViewById(R.id.imageViewQuestion2).setOnClickListener(onImageView2Click);

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
            final String sQuestion = "How do you think things through - conceptually, abstractly, verbally, writing it down?";

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
            final String sQuestion = "Do you use many or few words?";

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

    private static void controlStats(View view, int page, final int itemsFrom, final int itemsTo) {

        isLast = false;

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

            ((TextView) view.findViewById(R.id.title)).setText(m_context.getResources().getString(R.string.string_lfso_quiz_title_1));
            ((TextView) view.findViewById(R.id.subtitle)).setText(m_context.getResources().getString(R.string.string_lfso_quiz_subtitle_1));

            view.findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (statsResult <= 0) {
//                        statsResult = CommonFunctions.getResultInt(m_context, SECTION_ID);
//                    }

                    layoutContent.removeAllViews();
                    controlResult(layoutResult, 1, itemsFrom, itemsTo);
                    layoutContent.addView(layoutResult);
                    //overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            });
        } else if(page == 2) {
            CommonFunctions.updateLeftView(m_context, SECTION_ID, 13);

            ((TextView) view.findViewById(R.id.title)).setText(m_context.getResources().getString(R.string.string_lfso_quiz_title_2));
            ((TextView) view.findViewById(R.id.subtitle)).setText(m_context.getResources().getString(R.string.string_lfso_quiz_subtitle_2));

            view.findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if(statsResult <= 0) {
//                        statsResult = CommonFunctions.getResultInt(m_context, SECTION_ID);
//                    }

                    layoutContent.removeAllViews();
                    controlResult(layoutResult, 2, itemsFrom, itemsTo);
                    layoutContent.addView(layoutResult);
                    //overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            });
        } else if(page == 3) {
            CommonFunctions.updateLeftView(m_context, SECTION_ID, 14);

            ((TextView) view.findViewById(R.id.title)).setText(m_context.getResources().getString(R.string.string_lfso_quiz_title_3));
            ((TextView) view.findViewById(R.id.subtitle)).setText(m_context.getResources().getString(R.string.string_lfso_quiz_subtitle_3));

            view.findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if(statsResult <= 0) {
//                        statsResult = CommonFunctions.getResultInt(m_context, SECTION_ID);
//                    }

                    layoutContent.removeAllViews();
                    controlResult(layoutResult, 3, itemsFrom, itemsTo);
                    layoutContent.addView(layoutResult);
                    //overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            });
        } else if(page == 4) {
            CommonFunctions.updateLeftView(m_context, SECTION_ID, 15);

            ((TextView) view.findViewById(R.id.title)).setText(m_context.getResources().getString(R.string.string_lfso_quiz_title_4));
            ((TextView) view.findViewById(R.id.subtitle)).setText(m_context.getResources().getString(R.string.string_lfso_quiz_subtitle_4));

            view.findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if(statsResult <= 0) {
//                        statsResult = CommonFunctions.getResultInt(m_context, SECTION_ID);
//                    }

                    layoutContent.removeAllViews();
                    controlResult(layoutResult, 4, itemsFrom, itemsTo);
                    layoutContent.addView(layoutResult);
                    //overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            });
        }

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

        statsResult = 0;

        if(page == 1) {
            isLast = false;
            CommonFunctions.updateLeftView(m_context, SECTION_ID, 6);

            ((TextView) view.findViewById(R.id.textView3)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

            if (statsResult <= 0) {
                statsResult = CommonFunctions.getResultInt(m_context, SECTION_ID, itemsFrom, itemsTo);
            }

            if (10 <= statsResult && statsResult <= 45) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_lfso_result_1);
            } else if (46 <= statsResult && statsResult <= 75) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_lfso_result_2);
            } else if (76 <= statsResult && statsResult <= 100) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_lfso_result_3);
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
                    CommonFunctions.showDialog(m_context, 2, "Information:");
                }
            });
        } else if(page == 2) {
            isLast = false;
            CommonFunctions.updateLeftView(m_context, SECTION_ID, 17);

            ((TextView) view.findViewById(R.id.textView3)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

            if (statsResult <= 0) {
                statsResult = CommonFunctions.getResultInt(m_context, SECTION_ID, itemsFrom, itemsTo);
            }

            if (10 <= statsResult && statsResult <= 45) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_lfso_result_4);
            } else if (46 <= statsResult && statsResult <= 75) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_lfso_result_5);
            } else if (76 <= statsResult && statsResult <= 100) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_lfso_result_6);
            }

            ((TextView) view.findViewById(R.id.textView1)).setText(Integer.toString(statsResult));

            view.findViewById(R.id.imageViewCentralButton).setVisibility(View.INVISIBLE);

            view.findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutContent.removeAllViews();

                    controlQuotes(layoutQuotes);
                    layoutContent.addView(layoutQuotes);
                }
            });
        } else if(page == 3) {
            isLast = false;
            CommonFunctions.updateLeftView(m_context, SECTION_ID, 18);

            ((TextView) view.findViewById(R.id.textView3)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

            if (statsResult <= 0) {
                statsResult = CommonFunctions.getResultInt(m_context, SECTION_ID, itemsFrom, itemsTo);
            }

            if (10 <= statsResult && statsResult <= 45) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_lfso_result_7);
            } else if (46 <= statsResult && statsResult <= 75) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_lfso_result_8);
            } else if (76 <= statsResult && statsResult <= 100) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_lfso_result_9);
            }

            ((TextView) view.findViewById(R.id.textView1)).setText(Integer.toString(statsResult));

            view.findViewById(R.id.imageViewCentralButton).setVisibility(View.INVISIBLE);

            view.findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutContent.removeAllViews();

                    controlQuotes(layoutQuotes);
                    layoutContent.addView(layoutQuotes);
                }
            });
        } else if(page == 4) {
            isLast = false;
            CommonFunctions.updateLeftView(m_context, SECTION_ID, 19);

            ((TextView) view.findViewById(R.id.textView3)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

            if (statsResult <= 0) {
                statsResult = CommonFunctions.getResultInt(m_context, SECTION_ID, itemsFrom, itemsTo);
            }

            if (10 <= statsResult && statsResult <= 45) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_lfso_result_10);
            } else if (46 <= statsResult && statsResult <= 75) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_lfso_result_11);
            } else if (76 <= statsResult && statsResult <= 100) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_lfso_result_12);
            }

            ((TextView) view.findViewById(R.id.textView1)).setText(Integer.toString(statsResult));

            view.findViewById(R.id.imageViewCentralButton).setVisibility(View.INVISIBLE);

            view.findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutContent.removeAllViews();

                    controlMyers(layoutMyers);
                    layoutContent.addView(layoutMyers);
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
            // TODO: set the currentCB based on action (0=initial, 1=first, 2=previous, 3=next, 4=last)
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
                    CommonFunctions.updateSectionFinished(m_context, SECTION_ID, true);
                    LifeFoundationActivity.tabHost.setCurrentTab(2);
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

        relativeLayout.setVisibility(View.VISIBLE);

        if(currentQuote != null) {
            CommonFunctions.updateLastSessionQuote(m_context, SECTION_ID, currentQuote.getUnique_id());

            if(nextPosition != currentPosition && CommonFunctions.contains(new int[] {3, 14, 23, 38, 57}, nextPosition))
                mOverride = true;

            if(mOverride) {
                //show second quiz
                if (currentPosition == 3) {
                    mOverride = false;
                    layoutContent.removeAllViews();

                    controlStats(layoutStats, 1, 1, 10);
                    layoutContent.addView(layoutStats);

                } else if (currentPosition == 14) {
                    mOverride = false;
                    layoutContent.removeAllViews();

                    controlStats(layoutStats, 2, 11, 20);
                    layoutContent.addView(layoutStats);

                } else if (currentPosition == 23) {
                    mOverride = false;
                    layoutContent.removeAllViews();

                    controlStats(layoutStats, 3, 21, 30);
                    layoutContent.addView(layoutStats);

                } else if (currentPosition == 38) {
                    mOverride = false;
                    layoutContent.removeAllViews();

                    controlStats(layoutStats, 4, 31, 40);
                    layoutContent.addView(layoutStats);

                } else if (currentPosition == 57) {
                    mOverride = false;
                    layoutContent.removeAllViews();

                    controlClose1(layoutClose1);
                    layoutContent.addView(layoutClose1);
                }
            }

            //setting quote and source and date
            textViewQuote.setText(currentQuote.getQuote());
            textViewSourceNDate.setText(currentQuote.getPerson_source() + " " + currentQuote.getDate());
            //setting image
            if(!currentQuote.getImage().equals("Caricature")) {
                //Drawable drawable = getResources().getDrawable(CommonFunctions.getResId(currentCB.getImage_path(), this.getClass()));
                //Toast.makeText(m_context,currentQuote.getImage(),Toast.LENGTH_LONG).show();
                Drawable drawable = m_context.getResources().getDrawable(CommonFunctions.getResourceId(m_context, currentQuote.getImage(), "mipmap", PakegeName));
                imageViewCartoon.setImageDrawable(drawable);
            } else {
                //Toast.makeText(m_context,currentQuote.getImage(),Toast.LENGTH_LONG).show();
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

    private static void controlClose1(View view) {

        isLast = false;

        CommonFunctions.updateLeftView(m_context, SECTION_ID, 10);

        ((TextView) view.findViewById(R.id.textViewQuote)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

        view.findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutContent.removeAllViews();

                controlQuotes(layoutQuotes);
                layoutContent.addView(layoutQuotes);
            }
        });
    }

    private static void controlMyers(final View view) {
        isLast = false;
        final int[] clickCount = {0};

        clickCount[0] = 1;

        CommonFunctions.updateLeftView(m_context, SECTION_ID, 21);

        final TextView secondArea = (TextView) view.findViewById(R.id.secondArea);
        final LinearLayout firstArea = (LinearLayout) view.findViewById(R.id.firstArea);

        String letter1 = null;
        String letter2 = null;
        String letter3 = null;
        String letter4 = null;

        int result1 = CommonFunctions.getResultInt(m_context, SECTION_ID, 1, 10);
        int result2 = CommonFunctions.getResultInt(m_context, SECTION_ID, 11, 20);
        int result3 = CommonFunctions.getResultInt(m_context, SECTION_ID, 21, 30);
        int result4 = CommonFunctions.getResultInt(m_context, SECTION_ID, 31, 40);

        if(result1 < 55) {
            letter1 = "I";
        } else if(result1 >= 56) {
            letter1 = "E";
        }

        if(result2 < 55) {
            letter2 = "S";
        } else if(result2 >= 56) {
            letter2 = "I";
        }

        if(result3 < 55) {
            letter3 = "F";
        } else if(result3 >= 56) {
            letter3 = "T";
        }

        if(result4 < 55) {
            letter4 = "P";
        } else if(result4 >= 56) {
            letter4 = "J";
        }

        final String result = letter1 + letter2 + letter3 + letter4;
        ((TextView) view.findViewById(R.id.result)).setText(result);

        runProgressThread(((ProgressBar) view.findViewById(R.id.progressBar1)), result1);
        runProgressThread(((ProgressBar) view.findViewById(R.id.progressBar2)), result2);
        runProgressThread(((ProgressBar) view.findViewById(R.id.progressBar3)), result3);
        runProgressThread(((ProgressBar) view.findViewById(R.id.progressBar4)), result4);

        view.findViewById(R.id.imageViewCentralButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCount[0] == 0) {
                    clickCount[0] = 1;

                    ((ImageView) view.findViewById(R.id.imageViewCentralButton)).setImageDrawable(
                            m_context.getResources().getDrawable(R.mipmap.ic_central_button7));

                    firstArea.setVisibility(View.INVISIBLE);

                    view.findViewById(R.id.mbit).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.mbit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = Uri.parse("http://www.myersbriggs.org/my-mbti-personality-type/mbti-basics/the-16-mbti-types.htm");
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            m_context.startActivity(intent);
                        }
                    });
                    secondArea.setText(CommonFunctions.getMyersBriggsString(m_context, result));
                    secondArea.setVisibility(View.VISIBLE);
                } else if (clickCount[0] == 1) {
                    clickCount[0] = 0;

                    ((ImageView) view.findViewById(R.id.imageViewCentralButton)).setImageDrawable(
                            m_context.getResources().getDrawable(R.mipmap.ic_central_button6));

                    view.findViewById(R.id.mbit).setVisibility(View.GONE);

                    secondArea.setVisibility(View.INVISIBLE);
                    secondArea.setText("");

                    firstArea.setVisibility(View.VISIBLE);
                }
            }
        });

        view.findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutContent.removeAllViews();

                controlQuotes(layoutQuotes);
                layoutContent.addView(layoutQuotes);
            }
        });

    }

    private static int pStatus = 0;
    private static void runProgressThread(final ProgressBar pBar, final int pMaxVal) {
        final Handler handler = new Handler();
        pStatus = 0;
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (pStatus <= pMaxVal) {
                    pStatus++;

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            pBar.setProgress(pStatus);
                        }
                    });
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
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
            case 2:
                controlIntroduction(layoutIntroduction);
                layoutContent.addView(layoutIntroduction);
                isLast = true;
                break;
            case 5:
                controlIntro(layoutIntro);
                layoutContent.addView(layoutIntro);
                break;
            case 6:
                controlStats(layoutStats, 1, 1, 10);
                layoutContent.addView(layoutStats);
                break;
            case 9:
                controlResult(layoutResult, 1, 1, 10);
                layoutContent.addView(layoutResult);
                break;
            case 10:
                controlQuotes(layoutQuotes);
                layoutContent.addView(layoutQuotes);
                break;
            case 13:
                controlQuotes(layoutQuotes);
                layoutContent.addView(layoutQuotes);
                break;
            case 14:
                controlQuotes(layoutQuotes);
                layoutContent.addView(layoutQuotes);
                break;
            case 15:
                controlQuotes(layoutQuotes);
                layoutContent.addView(layoutQuotes);
                break;
            case 17:
                controlStats(layoutStats, 2, 11, 20);
                layoutContent.addView(layoutStats);
                break;
            case 18:
                controlStats(layoutStats, 3, 21, 30);
                layoutContent.addView(layoutStats);
                break;
            case 19:
                controlStats(layoutStats, 4, 31, 40);
                layoutContent.addView(layoutStats);
                break;
            case 21:
                controlResult(layoutResult, 4, 31, 40);
                layoutContent.addView(layoutResult);
                break;
            default:
                return true;
        }

        return false;
    }

    //endregion

    //region gen

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_life_foundation_soul, menu);
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

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        if(currentPosition!=0) {
            loadDataToControl(2, layoutQuotes);//overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
        else {
            if (MainActivity.currentActivity == 0 && MainActivity.activeActivity == 0) {
                super.onBackPressed();
            }
        }

    }

    //endregion
}
