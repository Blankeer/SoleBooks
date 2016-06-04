package com.blanke.solebook.core.random.persenter;

import com.blanke.solebook.bean.Book;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.rx.RxCloudFunction;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Blanke on 16-2-26.
 */
public class RandomPersenterImpl extends RandomPersenter {
    private boolean pullToRefresh;

    @Override
    public void getSearchRes(boolean pullToRefresh, int count) {
        this.pullToRefresh = pullToRefresh;
        getView().showLoading(pullToRefresh);
        HashMap<String, String> map = new HashMap<>();
        map.put("count", count + "");
        new RxCloudFunction<List<Book>>()
                .executeCloud(Constants.CLOUD_FUNCTION_RANDOM_BOOK, map)
                .subscribe(this::onSuccess, this::onFail);
    }

    @Override
    public boolean getPullToRefresh() {
        return pullToRefresh;
    }
}
