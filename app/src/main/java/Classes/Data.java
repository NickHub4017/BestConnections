package Classes;

import android.text.SpannableStringBuilder;

public class Data {
    public boolean mIsTitle;
    public String mTitleText;
    public String mRowHead;
    public SpannableStringBuilder mRowText;

    public Data(boolean isTitle, String titleText, String rowHead, SpannableStringBuilder rowText) {
        this.mIsTitle = isTitle;
        this.mTitleText = titleText;
        this.mRowHead = rowHead;
        this.mRowText = rowText;
    }
}
