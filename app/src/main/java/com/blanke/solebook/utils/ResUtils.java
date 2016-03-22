package com.blanke.solebook.utils;

import android.content.Context;

/**
 * Created by Blanke on 16-3-22.
 */
public class ResUtils {
    public static String getResString(Context context, int resId) {
        return context.getResources().getString(resId);
    }
}
