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


    public boolean getPullToRefresh(){
        return pullToRefresh;
    }
}
