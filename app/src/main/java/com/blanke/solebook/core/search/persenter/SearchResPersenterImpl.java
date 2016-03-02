package com.blanke.solebook.core.search.persenter;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.FunctionCallback;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.constants.Constants;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Blanke on 16-2-26.
 */
public class SearchResPersenterImpl extends SearchResPersenter {
    @Override
    public void getSearchRes(boolean pullToRefresh, int limit, String key) {
        getView().showLoading(pullToRefresh);
        HashMap<String, String> map = new HashMap<>();
        map.put("key", key);
        AVCloud.rpcFunctionInBackground(Constants.CLOUD_MOTHOD_SEARCH_KEY
                , map, new FunctionCallback<List<Book>>() {
                    @Override
                    public void done(List<Book> list, AVException e) {
                        if (isViewAttached()) {
                            MvpLceView view = getView();
                            view.setData(list);
                            KLog.d(list == null ? "null" : list.size());
                            if (e == null) {
                                view.showContent();
                            } else {
                                view.showError(e, pullToRefresh);
                            }
                        }
                    }
                });
    }
}
