package com.blanke.solebook.core.columnitem.persenter;

import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.core.column.view.ColumnView;
import com.blanke.solebook.core.columnitem.view.ColumnItemView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by Blanke on 16-2-22.
 */
public abstract class ColumnItemPersenter extends MvpBasePresenter<ColumnItemView> {

    abstract public void getBookData(BookColumn bookColumn, boolean pullToRefresh);
}
