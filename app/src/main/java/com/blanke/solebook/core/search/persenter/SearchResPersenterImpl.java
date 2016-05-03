package com.blanke.solebook.core.search.persenter;

import com.blanke.solebook.bean.Book;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.rx.RxCloudFunction;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Blanke on 16-2-26.
 */
public class SearchResPersenterImpl extends SearchResPersenter {
    private boolean pullToRefresh;

    @Override
    public void getSearchRes(boolean pullToRefresh, int limit, String key) {
        this.pullToRefresh = pullToRefresh;
        getView().showLoading(pullToRefresh);
        HashMap<String, String> map = new HashMap<>();
        map.put("key", key);
        new RxCloudFunction<List<Book>>()
                .executeCloud(Constants.CLOUD_MOTHOD_SEARCH_BOOK, map)
                .subscribe(this::onSuccess, this::onFail);
    }

    public boolean getPullToRefresh(){
        return pullToRefresh;
    }
}
