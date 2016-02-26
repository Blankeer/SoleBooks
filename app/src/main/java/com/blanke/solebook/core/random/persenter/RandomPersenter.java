package com.blanke.solebook.core.random.persenter;

import com.blanke.solebook.core.random.view.RandomView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by Blanke on 16-2-26.
 */
public abstract class RandomPersenter extends MvpBasePresenter<RandomView> {
    public abstract void getSearchRes(boolean pullToRefresh, int skip, int limit);
}
