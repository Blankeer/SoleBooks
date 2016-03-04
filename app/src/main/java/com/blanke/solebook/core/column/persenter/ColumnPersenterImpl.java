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

    public boolean getPullToRefresh() {
        return pullToRefresh;
    }
}
