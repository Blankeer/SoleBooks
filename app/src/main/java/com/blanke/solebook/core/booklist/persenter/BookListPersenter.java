package com.blanke.solebook.core.booklist.persenter;

import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.core.booklist.view.BookListView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by Blanke on 16-2-22.
 */
public abstract class BookListPersenter extends MvpBasePresenter<BookListView> {

    abstract public void getBookData(BookColumn bookColumn,boolean isCache, boolean pullToRefresh, int skip, int limit);
}
