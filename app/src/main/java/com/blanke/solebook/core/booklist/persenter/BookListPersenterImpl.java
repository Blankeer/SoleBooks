package com.blanke.solebook.core.booklist.persenter;

import com.avos.avoscloud.AVQuery;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.rx.RxBookColumn;

import java.util.List;

/**
 * Created by Blanke on 16-2-22.
 */
public class BookListPersenterImpl extends BookListPersenter {

    private boolean pullToRefresh;

    @Override
    public void getBookData(BookColumn bookColumn, boolean isCache, boolean pullToRefresh, int skip, int limit) {
        this.pullToRefresh = pullToRefresh;
        getView().showLoading(pullToRefresh);
        RxBookColumn.getBookListData(
                bookColumn, isCache ? AVQuery.CachePolicy.CACHE_ELSE_NETWORK : AVQuery.CachePolicy.NETWORK_ONLY
                , limit, skip)
                .subscribe(this::onSuccess, this::onFail);
    }

    @Override
    protected void onSuccess(List<Book> data) {
        if (isViewAttached()) {
            getView().setData(data);
            getView().showContent();
        }
    }

    @Override
    protected void onFail(Throwable e) {
        if (isViewAttached()) {
            getView().showError(e, pullToRefresh);
        }
    }
}
