package com.blanke.solebook.utils;

import android.content.Context;

/**
 * Created by blanke on 16-2-4.
 */
public class AppConfigUtils {

    public static int getScreenWidthDp(Context context) {
        return context.getResources().getConfiguration().screenWidthDp;
    }

    public static int getScreenHeighthDp(Context context) {
        return context.getResources().getConfiguration().screenHeightDp;
    }
}