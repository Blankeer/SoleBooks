package com.blanke.solebook.base;

import android.os.Bundle;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;

/**
 * Created by Blanke on 16-1-7.
 */
public abstract class BaseMvpViewStateFragment<V extends MvpView, P extends MvpPresenter<V>> extends MvpViewStateFragment<V, P> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
