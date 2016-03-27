package Classes;

import android.animation.FloatArrayEvaluator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.syncbridge.bestconnections.AboutC4LActivity;
import com.syncbridge.bestconnections.AboutIntroActivity;
import com.syncbridge.bestconnections.CoreBeliefsActivity;
import com.syncbridge.bestconnections.CoreBeliefsCloseActivity;
import com.syncbridge.bestconnections.CoreBelifeIntroActivity;
import com.syncbridge.bestconnections.CoreGoalsActivity;
import com.syncbridge.bestconnections.CoreGoalsCloseActivity;
import com.syncbridge.bestconnections.CoreGoalsIntro2Activity;
import com.syncbridge.bestconnections.CoreGoalsIntroActivity;
import com.syncbridge.bestconnections.CoreStatusActivity;
import com.syncbridge.bestconnections.CoreStatusClose1Activity;
import com.syncbridge.bestconnections.CoreStatusHappinessIntroActivity;
import com.syncbridge.bestconnections.CoreStatusHappinessQuizActivity;
import com.syncbridge.bestconnections.CoreStatusHappinessResultActivity;
import com.syncbridge.bestconnections.CoreStatusIntroActivity;
import com.syncbridge.bestconnections.CoreStatusMotivationIntroActivity;
import com.syncbridge.bestconnections.CoreStatusMotivationQuizActivity;
import com.syncbridge.bestconnections.CoreStatusMotivationResultActivity;
import com.syncbridge.bestconnections.CoreStatusSecurityIntroActivity;
import com.syncbridge.bestconnections.CoreStatusSecurityQuizActivity;
import com.syncbridge.bestconnections.CoreStatusSecurityResultActivity;
import com.syncbridge.bestconnections.DisconnectedActivity;
import com.syncbridge.bestconnections.JournalActivity;
import com.syncbridge.bestconnections.JournalIntroActivity;
import com.syncbridge.bestconnections.LifeFoundationActivity;
import com.syncbridge.bestconnections.MainActivity;
import com.syncbridge.bestconnections.R;
import com.syncbridge.bestconnections.ResourcesActivity;
import com.syncbridge.bestconnections.ResourcesClose1Activity;
import com.syncbridge.bestconnections.ResourcesClose2Activity;
import com.syncbridge.bestconnections.ResourcesIntroActivity;
import com.syncbridge.bestconnections.ResourcesQuizActivity;
import com.syncbridge.bestconnections.ResourcesResultActivity;
import com.syncbridge.bestconnections.UnconnectedActivity;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DBHelper.Quiz;
import DBHelper.QuizDataSource;
import DBHelper.Quote;
import DBHelper.QuoteDataSource;
import DBHelper.Section;
import DBHelper.SectionDataSource;

/**
 * Created by Pasan Eramusugoda on 4/3/2015.
 */
public class CommonFunctions extends Application{

    public static final String PREFERENCES_NAME = "BestConnectionsPreFile";
    public static final String SETTING_KEY_IS_FIRST_TIME = "is_first";
    public static final String SETTING_KEY_IS_CONTINUE = "is_continue";
    private static String sDefault = "";
    private static boolean bDefault = true;

