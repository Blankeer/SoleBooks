package com.blanke.solebook.utils;


import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.blanke.solebook.constants.Constants;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by Blanke on 16-3-31.
 */
public class AnimUtils {

    private static ObjectAnimator getObjectAnim(View v, String field, float i, float j, long time) {
        return ObjectAnimator.ofFloat(v, field, i, j).setDuration(time);
    }

    private static ObjectAnimator getScaleXAnim(View v, float i, float j, long time) {
        return getObjectAnim(v, "scaleX", i, j, time);
    }

    private static ObjectAnimator getScaleYAnim(View v, float i, float j, long time) {
        return getObjectAnim(v, "scaleY", i, j, time);
    }

    private static ObjectAnimator getTranslationYAnim(View v, float i, float j, long time) {
        return getObjectAnim(v, "translationY", i, j, time);
    }

    public static void fabHide(View v) {
        if (v.getVisibility() == View.GONE) {
            return;
        }
        v.clearAnimation();
        AnimatorSet set = new AnimatorSet();
        float scaleTemp = 0.3F;
        ObjectAnimator anim1, anim2, anim3, anim4, anim5;
        anim1 = getScaleXAnim(v, 1F, scaleTemp, Constants.ANIM_DURATION_MIND);
        anim2 = getScaleYAnim(v, 1F, scaleTemp, Constants.ANIM_DURATION_MIND);
        anim3 = getTranslationYAnim(v, 0, 100, Constants.ANIM_DURATION_MIND);
        anim4 = getScaleXAnim(v, scaleTemp, 0F, Constants.ANIM_DURATION_MIND);
        anim5 = getScaleYAnim(v, scaleTemp, 0F, Constants.ANIM_DURATION_MIND);
        set.play(anim1).with(anim2);
        set.play(anim3).after(Constants.ANIM_DURATION_MIND).after(anim1);
        set.play(anim4).with(anim5);
        set.play(anim4).after(anim3);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.GONE);
            }
        });
        anim1.setInterpolator(new OvershootInterpolator(3.0F));
        anim2.setInterpolator(new OvershootInterpolator(3.0F));
        set.start();
    }

    public static void fabShow(View v) {
        if (v.getVisibility() == View.VISIBLE) {
            return;
        }
        v.clearAnimation();
        v.setVisibility(View.VISIBLE);
        AnimatorSet set = new AnimatorSet();
        float scaleTemp = 0.3F;
        ObjectAnimator anim1, anim2, anim3, anim4, anim5;
        anim1 = getScaleXAnim(v, scaleTemp, 1F, Constants.ANIM_DURATION_MIND);
        anim2 = getScaleYAnim(v, scaleTemp, 1F, Constants.ANIM_DURATION_MIND);
        anim3 = getTranslationYAnim(v, v.getTranslationY(), 0, Constants.ANIM_DURATION_MIND);
        anim4 = getScaleXAnim(v, 0F, scaleTemp, Constants.ANIM_DURATION_MIND);
        anim5 = getScaleYAnim(v, 0F, scaleTemp, Constants.ANIM_DURATION_MIND);
        set.play(anim4).with(anim5);
        set.play(anim3).after(anim4);
        set.play(anim1).with(anim2);
        set.play(anim1).after(Constants.ANIM_DURATION_MIND).after(anim3);
        anim1.setInterpolator(new OvershootInterpolator(3.0F));
        anim2.setInterpolator(new OvershootInterpolator(3.0F));
        set.start();
    }
}
