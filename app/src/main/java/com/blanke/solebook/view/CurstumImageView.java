package com.blanke.solebook.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.socks.library.KLog;

/**
 * Created by Blanke on 16-2-29.
 */
public class CurstumImageView extends ImageView {

    private final Drawable mDrawable;
    private int mDrawableWidth;
    private int mDrawableHeight;

    public CurstumImageView(Context context) {
        this(context, null);
    }

    public CurstumImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurstumImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CurstumImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mDrawable = getDrawable();
        mDrawableWidth = mDrawable.getIntrinsicWidth();
        mDrawableHeight = mDrawable.getIntrinsicHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        KLog.d(widthMode + "," + heightMode);
        if (widthMode != heightMode) {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            if (heightMode == MeasureSpec.AT_MOST) {
                int finalHeightSize = getHoldHeight(widthSize);
                setMeasuredDimension(widthSize + getPaddingLeft() + getPaddingRight()
                        , Math.min(finalHeightSize, heightSize) + getPaddingTop() + getPaddingBottom());
            } else {
                int finalWidthSize = getHoldWidth(heightSize);
                setMeasuredDimension(Math.min(widthSize, finalWidthSize) + getPaddingLeft() + getPaddingRight()
                        , heightSize + getPaddingTop() + getPaddingBottom());
            }
            return;
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
