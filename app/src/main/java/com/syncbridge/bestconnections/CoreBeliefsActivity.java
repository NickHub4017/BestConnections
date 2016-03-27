package com.syncbridge.bestconnections;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;

import java.sql.SQLException;
import java.util.List;

import Classes.CommonFunctions;
import Classes.MusicManager;
import DBHelper.Comment;
import DBHelper.CommentDataSource;
import DBHelper.Option;
import DBHelper.OptionDataSource;
import DBHelper.Quote;
import DBHelper.QuoteDataSource;


public class CoreBeliefsActivity extends MainActivity {

    MediaPlayer mediaPlayer;
    private boolean isPlaying = true;

    //layout1
    private View layoutOption, layoutQuestion;
    private FrameLayout frameLayout;
    private ScrollView scrollView;

    private RelativeLayout relativeLayout1;
    private TextView textViewQuote1;
    private TextView textViewSourceNDate1;
    private CircularImageView imageViewCartoon1;
    private ImageView imageViewFirst1;
    private ImageView imageViewPrevious1;
    private ImageView imageViewNext1;
    private ImageView imageViewLast1;

    public Context m_context;
    public List<Quote> quoteList;
    public QuoteDataSource quoteDataSource;

    public String previousIdentity = "";
    public String currentIdentity = "";
    public String nextIdentity = "";

    public int previousPosition = 0;
    public int currentPosition = 0;
    public int nextPosition = 0;

