package com.yj.shopapp.view.CountDown;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

/**
 * Created by LK on 2018/3/23.
 *
 * @author LK
 */

public class JDCountDownTimer extends MikyouCountDownTimer {
    private SpannableString mSpan;
    private Context mContext;
    private int mDrawableId;

    public JDCountDownTimer(Context mContext, long mGapTime, String mTimePattern, int mDrawableId) {
        super(mContext, mGapTime, mTimePattern, mDrawableId);
        this.mContext = mContext;
        this.mDrawableId = mDrawableId;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initSpanData(String timeStr) {
        super.initSpanData(timeStr);
        for (int i = 0; i < numbers.length; i++) {
            MikyouBackgroundSpan mBackSpan = new MikyouBackgroundSpan(mContext.getDrawable(mDrawableId), ImageSpan.ALIGN_BOTTOM);
            initBackSpanStyle(mBackSpan);
            mBackSpanList.add(mBackSpan);
        }
        for (int i = 0; i < nonNumbers.length; i++) {
            ForegroundColorSpan mGapSpan = new ForegroundColorSpan(mGapSpanColor);
            mTextColorSpanList.add(mGapSpan);
        }
    }

    @Override
    public void setBackgroundSpan(String timeStr) {
        super.setBackgroundSpan(timeStr);
        int mGapLen = 1;
        mSpan = new SpannableString(timeStr);
        for (int i = 0; i < mBackSpanList.size(); i++) {
            int start = i * numbers[i].length() + i * mGapLen;
            int end = start + numbers[i].length();
            TimerUtils.setContentSpan(mSpan, mBackSpanList.get(i), start, end);

            if (i < mTextColorSpanList.size()) {//这里为了就是防止12:36:27这种样式，这种样式间隔只有２个所以需要做判断，防止数组越界
                TimerUtils.setContentSpan(mSpan, mTextColorSpanList.get(i), end, end + mGapLen);
            }
        }
        mDateTv.setMovementMethod(LinkMovementMethod.getInstance());//此方法很重要需要调用，否则绘制出来的倒计时就是重叠的样式
        mDateTv.setText(mSpan);
    }
}
