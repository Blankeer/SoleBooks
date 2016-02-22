package com.blanke.solebook.utils;

import android.widget.TextView;

/**
 * Created by blanke on 16-2-21.
 */
public class TextViewUtils {
    public static String getText(TextView tv) {
        return tv.getText().toString().trim();
    }
}
