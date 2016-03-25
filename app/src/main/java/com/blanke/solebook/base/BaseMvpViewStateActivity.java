package com.blanke.solebook.base;

import com.avos.avoscloud.AVAnalytics;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;

/**
 * Created by Blanke on 16-1-7.
 */
public abstract class BaseMvpViewStateActivity<V extends MvpView, P extends MvpPresenter<V>> extends MvpViewStateActivity<V, P> {
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }

    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
    }
}
