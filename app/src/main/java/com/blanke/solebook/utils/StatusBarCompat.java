package com.blanke.solebook.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * https://github.com/niorgai/StatusBarCompat/blob/master/library%2Fsrc%2Fmain%2Fjava%2Fqiu%2Fniorgai%2FStatusBarCompat.java
 * Created by blanke on 16-4-2.
 */
public class StatusBarCompat {

    public static final int COLOR_DEFAULT_WHITE = Color.parseColor("#FFFFFFFF");
    public static final int COLOR_DEFAULT_PINK = Color.parseColor("#FFEF4968");

    private static void setStatusBarColorByContentView(ViewGroup mContentView, Activity activity, int statusColor) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //First translucent status bar.
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //After LOLLIPOP not translucent status bar
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //Then call setStatusBarColor.
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(statusColor);
                //set child View not fill the system window
                View mChildView = mContentView.getChildAt(0);
                if (mChildView != null) {
                    ViewCompat.setFitsSystemWindows(mChildView, true);
                }
            } else {
                setStatusBarByAddView(mContentView, activity, statusColor);
            }
        }
    }

    /**
     * 通过add view的方式造假状态栏，曲线救国 = =
     *
     * @param mContentView
     * @param activity
     * @param statusColor
     */
    private static void setStatusBarByAddView(ViewGroup mContentView, Activity activity, int statusColor) {
        int statusBarHeight = getStatusBarHeight(activity);
        ViewCompat.setFitsSystemWindows(mContentView,false);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mChildView.getLayoutParams();
            //if margin top has already set, just skip.
            if (lp != null && lp.topMargin < statusBarHeight && lp.height != statusBarHeight) {
                //do not use fitsSystemWindows
                ViewCompat.setFitsSystemWindows(mChildView, false);
                //add margin to content
                lp.topMargin += statusBarHeight;
                mChildView.setLayoutParams(lp);
            }
        }

        //Before LOLLIPOP create a fake status bar View.
        View statusBarView = mContentView.getChildAt(0);
        if (statusBarView != null && statusBarView.getLayoutParams() != null && statusBarView.getLayoutParams().height == statusBarHeight) {
            //if fake status bar view exist, we can setBackgroundColor and return.
            statusBarView.setBackgroundColor(statusColor);
            return;
        }
        statusBarView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        statusBarView.setBackgroundColor(statusColor);
        mContentView.addView(statusBarView, 0, lp);
    }

    public static void setStatusBarColor(Activity activity, int statusColor) {
        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        setStatusBarColorByContentView(mContentView, activity, statusColor);
    }

    public static void setStatusBarColorByDrawerLayout(Activity activity, DrawerLayout drawerLayout, int statusColor) {
        ViewCompat.setFitsSystemWindows(drawerLayout,false);

        ViewGroup mContentView = (ViewGroup) drawerLayout.getChildAt(0);
        //setStatusBarColorByContentView(mContentView, activity, statusColor);//set contentview statuscolor
        setStatusBarByAddView(mContentView, activity, statusColor);//造假状态栏
        ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
        translucentStatusBarByContentView(drawer, activity);//set drawerlayou status trans
    }

    public static void translucentStatusBarByContentView(ViewGroup mContentView, Activity activity) {
        Window window = activity.getWindow();
        //set child View not fill the system window
        View mChildView = mContentView.getChildAt(0);
        ViewCompat.setFitsSystemWindows(mContentView, false);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight(activity);
            //First translucent status bar.
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //After LOLLIPOP just set LayoutParams.
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            } else {
                if (mChildView != null && mChildView.getLayoutParams() != null && mChildView.getLayoutParams().height == statusBarHeight) {
                    //Before LOLLIPOP need remove fake status bar view.
                    mContentView.removeView(mChildView);
                    mChildView = mContentView.getChildAt(0);
                }
                if (mChildView != null) {
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
                    //cancel the margin top
                    if (lp != null && lp.topMargin >= statusBarHeight) {
                        lp.topMargin -= statusBarHeight;
                        mChildView.setLayoutParams(lp);
                    }
                }
            }

        }
    }

    public static void translucentStatusBar(Activity activity) {
        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        translucentStatusBarByContentView(mContentView, activity);
    }


    public static void setStatusBarColor(Activity activity) {
        setStatusBarColor(activity, COLOR_DEFAULT_PINK);
    }

    //Get status bar height
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }
        return result;
    }

}
