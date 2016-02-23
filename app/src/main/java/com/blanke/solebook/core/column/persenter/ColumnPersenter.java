package com.blanke.solebook.core.column.persenter;

import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.core.column.view.ColumnView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by Blanke on 16-2-22.
 */
public abstract class ColumnPersenter extends MvpBasePresenter<ColumnView> {

    abstract public void getColumnData(BookColumn parentBookColumn,boolean pullToRefresh);
}