    public static int getResultInt(Context m_context, String sectionId) {
        int result = 0;

        try {
            final QuizDataSource quizDataSource = new QuizDataSource(m_context);
            quizDataSource.open();

            for (Quiz q : quizDataSource.getAllEntries(sectionId)) {
                result += q.getValue();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int getResultInt(Context m_context, String sectionId, int itemsFrom, int itemsTo) {
        int result = 0;

        try {
            final QuizDataSource quizDataSource = new QuizDataSource(m_context);
            quizDataSource.open();

            if(itemsFrom == 0 && itemsTo == 0) {
                for (Quiz q : quizDataSource.getAllEntries(sectionId)) {
                    result += q.getValue();
                }
            } else {
                int intItem = 1;
                for (Quiz q : quizDataSource.getAllEntries(sectionId)) {
                    if(intItem >= itemsFrom && intItem <= itemsTo) {
                        result += q.getValue();
                    }
                    intItem++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getResultString(Context m_context, String sectionId) {
        int result = 0;

        try {
            final QuizDataSource quizDataSource = new QuizDataSource(m_context);
            quizDataSource.open();

            for (Quiz q : quizDataSource.getAllEntries(sectionId)) {
                result += q.getValue();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Integer.toString(result);
    }

    public static String getDateTime(String dateTimeFormat) {
        //"yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                dateTimeFormat, Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static int getResourceId(Context context, String pVariableName, String pResourcename, String pPackageName) {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void closePreviousActivities(int currentActivity) {
        for(int i = 1; i <= 10; i++) {
            if(i == currentActivity) {
                continue;
            } else {
                switch (i) {
                    case 1:
                        if(CoreBeliefsActivity.h != null)
                            CoreBeliefsActivity.h.sendEmptyMessage(0);
                        if(CoreBelifeIntroActivity.h != null)
                            CoreBelifeIntroActivity.h.sendEmptyMessage(0);
                        if(CoreBeliefsCloseActivity.h != null)
                            CoreBeliefsCloseActivity.h.sendEmptyMessage(0);
                        break;
                    case 2:
                        if(CoreGoalsActivity.h != null)
                            CoreGoalsActivity.h.sendEmptyMessage(0);
                        if(CoreGoalsIntro2Activity.h != null)
                            CoreGoalsIntro2Activity.h.sendEmptyMessage(0);
                        if(CoreGoalsIntroActivity.h != null)
                            CoreGoalsIntroActivity.h.sendEmptyMessage(0);
                        if(CoreGoalsCloseActivity.h != null)
                            CoreGoalsCloseActivity.h.sendEmptyMessage(0);
                        break;
                    case 3:
                        if(CoreStatusActivity.h != null)
                            CoreStatusActivity.h.sendEmptyMessage(0);
                        if(CoreStatusIntroActivity.h != null)
                            CoreStatusIntroActivity.h.sendEmptyMessage(0);
                        if(CoreStatusHappinessIntroActivity.h != null)
                            CoreStatusHappinessIntroActivity.h.sendEmptyMessage(0);
                        if(CoreStatusHappinessQuizActivity.h != null)
                            CoreStatusHappinessQuizActivity.h.sendEmptyMessage(0);
                        if(CoreStatusHappinessResultActivity.h != null)
                            CoreStatusHappinessResultActivity.h.sendEmptyMessage(0);
                        if(CoreStatusSecurityIntroActivity.h != null)
                            CoreStatusSecurityIntroActivity.h.sendEmptyMessage(0);
                        if(CoreStatusSecurityQuizActivity.h != null)
                            CoreStatusSecurityQuizActivity.h.sendEmptyMessage(0);
                        if(CoreStatusSecurityResultActivity.h != null)
                            CoreStatusSecurityResultActivity.h.sendEmptyMessage(0);
                        if(CoreStatusMotivationIntroActivity.h != null)
                            CoreStatusMotivationIntroActivity.h.sendEmptyMessage(0);
                        if(CoreStatusMotivationQuizActivity.h != null)
                            CoreStatusMotivationQuizActivity.h.sendEmptyMessage(0);
                        if(CoreStatusMotivationResultActivity.h != null)
                            CoreStatusMotivationResultActivity.h.sendEmptyMessage(0);
                        break;
                    case 4:
                        if(LifeFoundationActivity.h != null)
                            LifeFoundationActivity.h.sendEmptyMessage(0);
                        break;
                    case 5:
                        if(LifeFoundationActivity.h != null)
                            LifeFoundationActivity.h.sendEmptyMessage(0);
                        break;
                    case 6:
                        if(LifeFoundationActivity.h != null)
                            LifeFoundationActivity.h.sendEmptyMessage(0);
                        break;
                    case 7:
                        if(UnconnectedActivity.h != null)
                            UnconnectedActivity.h.sendEmptyMessage(0);
                        break;
                    case 8:
                        if(DisconnectedActivity.h != null)
                            DisconnectedActivity.h.sendEmptyMessage(0);
                        break;
                    case 9:
                        if(JournalActivity.h != null)
                            JournalActivity.h.sendEmptyMessage(0);
                        if(JournalIntroActivity.h != null)
                            JournalIntroActivity.h.sendEmptyMessage(0);
                        break;
                    case 10:
                        if(ResourcesIntroActivity.h != null)
                            ResourcesIntroActivity.h.sendEmptyMessage(0);
                        if(ResourcesQuizActivity.h != null)
                            ResourcesQuizActivity.h.sendEmptyMessage(0);
                        if(ResourcesResultActivity.h != null)
                            ResourcesResultActivity.h.sendEmptyMessage(0);
                        if(ResourcesActivity.h != null)
                            ResourcesActivity.h.sendEmptyMessage(0);
                        if(ResourcesClose1Activity.h != null)
                            ResourcesClose1Activity.h.sendEmptyMessage(0);
                        if(ResourcesClose2Activity.h != null)
                            ResourcesClose2Activity.h.sendEmptyMessage(0);
                        break;
                    case 11:
                        if(AboutIntroActivity.h != null)
                            AboutIntroActivity.h.sendEmptyMessage(0);
                        if(AboutC4LActivity.h != null)
                            AboutC4LActivity.h.sendEmptyMessage(0);
                        break;
                }
            }
        }
    }

    //region last session

    public static int getLastSessionQuote(Context context, String sectionId, String lastQuoteId) {
        try{
            QuoteDataSource quoteDataSource = new QuoteDataSource(context);
            quoteDataSource.open();

            int index = 0;
            for(Quote quote : quoteDataSource.getAllEntries(sectionId)) {
                if(quote.getUnique_id().equals(lastQuoteId)) {
                    break;
                }
                index++;
            }

            return index;
        } catch (SQLException ex) {
            Log.e(MainActivity.TAG, ex.getMessage(), ex);
            return 0;
        }
    }

    public static void updateLastSessionQuote(Context context, String sectionId, String lastQuoteId) {
        try{
            SectionDataSource sectionDataSource = new SectionDataSource(context);
            sectionDataSource.open();

            Section section = new Section();
            section.setSection_id(sectionId);
            section.setLast_quote(lastQuoteId);

            sectionDataSource.updateEntry(section);
        } catch (SQLException ex) {
            Log.e(MainActivity.TAG, ex.getMessage(), ex);
        }
    }

    //endregion

    public static boolean isSectionFinished(Context context, String sectionId) {
        try{
            SectionDataSource sectionDataSource = new SectionDataSource(context);
            sectionDataSource.open();

            Section section = sectionDataSource.getEntry(sectionId);

            sectionDataSource.close();

            return section.getSection_finished();

        } catch (SQLException ex) {
            Log.e(MainActivity.TAG, ex.getMessage(), ex);
            return false;
        }
    }

    public static void updateSectionFinished(Context context, String sectionId, boolean isFinished) {
        try{
            SectionDataSource sectionDataSource = new SectionDataSource(context);
            sectionDataSource.open();

//            Section section = new Section();
//            section.setSection_finished(isFinished);
//            section.setSection_id(sectionId);

            sectionDataSource.updateEntry(sectionId, isFinished);
            sectionDataSource.close();
        } catch (SQLException ex) {
            Log.e(MainActivity.TAG, ex.getMessage(), ex);
        }
    }

    public static boolean handleSpeakerIcon(final boolean conMusic, final View root, final Context m_context) {

        final boolean[] tiktik = {false};
        if (!conMusic) {
            ((ImageView) root.findViewById(R.id.imageViewSpeaker)).setImageResource(R.mipmap.ic_mute);
        } else {
            ((ImageView) root.findViewById(R.id.imageViewSpeaker)).setImageResource(R.mipmap.ic_unmute);
        }

        ((ImageView) root.findViewById(R.id.imageViewSpeaker)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mDrawerLayout.closeDrawers();
                if (conMusic) {
                    ((ImageView) root.findViewById(R.id.imageViewSpeaker)).setImageResource(R.mipmap.ic_mute);
                    ((ImageView) root.findViewById(R.id.imageViewSpeaker)).requestLayout();
                    MusicManager.pause();
                    tiktik[0] = false;
                } else {
                    ((ImageView) root.findViewById(R.id.imageViewSpeaker)).setImageResource(R.mipmap.ic_unmute);
                    ((ImageView) root.findViewById(R.id.imageViewSpeaker)).requestLayout();
                    MusicManager.start(m_context, MainActivity.getMusic(), true);
                    tiktik[0] = true;
                }
            }
        });

        return tiktik[0];
    }

    //region save settings

    public static String getSettingString(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_NAME, 0);
        return settings.getString(key, sDefault);
    }

    public static boolean getSettingBoolean(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_NAME, 0);
        return settings.getBoolean(key, bDefault);
    }

    public static void saveSettingBoolean(Context context, String key, boolean value) {
        try {
            SharedPreferences settings = context.getSharedPreferences(PREFERENCES_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();

            editor.putBoolean(key, value); //

            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //endregion

    public static void updateLeftView(Context context, String sectionId, int leftView) {
        try{
            SectionDataSource sectionDataSource = new SectionDataSource(context);
            sectionDataSource.open();

            sectionDataSource.updateEntry(sectionId, leftView);
        } catch (SQLException ex) {
            Log.e(MainActivity.TAG, ex.getMessage(), ex);
        }
    }

    // 1 - intro1
    // 2 - intro2
    // 3 - intro3
    // 4 - intro4
    // 5 - quiz
    // 6 - result
    // 7 - intro5
    // 8 - intro6
    // 9 - quote
    // 10 - close1
    // 11 - close2
    // 12 - close3
    // 13 - quiz2
    // 14 - quiz3
    // 15 - quiz4
    // 16 - quiz5
    // 17 - result2
    // 18 - result3
    // 19 - result4
    // 20 - result5
    // 21 - specialPage1
    // 22 - specialPage2

    public static Class<?> getActivityClassByLeftView(Context context, String sectionId) {
        Class<?> cls = null;

        try {
            SectionDataSource sectionDataSource = new SectionDataSource(context);
            sectionDataSource.open();

            Section section = sectionDataSource.getEntry(sectionId);

            if(section != null) {

                if(section.getLeft_view() != -1) {
                    cls = getActivity(section.getSection_id(), section.getLeft_view());
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cls;
    }

    public static int getLeftView(Context context, String sectionId) {
        int view = -1;

        try {
            SectionDataSource sectionDataSource = new SectionDataSource(context);
            sectionDataSource.open();

            Section section = sectionDataSource.getEntry(sectionId);

            if(section != null) {

                if(section.getLeft_view() != -1) {
                    view = section.getLeft_view();
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return view;
    }

    // 1 - intro1
    // 2 - intro2
    // 3 - intro3
    // 4 - intro4
    // 5 - quiz
    // 6 - result
    // 7 - intro5
    // 8 - intro6
    // 9 - quote
    // 10 - close1
    // 11 - close2
    // 12 - close3
    // 13 - quiz2
    // 14 - quiz3
    // 15 - quiz4
    // 16 - quiz5
    // 17 - result2
    // 18 - result3
    // 19 - result4
    // 20 - result5
    // 21 - specialPage1
    // 22 - specialPage2

    private static Class<?> getActivity(String section, int leftView) {
        Class<?> cls = null;

        if(section.equals("CB")) {

            switch (leftView) {
                case 1:
                    cls = CoreBelifeIntroActivity.class;
                    break;

                case 9:
                    cls = CoreBeliefsActivity.class;
                    break;

                case 10:
                    cls = CoreBeliefsCloseActivity.class;
                    break;
            }

        } else if(section.equals("CG")) {

            switch (leftView) {
                case 1:
                    cls = CoreGoalsIntroActivity.class;
                    break;

                case 2:
                    cls = CoreGoalsIntro2Activity.class;
                    break;

                case 9:
                    cls = CoreGoalsActivity.class;
                    break;

                case 10:
                    cls = CoreGoalsCloseActivity.class;
                    break;
            }

        } else if(section.equals("CS")) {

            switch (leftView) {
                case 1:
                    cls = CoreStatusIntroActivity.class;
                    break;

                case 10:
                    cls = CoreStatusClose1Activity.class;
                    break;
            }

        } else if(section.equals("CSS")) {

            switch (leftView) {
                case 1:
                    cls = CoreStatusSecurityIntroActivity.class;
                    break;

                case 5:
                    cls = CoreStatusSecurityQuizActivity.class;
                    break;

                case 6:
                    cls = CoreStatusSecurityResultActivity.class;
                    break;

                case 9:
                    cls = CoreStatusActivity.class;
                    break;
            }

        } else if(section.equals("CSH")) {

            switch (leftView) {
                case 1:
                    cls = CoreStatusHappinessIntroActivity.class;
                    break;

                case 5:
                    cls = CoreStatusHappinessQuizActivity.class;
                    break;

                case 6:
                    cls = CoreStatusHappinessResultActivity.class;
                    break;

                case 9:
                    cls = CoreStatusActivity.class;
                    break;
            }

        } else if(section.equals("CSM")) {

            switch (leftView) {
                case 1:
                    cls = CoreStatusMotivationIntroActivity.class;
                    break;

                case 5:
                    cls = CoreStatusMotivationQuizActivity.class;
                    break;

                case 6:
                    cls = CoreStatusMotivationResultActivity.class;
                    break;

                case 9:
                    cls = CoreStatusActivity.class;
                    break;
            }

        } else if(section.equals("LFBO")) {

        } else if(section.equals("LFSO")) {

        } else if(section.equals("LFSP")) {

        } else if(section.equals("UC")) {

        } else if(section.equals("DC")) {

        } else if(section.equals("RS")) {

            switch (leftView) {
                case 1:
                    cls = ResourcesIntroActivity.class;
                    break;

                case 5:
                    cls = ResourcesQuizActivity.class;
                    break;

                case 6:
                    cls = ResourcesResultActivity.class;
                    break;

                case 9:
                    cls = ResourcesActivity.class;
                    break;

                case 10:
                    cls = ResourcesClose1Activity.class;
                    break;

                case 11:
                    cls = ResourcesClose2Activity.class;
                    break;
            }

        }

        return cls;
    }

    //region show result description dialog
    private static ResultDescriptionAdapter mAdapter;

    public static void showDialog(final Context mContext,
                                   int mDialogType, String mDialogTitle) {
        try {
//            DialogFragment newFragment = CustomDialog.newInstance(mContext, mDialogType, mDialogTitle);
//            newFragment.show(mFragmentManager, "dialog");

            Dialog dialog = new Dialog(mContext);
            dialog.setTitle(mDialogTitle);
            dialog.setCancelable(true);

            switch (mDialogType) {
                case 1:
                    dialog.setContentView(R.layout.layout_life_foundation_body_result_description);

                    RecyclerView mRecyclerView = (RecyclerView) dialog.findViewById(R.id.list);

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                    mRecyclerView.setLayoutManager(mLayoutManager);

                    mAdapter = new ResultDescriptionAdapter();
                    mRecyclerView.setAdapter(mAdapter);

                    addItems(mContext);

                    break;
                case 2:
                    dialog.setContentView(R.layout.layout_life_foundation_soul_result_description);

                    String text = mContext.getResources().getString(R.string.string_lfso_result_des);

                    SpannableStringBuilder ssb = new SpannableStringBuilder(text);

                    Pattern pattern = Pattern.compile("(https?://([-\\w\\.]+)+(:\\d+)?(/([\\w/_\\.]*(\\?\\S+)?)?)?)");
                    for (final MatchResult match : allMatches(pattern, text)) {

                        int start = match.start();
                        int end = match.end();

                        URLSpan urlSpan = new URLSpan(match.group()) {
                            @Override
                            public void onClick(View widget) {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(match.group()));
                                mContext.startActivity(i);
                            }
                        };
                        ssb.setSpan(urlSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    }

                    ((TextView) dialog.findViewById(R.id.text)).setText(ssb, TextView.BufferType.SPANNABLE);

                    break;
            }

            dialog.show();


        } catch (IllegalStateException ills) {
            Toast.makeText(mContext, ills.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private static List<Data> mData = new ArrayList<>();
    private static void addItems(final Context mContext) {
        Data dataTitle = new Data(true, "SELF IMPROVEMENT!", null, null);
        mData.add(dataTitle);
        mAdapter.addItem(dataTitle);

        for (String item : mContext.getResources().getStringArray(R.array.string_lfbo_result_des_items)) {
            String head = item.split("[|]")[0];
            String text = item.split("[|]")[1];

            SpannableStringBuilder ssb = new SpannableStringBuilder(text);
            //URLSpan[] spans = ssb.getSpans(0, ssb.length(), URLSpan.class);
            List<URLSpan> spans = new ArrayList<>();

            Pattern pattern = Pattern.compile(mContext.getResources().getString(R.string.HTTP_URL_PATTERN));
            for (final MatchResult match : allMatches(pattern, text)) {

                int start = match.start();
                int end = match.end();

                URLSpan urlSpan = new URLSpan(match.group()) {
                    @Override
                    public void onClick(View widget) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(match.group()));
                        mContext.startActivity(i);
                    }
                };
                ssb.setSpan(urlSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                spans.add(urlSpan);

            }

            mData.add(new Data(false, null, head, ssb));

            //mAdapter.addItem(mData.size() - 1, new Data(false, null, head, ssb));
            mAdapter.addItem(new Data(false, null, head, ssb));
        }
    }

    //endregion

    public static SpannableStringBuilder getSpannableStringBuilder(final Context mContext, String text) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);

        Pattern pattern = Pattern.compile(mContext.getResources().getString(R.string.HTTP_URL_PATTERN));
        for (final MatchResult match : allMatches(pattern, text)) {

            int start = match.start();
            int end = match.end();

            URLSpan urlSpan = new URLSpan(match.group()) {
                @Override
                public void onClick(View widget) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(match.group()));
                    mContext.startActivity(i);
                }
            };
            ssb.setSpan(urlSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return ssb;
    }

    private static Iterable<MatchResult> allMatches(
            final Pattern p, final CharSequence input) {
        return new Iterable<MatchResult>() {
            public Iterator<MatchResult> iterator() {
                return new Iterator<MatchResult>() {
                    // Use a matcher internally.
                    final Matcher matcher = p.matcher(input);
                    // Keep a match around that supports any interleaving of hasNext/next calls.
                    MatchResult pending;

                    public boolean hasNext() {
                        // Lazily fill pending, and avoid calling find() multiple times if the
                        // clients call hasNext() repeatedly before sampling via next().
                        if (pending == null && matcher.find()) {
                            pending = matcher.toMatchResult();
                        }
                        return pending != null;
                    }

                    public MatchResult next() {
                        // Fill pending if necessary (as when clients call next() without
                        // checking hasNext()), throw if not possible.
                        if (!hasNext()) { throw new NoSuchElementException(); }
                        // Consume pending so next call to hasNext() does a find().
                        MatchResult next = pending;
                        pending = null;
                        return next;
                    }

                    /** Required to satisfy the interface, but unsupported. */
                    public void remove() { throw new UnsupportedOperationException(); }
                };
            }
        };
    }

    public static boolean contains(final int[] array, final int key) {
        for (int item : array) {
            if(item == key)
                return true;
        }
        return false;
    }

    public static String getMyersBriggsString(Context mContext, String result) {
        if (result.equals("INFP")) {
            return mContext.getResources().getString(R.string.INFP);
        } else if (result.equals("INFJ")) {
            return mContext.getResources().getString(R.string.INFJ);
        } else if (result.equals("INTJ")) {
            return mContext.getResources().getString(R.string.INTJ);
        } else if (result.equals("INTP")) {
            return mContext.getResources().getString(R.string.INTP);
        } else if (result.equals("ISFJ")) {
            return mContext.getResources().getString(R.string.ISFJ);
        } else if (result.equals("ISFP")) {
            return mContext.getResources().getString(R.string.ISFP);
        } else if (result.equals("ISTJ")) {
            return mContext.getResources().getString(R.string.ISTJ);
        } else if (result.equals("ISTP")) {
            return mContext.getResources().getString(R.string.ISTP);
        } else if (result.equals("ENFJ")) {
            return mContext.getResources().getString(R.string.ENFJ);
        } else if (result.equals("ENFP")) {
            return mContext.getResources().getString(R.string.ENFP);
        } else if (result.equals("ENTJ")) {
            return mContext.getResources().getString(R.string.ENTJ);
        } else if (result.equals("ENTP")) {
            return mContext.getResources().getString(R.string.ENTP);
        } else if (result.equals("ESFJ")) {
            return mContext.getResources().getString(R.string.ESFJ);
        } else if (result.equals("ESFP")) {
            return mContext.getResources().getString(R.string.ESFP);
        } else if (result.equals("ESTJ")) {
            return mContext.getResources().getString(R.string.ESTJ);
        } else if (result.equals("ESTP")) {
            return mContext.getResources().getString(R.string.ESTP);
        } else {
            return "No result to show.";
        }
    }

    //region fonts

    public static Typeface getTypeFace(Context context, int fontType) {
        Typeface typeface = null;

        switch (fontType) {
            case 1:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Abadi-MT-Condensed-Light-Regular.ttf");
                break;
            case 2:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Abadi-MT-Condensed-Light.ttf");
                break;
            case 3:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Futura-Condensed-Medium.ttf");
                break;
            case 4:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Futura-Medium.ttf");
                break;
            case 5:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Futura-Medium-Italic.ttf");
                break;
            case 6:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Abadi-MT-Condensed-Extra-Bold.ttf");
                break;
            case 7:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Futura-Condensed-Medium-Italic.ttf");
                break;
            case 8:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Futura-Condensed-Medium-Bold.ttf");
                break;
            case 9:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Fonesia-Bold.ttf");
                break;
            case 10:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Fonesia-Light.ttf");
                break;
            case 11:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Fonesia-Regular.ttf");
                break;
        }
        return typeface;
    }

    //endregion
}
