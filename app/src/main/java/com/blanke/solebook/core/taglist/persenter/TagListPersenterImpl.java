package com.blanke.solebook.core.taglist.persenter;

import com.avos.avoscloud.AVQuery;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.bean.Tag;
import com.blanke.solebook.rx.RxBookColumn;

import java.util.List;

/**
 * Created by Blanke on 16-2-22.
 */
public class TagListPersenterImpl extends TagListPersenter {

    private boolean pullToRefresh;

    @Override
    public void getTagData(boolean pullToRefresh, int skip, int limit) {
        this.pullToRefresh = pullToRefresh;
        getView().showLoading(pullToRefresh);
        RxBookColumn.getTagListData(
                AVQuery.CachePolicy.CACHE_ELSE_NETWORK
                , limit, skip)
                .subscribe(this::onSuccess, this::onFail);
    }

    public boolean getPullToRefresh() {
        return pullToRefresh;
    }
}