    public static Handler h;
    public String lastQuoteId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_beliefs);

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

        SECTION_ID = "CB";

        if(getIntent().getExtras().getString("LAST_QUOTE_ID") != null) {
            lastQuoteId = getIntent().getExtras().getString("LAST_QUOTE_ID");
        }
        m_context = this.getApplicationContext();

        activeActivity = 1;
        set(m_context);

        CommonFunctions.updateLeftView(m_context, SECTION_ID, 9);

        //layout1
        frameLayout = (FrameLayout) findViewById(R.id.frameLayoutUser);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayoutCoreBelief);
        textViewQuote1 = (TextView) findViewById(R.id.textViewQuote);
        textViewSourceNDate1 = (TextView) findViewById(R.id.textViewSourceNDate);
        imageViewCartoon1 = (CircularImageView) findViewById(R.id.imageViewCartoon);
        imageViewFirst1 = (ImageView) findViewById(R.id.imageButtonFirst);
        imageViewPrevious1 = (ImageView) findViewById(R.id.imageButtonPrevious);
        imageViewNext1 = (ImageView) findViewById(R.id.imageButtonNext);
        imageViewLast1 = (ImageView) findViewById(R.id.imageButtonLast);

        quoteDataSource = new QuoteDataSource(m_context);

        try {
            quoteDataSource.open();
            quoteList = quoteDataSource.getAllEntries("CB");

            if(lastQuoteId.isEmpty()) {
                loadDataToControl(0);
            } else {
                currentIdentity = lastQuoteId;
                loadDataToControl(5);
            }

        } catch (SQLException ex) {

        }

        imageViewFirst1.setOnClickListener(onClickFirst);
        imageViewPrevious1.setOnClickListener(onClickPrevious);
        imageViewNext1.setOnClickListener(onClickNext);
        imageViewLast1.setOnClickListener(onClickLast);

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

    //region functions

    //region button clicks

    View.OnClickListener onClickFirst = new View.OnClickListener() {
        public void onClick(View v) {
            loadDataToControl(1);
            scrollView.fullScroll(ScrollView.FOCUS_UP);
        }
    };

    View.OnClickListener onClickPrevious = new View.OnClickListener() {
        public void onClick(View v) {
            loadDataToControl(2);
            scrollView.fullScroll(ScrollView.FOCUS_UP);
        }
    };

    View.OnClickListener onClickNext = new View.OnClickListener() {
        public void onClick(View v) {
            loadDataToControl(3);
            scrollView.fullScroll(ScrollView.FOCUS_UP);
        }
    };

    View.OnClickListener onClickLast = new View.OnClickListener() {
        public void onClick(View v) {
            loadDataToControl(4);
            scrollView.fullScroll(ScrollView.FOCUS_UP);
        }
    };

    //endregion

    private void loadDataToControl(int action) {
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        relativeLayout1.setVisibility(View.INVISIBLE);
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
                    Intent i = new Intent(CoreBeliefsActivity.this,CoreBeliefsCloseActivity.class);
                    i.putExtra("LAST_QUOTE_ID", currentIdentity);
                    startActivity(i);
                    //finish();
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
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

        relativeLayout1.setVisibility(View.VISIBLE);

        if(currentQuote != null) {
            //add last quote to section table
            CommonFunctions.updateLastSessionQuote(m_context, SECTION_ID, currentQuote.getUnique_id());

            //setting quote and source and date
            textViewQuote1.setText(currentQuote.getQuote());
            textViewSourceNDate1.setText(currentQuote.getPerson_source() + " " + currentQuote.getDate());
            //setting image
            if(!currentQuote.getImage().equals("Caricature")) {
                //Drawable drawable = getResources().getDrawable(CommonFunctions.getResId(currentCB.getImage_path(), this.getClass()));
                Drawable drawable =getResources().getDrawable(CommonFunctions.getResourceId(getApplicationContext(), currentQuote.getImage(), "mipmap", getPackageName()));
                imageViewCartoon1.setImageDrawable(drawable);
            } else {
                imageViewCartoon1.setImageDrawable(null);
            }
            //render question or option controls
            frameLayout.removeAllViews();
            if(currentQuote.getQuote_type().equals("O")) {
                frameLayout.addView(loadOptionControl(currentQuote));
            } else if(currentQuote.getQuote_type().equals("Q")) {

            }

            handleCommentControl(currentQuote);
        }
    }

    //endregion

    //region handle options control
    private View loadOptionControl(final Quote quote) {
        LayoutInflater inflater = getLayoutInflater();
        layoutOption = inflater.inflate(R.layout.layout_option_like_unlike, null);

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
                        option.setIs_like(true);
                        option.setIs_bullseye(false);

                        optionDataSource.updateEntry(option);

                        quote.setAnswered(true);
                        quoteDataSource.updateEntry(quote);

                        if(option.getIs_like()) {
                            imageViewLike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_like_selected));
                            imageViewUnlike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_unlike_not_selected));
                        } else {
                            imageViewLike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_like_not_selected));
                            imageViewUnlike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_unlike_selected));
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
                                true,
                                false
                        );

                        quote.setAnswered(true);
                        quoteDataSource.updateEntry(quote);

                        if(newOption.getIs_like()) {
                            imageViewLike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_like_selected));
                            imageViewUnlike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_unlike_not_selected));
                        } else {
                            imageViewLike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_like_not_selected));
                            imageViewUnlike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_unlike_selected));
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                loadDataToControl(3);
            }
        });


        imageViewUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quote.getAnswered()) {
                    try {
                        OptionDataSource optionDataSource = new OptionDataSource(m_context);
                        optionDataSource.open();

                        Option option = optionDataSource.getEntry(quote.getUnique_id());
                        option.setIs_like(false);
                        option.setIs_bullseye(false);

                        optionDataSource.updateEntry(option);

                        quote.setAnswered(true);
                        quoteDataSource.updateEntry(quote);

                        if(option.getIs_like()) {
                            imageViewLike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_like_selected));
                            imageViewUnlike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_unlike_not_selected));
                        } else {
                            imageViewLike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_like_not_selected));
                            imageViewUnlike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_unlike_selected));
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

                        if(newOption.getIs_like()) {
                            imageViewLike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_like_selected));
                            imageViewUnlike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_unlike_not_selected));
                        } else {
                            imageViewLike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_like_not_selected));
                            imageViewUnlike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_unlike_selected));
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                loadDataToControl(3);
            }
        });
        //endregion

        if(quote.getAnswered()) {
            try {
                OptionDataSource optionDataSource = new OptionDataSource(m_context);
                optionDataSource.open();

                Option option = optionDataSource.getEntry(quote.getUnique_id());

                if(option.getIs_like()) {
                    imageViewLike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_like_selected));
                    imageViewUnlike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_unlike_not_selected));
                } else {
                    imageViewLike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_like_not_selected));
                    imageViewUnlike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_unlike_selected));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            imageViewLike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_like));
            imageViewUnlike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cb_unlike));
        }

        return layoutOption;
    }
    //endregion

    //region handle question control
    //endregion

    //region handle comment control
    private void handleCommentControl(final Quote quote) {
        ImageView imageViewAddToJournal = (ImageView) findViewById(R.id.imageViewAddToJournal);
        imageViewAddToJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(CoreBeliefsActivity.this);
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
                                CommentDataSource commentDataSource = new CommentDataSource(m_context);
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
                    CommentDataSource commentDataSource = new CommentDataSource(m_context);
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
            if(currentPosition==0){
                this.finish();
            }
            else {
                loadDataToControl(2);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }

        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            setResult(RESULT_OK);
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onResume() {

        activeActivity = 1;
        currentActivity = 1;
        CommonFunctions.updateLeftView(m_context, SECTION_ID, 9);

        super.onResume();
    }
}
