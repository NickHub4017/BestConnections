package Classes;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.syncbridge.bestconnections.JournalIntroActivity;
import com.syncbridge.bestconnections.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Pasan Eramusugoda on 7/15/2015.
 */
public class CustomDialog extends DialogFragment {

    private static Context mContext;
    private static int mLayoutType = 0;
    private static List<Data> mData = new ArrayList<>();
    private static ResultDescriptionAdapter mAdapter;

    private static String mDialogTitle;

    /**
     * New instance
     * @param layoutType 1 = Body Result Description
     * @return
     */
    public static CustomDialog newInstance(Context context, int layoutType, String dialogTitle) {
        mLayoutType = layoutType;
        mContext = context;
        mDialogTitle = dialogTitle;
        return new CustomDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(mDialogTitle != null) {
            getDialog().setTitle(mDialogTitle);
        }

        View v = null;

        switch (mLayoutType) {
            case 1:
                v = inflater.inflate(R.layout.layout_life_foundation_body_result_description, container, false);

                RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.list);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter = new ResultDescriptionAdapter();
                mRecyclerView.setAdapter(mAdapter);

                addItems();

                break;
            case 2:

                v = inflater.inflate(R.layout.layout_life_foundation_soul_result_description, container, false);

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
                            startActivity(i);
                        }
                    };
                    ssb.setSpan(urlSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }

                ((TextView)v.findViewById(R.id.text)).setText(ssb, TextView.BufferType.SPANNABLE);

                break;
        }

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        //getTargetFragment().onActivityResult(getTargetRequestCode(), 0, null);
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);

        //outState.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) mData);
    }

    //region functions

    public int show(FragmentTransaction transaction, String tag) {
        return show(transaction, tag, false);
    }

    public int show(FragmentTransaction transaction, String tag, boolean allowStateLoss) {
        transaction.add(this, tag);
        return allowStateLoss ? transaction.commitAllowingStateLoss() : transaction.commit();
    }

    private void addItems() {
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
                        startActivity(i);
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

    public static Iterable<MatchResult> allMatches(
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

    //endregion

}
