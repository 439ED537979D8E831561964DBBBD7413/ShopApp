package com.yj.shopapp.util;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LK on 2017/9/26.
 */

public class InputFilterMinMax implements InputFilter {
    private int min, max;
    Pattern pattern;

    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (!source.equals(" ")) {
            pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(source);
            if (isNum.matches()) {
                int input = Integer.parseInt(dest.toString().trim() + source.toString().trim());
                if (isInRange(min, max, input)) {
                    return source.toString().trim();
                }
            }
        }
        return "";
    }

    private boolean isInRange(int min, int max, int input) {

        if (max != 0) {
            return input <= max ? true : false;
        }
        return true;

    }
}
