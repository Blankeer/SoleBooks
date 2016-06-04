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

    /**
     * persenter层,
     * @param pullToRefresh 是否是用户手动刷新
     * @param limit 返回数量
     * @param key 搜索关键字
     */
    @Override
    public void getSearchRes(boolean pullToRefresh, int limit, String key) {
        this.pullToRefresh = pullToRefresh;
        getView().showLoading(pullToRefresh);//调用view层显示loading状态
        HashMap<String, String> map = new HashMap<>();
        map.put("key", key);//封装参数
        new RxCloudFunction<List<Book>>()//调用服务器端搜索接口,rxjava语法
                .executeCloud(Constants.CLOUD_FUNCTION_SEARCH_BOOK, map)
                .subscribe(this::onSuccess, this::onFail);
    }

    public boolean getPullToRefresh(){
        return pullToRefresh;
    }
}
