package com.blanke.solebook.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by blanke on 16-2-20.
 */
public class ViewAnimationHelper {
    public static void start(View v, int animResId) {
        if (v != null) {
            v.clearAnimation();
            Animation shake = AnimationUtils.loadAnimation(v.getContext(), animResId);
            v.startAnimation(shake);
        }
    }
}
