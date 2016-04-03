package com.blanke.solebook.utils;

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
}
