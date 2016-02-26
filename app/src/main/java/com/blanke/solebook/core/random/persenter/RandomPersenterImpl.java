package com.blanke.solebook.core.random.persenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.utils.AvosCacheUtils;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.socks.library.KLog;

import java.util.List;

/**
 * Created by Blanke on 16-2-26.
 */
public class RandomPersenterImpl extends RandomPersenter {
    @Override
    public void getSearchRes(boolean pullToRefresh, int skip, int limit) {
        getView().showLoading(pullToRefresh);
        AVQuery<Book> query = Book.getQuery(Book.class);
        AvosCacheUtils.CacheELseNetwork(query)
                .limit(limit)
                .skip(skip)
                .findInBackground(new FindCallback<Book>() {
                    @Override
                    public void done(List<Book> list, AVException e) {
                        if (isViewAttached()) {
                            MvpLceView view = getView();
                            view.setData(list);
                            KLog.d(list.size());
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
