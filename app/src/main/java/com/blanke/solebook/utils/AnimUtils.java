package com.blanke.solebook.utils;


import android.renderscript.ScriptC;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.blanke.solebook.constants.Constants;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.TimeAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewPropertyAnimator;

import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

/**
 * Created by Blanke on 16-3-31.
 */
public class AnimUtils {
    private ObjectAnimator getScaleXAnim(View v,float i,float j){
        return ObjectAnimator.ofFloat(v, "scaleX", i, j).setDuration(1000);
    }

    public static void hide(View v) {
        if (v.getVisibility() == View.GONE) {
            return;
        }
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator anim1, anim2, anim3, anim4,anim5;
        anim1 = ObjectAnimator.ofFloat(v, "scaleX", 1F, 0.3F).setDuration(1000);
        anim2 = ObjectAnimator.ofFloat(v, "scaleY", 1F, 0.3F).setDuration(1000);
        anim3 = ObjectAnimator.ofFloat(v, "translationY", 0, 100).setDuration(1000);
        anim4 = ObjectAnimator.ofFloat(v, "scaleX", 0.3F, 0F).setDuration(1000);
        anim5 = ObjectAnimator.ofFloat(v, "scaleY", 0.3F, 0F).setDuration(1000);
        set.play(anim1).with(anim2);
        set.play(anim3).after(800).after(anim1);
        set.play(anim4).with(anim5);
        set.play(anim4).after(anim3);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.GONE);
            }
        });
        set.start();
    }

    public static void show(View v) {
        if (v.getVisibility() == View.VISIBLE) {
            return;
        }
    }
}
