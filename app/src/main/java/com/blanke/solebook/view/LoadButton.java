package com.blanke.solebook.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 * Created by Blanke on 16-2-19.
 */
public class LoadButton extends FrameLayout {
    private final ProgressBar mProgress;
    private final Button mButton;
    private boolean isShowing = false;

    public LoadButton(Context context) {
        this(context, null);
    }

    public LoadButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mButton = new Button(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ViewGroup.LayoutParams layoutParams2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mButton.setLayoutParams(layoutParams);
        mProgress = new ProgressBar(context);
        mProgress.setElevation(mButton.getElevation() + 20);
        mProgress.setLayoutParams(layoutParams2);
        mProgress.setVisibility(View.INVISIBLE);
        mProgress.setBackgroundColor(Color.TRANSPARENT);
        addView(mButton);
        addView(mProgress);


    }

    public void setText(String text) {
        this.mButton.setText(text);
    }

    public void setTextColor(int color) {
        mProgress.getIndeterminateDrawable().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        mButton.setTextColor(color);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (isShowing) {
                mProgress.setVisibility(View.INVISIBLE);
                mButton.setTextColor(Color.WHITE);
            } else {
                mButton.setTextColor(mButton.getDrawingCacheBackgroundColor());
                mProgress.setVisibility(View.VISIBLE);
            }
            isShowing = !isShowing;
        }
        return super.onInterceptTouchEvent(ev);
    }

    class MyProgress extends ProgressBar {

        public MyProgress(Context context) {
            super(context);
        }

        @Override
        protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int w = MeasureSpec.getSize(widthMeasureSpec);
            int h = MeasureSpec.getSize(heightMeasureSpec);
            setMeasuredDimension(32, 32);
        }
    }
}
