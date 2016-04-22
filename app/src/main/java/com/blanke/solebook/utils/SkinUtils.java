package com.blanke.solebook.utils;

import android.content.Context;

import com.blanke.solebook.constants.Constants;
import com.zhy.changeskin.SkinManager;

import org.simple.eventbus.EventBus;

/**
 * Created by blanke on 16-4-3.
 */
public class SkinUtils {
    public static void toggleTheme() {
        SkinManager skinManager = SkinManager.getInstance();
        if (skinManager.needChangeSkin()) {
            skinManager.removeAnySkin();
        } else {
            skinManager.changeSkin(Constants.THEME_NIGHT);
        }
        EventBus.getDefault().post(new Object(), Constants.EVENT_THEME_CHANGE);
    }

    public static int getColor(String colorName) {
        SkinManager skinManager = SkinManager.getInstance();
        return skinManager.getResourceManager().getColor(colorName);
    }

    public static int getLoadProgressColor() {
        return getColor(Constants.RES_COLOR_LOAD_PROGRESS);
    }

    public static int getStatusBarColor() {
        return getColor(Constants.RES_COLOR_STATUSBAR);
    }

    public static int getTextColor() {
        return getColor(Constants.RES_COLOR_TEXT);
    }

    public static int getTextBackgroundColor() {
        return getColor(Constants.RES_COLOR_TEXT_BACKGROUND);
    }

    public static int getTextHeightColor() {
        return getColor(Constants.RES_COLOR_TEXT_HIGHT);
    }

    public static int getWindowColor() {
        return getColor(Constants.RES_COLOR_WINDOWS);
    }

    public static int getColorId(Context context, String colorName) {
        if (SkinManager.getInstance().needChangeSkin()) {
            colorName += "_" + Constants.THEME_NIGHT;
        }
        return context.getResources().getIdentifier(colorName, "color", context.getPackageName());
    }

    public static int getLoadProgressColorId(Context context) {
        return getColorId(context, Constants.RES_COLOR_LOAD_PROGRESS);
    }

    public static int getTextBackgroundColorId(Context context) {
        return getColorId(context, Constants.RES_COLOR_TEXT_BACKGROUND);
    }

    public static boolean isNight() {
        return SkinManager.getInstance().needChangeSkin();
    }
}
