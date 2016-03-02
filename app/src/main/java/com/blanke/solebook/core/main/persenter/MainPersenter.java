package com.blanke.solebook.core.main.persenter;

import com.blanke.solebook.base.BaseRxPresenter;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.core.main.view.MainView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

/**
 * Created by Blanke on 16-2-23.
 */
public abstract class MainPersenter extends BaseRxPresenter<MainView, List<BookColumn>> {
    public abstract void loadBookColumn(boolean pullToRefresh);
}
