package com.blanke.solebook.core.booklist.persenter;

import com.blanke.solebook.base.BaseRxPresenter;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.core.booklist.view.BookListView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

/**
 * Created by Blanke on 16-2-22.
 */
public abstract class BookListPersenter extends BaseRxPresenter<BookListView, List<Book>> {

    abstract public void getBookData(BookColumn bookColumn, boolean pullToRefresh, int skip, int limit);
}
