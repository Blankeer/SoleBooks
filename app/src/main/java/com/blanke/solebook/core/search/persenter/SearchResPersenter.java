package com.blanke.solebook.core.search.persenter;

import com.blanke.solebook.base.BaseRxPresenter;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.core.search.view.SearchResView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

/**
 * Created by Blanke on 16-2-26.
 */
public abstract class SearchResPersenter extends BaseRxPresenter<SearchResView, List<Book>> {
    public abstract void getSearchRes(boolean pullToRefresh, int limit, String key);

}
