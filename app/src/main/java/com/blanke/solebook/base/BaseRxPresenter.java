package com.blanke.solebook.base;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

/**
 * Created by Blanke on 16-3-2.
 */
public abstract class BaseRxPresenter<V extends MvpLceView, M> extends MvpBasePresenter<V> {

    protected void onSuccess(M data) {
        if (isViewAttached()) {
            getView().setData(data);
            getView().showContent();
        }
    }

    public abstract boolean getPullToRefresh();

    protected void onFail(Throwable e) {
        if (isViewAttached()) {
            getView().showError(e, getPullToRefresh());
        }
    }
}
