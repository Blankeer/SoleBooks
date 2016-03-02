package com.blanke.solebook.core.main.persenter;

import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.rx.RxBookColumn;

import java.util.List;

/**
 * Created by Blanke on 16-2-23.
 */
public class MainPersenterImpl extends MainPersenter {
    private boolean pullToRefresh;

    @Override
    public void loadBookColumn(boolean pullToRefresh) {
        this.pullToRefresh = pullToRefresh;
        getView().showLoading(pullToRefresh);
        RxBookColumn.getMainColumnData()
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
