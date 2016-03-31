package com.syncbridge.bestconnections;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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


public class LifeFoundationSpiritActivity extends Activity {

    private static String SECTION_ID = "LFSP";
    private static String PakegeName;

    private static Context m_context;
    private static ArrayAdapter<String> adapter;

    private static View layoutOption;
    private static FrameLayout frameLayout;
    private static ScrollView scrollView;

    private static View layoutIntro, layoutIntro2, layoutStats, layoutResult, layoutQuotes, layoutClose1, layoutClose2, layoutClose3;
    private static FrameLayout layoutContent;
    private static int statsResult = 0;

    private static boolean isLast = false;

    private static LayoutInflater inflater;
    private static RelativeLayout relativeLayout;
    private static TextView textViewQuote;
    private static TextView textViewSourceNDate;
    private static TextView textViewQuestion;
    private static ImageView imageViewYouTube;
    private static ImageView imageViewCartoon;
    private static ImageView imageViewFirst;
    private static ImageView imageViewPrevious;
    private static ImageView imageViewNext;
    private static ImageView imageViewLast;
    private static ImageView imageViewAddToJournal;
    private static ImageView imageViewQuestion1;
    private static ImageView imageViewQuestion2;
    private static TextView textViewQuestion1;
    private static TextView textViewQuestion2;

    private static String previousIdentity = "";
    private static String currentIdentity = "";
    private static String nextIdentity = "";

    private static int previousPosition = 0;
    private static int currentPosition = 0;
    private static int nextPosition = 0;

    private static List<Quote> quoteList;
    private static QuoteDataSource quoteDataSource;

