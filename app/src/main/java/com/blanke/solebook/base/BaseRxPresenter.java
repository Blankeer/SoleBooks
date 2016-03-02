package com.blanke.solebook.base;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Blanke on 16-3-2.
 */
public abstract class BaseRxPresenter<V extends MvpView, M> extends MvpBasePresenter<V> {

    protected abstract void onSuccess(M data);

    protected abstract void onFail(Throwable e);
}
