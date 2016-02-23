package com.blanke.solebook.core.columnitem.persenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.column.view.ColumnView;
import com.blanke.solebook.core.columnitem.view.ColumnItemView;
import com.blanke.solebook.utils.AvosCacheUtils;
import com.socks.library.KLog;

import java.util.List;

/**
 * Created by Blanke on 16-2-22.
 */
public class ColumnItemPersenterImpl extends ColumnItemPersenter {

    @Override
    public void getBookData(BookColumn bookColumn, boolean isCache, boolean pullToRefresh, int skip, int limit) {
        getView().showLoading(pullToRefresh);
        AvosCacheUtils.CacheELseNetwork(bookColumn.getBooks().getQuery(Book.class))
                .setPolicy(isCache ? AVQuery.CachePolicy.CACHE_ELSE_NETWORK : AVQuery.CachePolicy.NETWORK_ONLY)
                .limit(limit)
                .skip(skip)
                .findInBackground(new FindCallback<Book>() {
                    @Override
                    public void done(List<Book> list, AVException e) {
                        if (isViewAttached()) {
                            ColumnItemView view = getView();
                            view.setData(list);
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