    private static String lastQuoteId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_foundation_spirit);

        m_context = LifeFoundationSpiritActivity.this;
        PakegeName = getPackageName();

        layoutContent = (FrameLayout) findViewById(R.id.layoutContent);

        inflater = getLayoutInflater();
        layoutIntro = inflater.inflate(R.layout.layout_life_foundation_spirit_intro, null);
        layoutIntro2 = inflater.inflate(R.layout.layout_life_foundation_spirit_intro2, null);
        layoutStats = inflater.inflate(R.layout.layout_life_foundation_spirit_stats, null);
        layoutResult = inflater.inflate(R.layout.layout_life_foundation_spirit_result, null);
        layoutQuotes = inflater.inflate(R.layout.layout_life_foundation_spirit_quotes, null);
        layoutClose1 = inflater.inflate(R.layout.layout_life_foundation_spirit_close1, null);
        layoutClose2 = inflater.inflate(R.layout.layout_life_foundation_spirit_close2, null);
        layoutClose3 = inflater.inflate(R.layout.layout_life_foundation_spirit_close3, null);

        layoutContent.removeAllViews();

        int leftView = CommonFunctions.getLeftView(m_context, SECTION_ID);

        switch (leftView) {
            case 2:
                controlIntro(layoutIntro);
                layoutContent.addView(layoutIntro);
                isLast = true;
                break;
            case 3:
                controlIntro2(layoutIntro2);
                layoutContent.addView(layoutIntro2);
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
                controlClose(layoutClose1, 1);
                layoutContent.addView(layoutClose1);
                break;
            case 11:
                controlClose(layoutClose2, 2);
                layoutContent.addView(layoutClose2);
                break;
            case 12:
                controlClose(layoutClose3, 3);
                layoutContent.addView(layoutClose3);
                break;
            case 13:
                controlStats(layoutStats, 2, 11, 20);
                layoutContent.addView(layoutStats);
                break;
            case 17:
                controlResult(layoutResult, 2, 11, 20);
                layoutContent.addView(layoutResult);
                break;
            default:
                controlIntro(layoutIntro);
                layoutContent.addView(layoutIntro);
                isLast = true;
                break;
        }

    }

    //region control methods

    private static void controlIntro(View view) {

        isLast = false;
        CommonFunctions.updateLeftView(m_context, SECTION_ID, 2);

        ((TextView) view.findViewById(R.id.textViewQuote)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView) view.findViewById(R.id.textViewSourceNDate)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView) view.findViewById(R.id.textViewQuestion1)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView) view.findViewById(R.id.textViewIntro1)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

        ImageView imageViewNextButton = (ImageView) view.findViewById(R.id.imageViewNext);

        view.findViewById(R.id.imageViewQuestion1).setOnClickListener(onImageView1Click);

        imageViewNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutContent.removeAllViews();

                controlIntro2(layoutIntro2);
                layoutContent.addView(layoutIntro2);
            }
        });
    }

    //region onClicks

    private static View.OnClickListener onImageView1Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String sQuestion = "How about you?";

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

    private static void controlIntro2(View view) {

        isLast = false;
        CommonFunctions.updateLeftView(m_context, SECTION_ID, 3);

        ((TextView) view.findViewById(R.id.textViewQuote)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView) view.findViewById(R.id.textViewSourceNDate)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView) view.findViewById(R.id.textViewIntro1)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

        ImageView imageViewNextButton = (ImageView) view.findViewById(R.id.imageViewNext);

        imageViewNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutContent.removeAllViews();

                controlStats(layoutStats, 1, 1, 10);
                layoutContent.addView(layoutStats);
            }
        });
    }

    private static void controlStats(View view, final int page, final int itemsFrom, final int itemsTo) {

        isLast = false;

        List<String> spinnerArray = new ArrayList<String>();
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

            ((TextView) view.findViewById(R.id.title)).setText(m_context.getResources().getString(R.string.string_lfsp_quiz_title_1));
            ((TextView) view.findViewById(R.id.subtitle)).setText(m_context.getResources().getString(R.string.string_lfsp_quiz_subtitle_1));

            view.findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    layoutContent.removeAllViews();
                    controlResult(layoutResult, page, itemsFrom, itemsTo);
                    layoutContent.addView(layoutResult);
                    //overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            });

        } else if (page == 2) {

            CommonFunctions.updateLeftView(m_context, SECTION_ID, 13);

            ((TextView) view.findViewById(R.id.title)).setText(m_context.getResources().getString(R.string.string_lfsp_quiz_title_2));
            ((TextView) view.findViewById(R.id.subtitle)).setText(m_context.getResources().getString(R.string.string_lfsp_quiz_subtitle_2));

            view.findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    layoutContent.removeAllViews();
                    controlResult(layoutResult, page, itemsFrom, itemsTo);
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

        TableLayout tableLayoutQuiz = (TableLayout) inflater.inflate(R.layout.layout_quiz_table, null);

        try {
            final QuizDataSource quizDataSource = new QuizDataSource(m_context);
            quizDataSource.open();

            List<Quiz> quizList = quizDataSource.getAllEntries(SECTION_ID);

            int intItem = 1;
            for (final Quiz quiz : quizList) {
                if (intItem >= itemsFrom && intItem <= itemsTo) {
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
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_lfsp_result_1);
            } else if (46 <= statsResult && statsResult <= 75) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_lfsp_result_2);
            } else if (76 <= statsResult && statsResult <= 100) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_lfsp_result_3);
            }

            ((TextView) view.findViewById(R.id.textView1)).setText(Integer.toString(statsResult));

            ImageView imageViewNextButton = (ImageView) view.findViewById(R.id.imageViewNext);
            imageViewNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutContent.removeAllViews();

                    controlQuotes(layoutQuotes);
                    layoutContent.addView(layoutQuotes);
                }
            });
        } else if (page == 2) {
            isLast = false;
            CommonFunctions.updateLeftView(m_context, SECTION_ID, 17);

            ((TextView) view.findViewById(R.id.textView3)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

            if (statsResult <= 0) {
                statsResult = CommonFunctions.getResultInt(m_context, SECTION_ID, itemsFrom, itemsTo);
            }

            if (10 <= statsResult && statsResult <= 30) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_lfsp_result_4);
            } else if (31 <= statsResult && statsResult <= 75) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_lfsp_result_5);
            } else if (76 <= statsResult && statsResult <= 100) {
                ((TextView) view.findViewById(R.id.textView3)).setText(R.string.string_lfsp_result_6);
            }

            ((TextView) view.findViewById(R.id.textView1)).setText(Integer.toString(statsResult));

            ImageView imageViewNextButton = (ImageView) view.findViewById(R.id.imageViewNext);
            imageViewNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutContent.removeAllViews();

                    controlClose(layoutClose1, 1);
                    layoutContent.addView(layoutClose1);
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
        imageViewYouTube = (ImageView) view.findViewById(R.id.imageViewYouTube);
        imageViewCartoon = (ImageView) view.findViewById(R.id.imageViewCartoon);
        imageViewFirst = (ImageView) view.findViewById(R.id.imageButtonFirst);
        imageViewPrevious = (ImageView) view.findViewById(R.id.imageButtonPrevious);
        imageViewNext = (ImageView) view.findViewById(R.id.imageButtonNext);
        imageViewLast = (ImageView) view.findViewById(R.id.imageButtonLast);

        imageViewYouTube.setOnClickListener(onClickYouTube);
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

    private static View.OnClickListener onClickYouTube = new View.OnClickListener() {
        public void onClick(View v) {
            try {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + "aboZctrHfK8"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                m_context.startActivity(intent);

            }catch(ActivityNotFoundException e) {

                // youtube is not installed.Will be opened in other available apps

                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=aboZctrHfK8"));
                m_context.startActivity(i);
            }
        }
    };

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
                    layoutContent.removeAllViews();

                    controlStats(layoutStats, 2, 11, 20);
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

            if(currentPosition == 14) {
                imageViewYouTube.setVisibility(View.VISIBLE);
            } else {
                imageViewYouTube.setVisibility(View.GONE);
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

            ((TextView) view.findViewById(R.id.textViewQuote)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
            ((TextView) view.findViewById(R.id.textViewIntro1)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

            view.findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutContent.removeAllViews();

                    controlClose(layoutClose2, 2);
                    layoutContent.addView(layoutClose2);
                }
            });
        }else if(page == 2) {
            CommonFunctions.updateLeftView(m_context, SECTION_ID, 11);

//            ((TextView) view.findViewById(R.id.textViewQuote)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
//            ((TextView) view.findViewById(R.id.textViewSourceNDate)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
//            ((TextView) view.findViewById(R.id.textViewSourceNDate)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

            view.findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutContent.removeAllViews();

                    controlClose(layoutClose3, 3);
                    layoutContent.addView(layoutClose3);
                }
            });
        }else if(page == 3) {
            isLast = false;
            CommonFunctions.updateLeftView(m_context, SECTION_ID, 12);
            view.findViewById(R.id.imageViewIntro).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SECTION_ID = "UC";
                    String lastQuoteId = "";
                    try {
                        SectionDataSource sectionDataSource = new SectionDataSource(m_context);
                        sectionDataSource.open();
                        lastQuoteId = sectionDataSource.getEntry(SECTION_ID).getLast_quote();
                    } catch (SQLException ex) {
                        Log.e(LifeFoundationActivity.TAG, ex.getMessage(), ex);
                    }
                    if (LifeFoundationActivity.activeActivity != 7) {
                        if (LifeFoundationActivity.activeActivity != 0) {
                            LifeFoundationActivity.activeActivity = 7;
                            CommonFunctions.closePreviousActivities(LifeFoundationActivity.activeActivity);
                        }
                        LifeFoundationActivity.activeActivity = 7;
                        if (lastQuoteId.isEmpty()) {
                            Intent i = new Intent(m_context, UnconnectedActivity.class);
                            i.putExtra("SECTION", SECTION_ID);
                            m_context.startActivity(i);
                            //overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                        } else {
                            Intent i = new Intent(m_context, UnconnectedActivity.class);
                            i.putExtra("SECTION", SECTION_ID);
                            i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                            m_context.startActivity(i);
                            //overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                        }
                    }
                }
            });

            view.findViewById(R.id.imageViewIntro2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SECTION_ID = "DC";
                    String lastQuoteId = "";
                    try {
                        SectionDataSource sectionDataSource = new SectionDataSource(m_context);
                        sectionDataSource.open();
                        lastQuoteId = sectionDataSource.getEntry(SECTION_ID).getLast_quote();
                    } catch (SQLException ex) {
                        Log.e(LifeFoundationActivity.TAG, ex.getMessage(), ex);
                    }
                    if (LifeFoundationActivity.activeActivity != 7) {
                        if (LifeFoundationActivity.activeActivity != 0) {
                            LifeFoundationActivity.activeActivity = 7;
                            CommonFunctions.closePreviousActivities(LifeFoundationActivity.activeActivity);
                        }
                        LifeFoundationActivity.activeActivity = 7;
                        if (lastQuoteId.isEmpty()) {
                            Intent i = new Intent(m_context, DisconnectedActivity.class);
                            i.putExtra("SECTION", SECTION_ID);
                            m_context.startActivity(i);
                            //overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                        } else {
                            Intent i = new Intent(m_context, DisconnectedActivity.class);
                            i.putExtra("SECTION", SECTION_ID);
                            i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                            m_context.startActivity(i);
                            //overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                        }
                    }
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
            case 3:
                controlIntro(layoutIntro);
                layoutContent.addView(layoutIntro);
                isLast = true;
                break;
            case 5:
                controlIntro2(layoutIntro2);
                layoutContent.addView(layoutIntro2);
                break;
            case 6:
                controlStats(layoutStats, 1, 1, 10);
                layoutContent.addView(layoutStats);
                break;
            case 9:
                controlResult(layoutResult, 1, 1, 10);
                layoutContent.addView(layoutResult);
                break;
            case 13:
                controlQuotes(layoutQuotes);
                layoutContent.addView(layoutQuotes);
                break;
            case 17:
                controlStats(layoutStats, 2, 11, 20);
                layoutContent.addView(layoutStats);
                break;
            case 10:
                controlResult(layoutResult, 2, 11, 20);
                layoutContent.addView(layoutResult);
                break;
            case 11:
                controlClose(layoutClose1, 1);
                layoutContent.addView(layoutClose1);
                break;
            case 12:
                controlClose(layoutClose2, 2);
                layoutContent.addView(layoutClose2);
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
        getMenuInflater().inflate(R.menu.menu_life_foundation_spirit, menu);
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
        else{    if (MainActivity.currentActivity == 0 && MainActivity.activeActivity == 0) {
                super.onBackPressed();
            }
        }

    }

    //endregion
}