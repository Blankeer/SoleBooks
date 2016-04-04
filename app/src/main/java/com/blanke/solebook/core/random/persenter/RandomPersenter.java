package com.blanke.solebook.core.random.persenter;

import com.blanke.solebook.base.BaseRxPresenter;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.core.random.view.RandomView;

import java.util.List;

/**
 * Created by Blanke on 16-2-26.
 */
public abstract class RandomPersenter extends BaseRxPresenter<RandomView, List<Book>> {
    public abstract void getSearchRes(boolean pullToRefresh, int count);
}
