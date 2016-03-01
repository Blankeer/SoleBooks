package com.blanke.solebook.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Blanke on 16-2-29.
 */
public class HoldScaleImageView extends ImageView {

    private Drawable mDrawable = null;
    private int mDrawableWidth;
    private int mDrawableHeight;
    private boolean holdWidth, holdHeight;

    public HoldScaleImageView(Context context) {
        this(context, null);
    }

    public HoldScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HoldScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, com.blanke.holdscaleimageview.R.styleable.HoldScaleImageView, defStyleAttr, 0);
        holdWidth = a.getBoolean(com.blanke.holdscaleimageview.R.styleable.HoldScaleImageView_holdWidth, false);
        holdHeight = a.getBoolean(com.blanke.holdscaleimageview.R.styleable.HoldScaleImageView_holdHeight, false);
        a.recycle();
        mDrawable = getDrawable();
        mDrawableWidth = mDrawable.getIntrinsicWidth();
        mDrawableHeight = mDrawable.getIntrinsicHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (holdHeight != holdWidth) {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            int widthMode = MeasureSpec.getMode(widthSize);
            int heightMode = MeasureSpec.getMode(heightSize);
            if (holdWidth) {
                if (heightMode == MeasureSpec.AT_MOST) {
                    int finalHeightSize = getHoldHeight(widthSize);
                    setMeasuredDimension(widthSize + getPaddingLeft() + getPaddingRight()
                            , Math.min(finalHeightSize, heightSize) + getPaddingTop() + getPaddingBottom());
                    return;
                }
            } else {
                if (widthMode == MeasureSpec.AT_MOST) {
                    int finalWidthSize = getHoldWidth(heightSize);
                    setMeasuredDimension(Math.min(widthSize, finalWidthSize) + getPaddingLeft() + getPaddingRight()
                            , heightSize + getPaddingTop() + getPaddingBottom());
                    return;
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int getHoldHeight(int widthSize) {
        return widthSize * mDrawableHeight / mDrawableWidth;
    }

    private int getHoldWidth(int heightSize) {
        return heightSize * mDrawableWidth / mDrawableHeight;
    }
}
