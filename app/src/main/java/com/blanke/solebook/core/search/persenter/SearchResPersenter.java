package com.blanke.solebook.core.search.persenter;

import com.blanke.solebook.core.search.view.SearchResView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by Blanke on 16-2-26.
 */
public abstract class SearchResPersenter extends MvpBasePresenter<SearchResView> {
    public abstract void getSearchRes(boolean pullToRefresh, int limit, String key);
}
