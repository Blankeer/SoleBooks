package com.blanke.solebook.core.column.persenter;

import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.rx.RxBookColumn;

import java.util.List;

/**
 * Created by Blanke on 16-2-22.
 */
public class ColumnPersenterImpl extends ColumnPersenter {
    private boolean pullToRefresh;

    @Override
    public void getColumnData(BookColumn parentBookColumn, boolean pullToRefresh) {
        this.pullToRefresh = pullToRefresh;
        getView().showLoading(pullToRefresh);
        RxBookColumn.getSubColumnData(parentBookColumn)
                .subscribe(this::onSuccess, this::onFail);
    }

    @Override
    protected void onSuccess(List<BookColumn> data) {
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
