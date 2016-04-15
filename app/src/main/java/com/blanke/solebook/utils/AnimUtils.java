package com.blanke.solebook.utils;


import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

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
        anim3 = getTranslationYAnim(v, 0, 60, Constants.ANIM_DURATION_MIND);
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
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator anim1, anim2, anim3, anim4, anim5;
        float scaleTemp;
        if (v.getVisibility() == View.VISIBLE) {//缩小一点之后再放大
            scaleTemp = 0.9F;
            anim1 = getScaleXAnim(v, 1F, scaleTemp, Constants.ANIM_DURATION_SHORT);
            anim2 = getScaleYAnim(v, 1F, scaleTemp, Constants.ANIM_DURATION_SHORT);
            anim3 = getScaleXAnim(v, scaleTemp, 1F, Constants.ANIM_DURATION_SHORT);
            anim4 = getScaleYAnim(v, scaleTemp, 1F, Constants.ANIM_DURATION_SHORT);
            set.play(anim1).with(anim2);
            set.play(anim3).with(anim4);
            set.play(anim3).after(anim1);
            set.setInterpolator(new OvershootInterpolator(15.0F));
            set.start();
            return;
        }
        v.clearAnimation();
        v.setVisibility(View.VISIBLE);
        scaleTemp = 0.3F;
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

    public static void toggleTheme(ImageView v, int endImgRes, CallBack callBack) {
        v.clearAnimation();
        AnimatorSet set = new AnimatorSet(), set2 = new AnimatorSet();
        float scaleTemp = 0.5F;
        ObjectAnimator anim1, anim2, anim3, anim4, anim5;
        anim1 = getScaleXAnim(v, 1F, scaleTemp, Constants.ANIM_DURATION_MIND);
        anim2 = getScaleYAnim(v, 1F, scaleTemp, Constants.ANIM_DURATION_MIND);
        anim4 = getScaleXAnim(v, scaleTemp, 1F, Constants.ANIM_DURATION_MIND);
        anim5 = getScaleYAnim(v, scaleTemp, 1F, Constants.ANIM_DURATION_MIND);
        set.play(anim1).with(anim2);
        set.play(anim4).with(anim5);
        set.play(anim4).after(anim1);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setImageResource(endImgRes);
                if (callBack != null) {
                    callBack.call();
                }
            }
        });
        anim4.setInterpolator(new OvershootInterpolator(2.0F));
        anim5.setInterpolator(new OvershootInterpolator(2.0F));
        set.start();
    }

    public static void loginShow(View v) {
        v.clearAnimation();
        v.setVisibility(View.VISIBLE);
        v.setScaleX(0.1F);
        v.setScaleY(0.1F);
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator anim4 = getScaleXAnim(v, 0.1F, 1F, Constants.ANIM_DURATION_MIND);
        ObjectAnimator anim5 = getScaleYAnim(v, 0.1F, 1F, Constants.ANIM_DURATION_MIND);
        set.play(anim4).with(anim5);
        anim4.setInterpolator(new OvershootInterpolator(3.0F));
        anim5.setInterpolator(new OvershootInterpolator(3.0F));
        set.start();
//        ViewPropertyAnimator.animate(v)
//                .alpha(1F)
//                .setDuration(Constants.ANIM_DURATION_LONG).start();
    }

    public interface CallBack {
        void call();
    }
}
