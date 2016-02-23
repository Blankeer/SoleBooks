package com.blanke.solebook.core.main.persenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.FindCallback;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.core.main.view.MainView;
import com.blanke.solebook.utils.AvosCacheUtils;

import java.util.List;

/**
 * Created by Blanke on 16-2-23.
 */
public class MainPersenterImpl extends MainPersenter {
    @Override
    public void loadBookColumn(boolean pullToRefresh) {
        getView().showLoading(pullToRefresh);
        AvosCacheUtils.CacheELseNetwork(BookColumn.getQuery(BookColumn.class))
                .whereLessThan("order", 10)
                .orderByAscending("order")
                .findInBackground(new FindCallback<BookColumn>() {
                    @Override
                    public void done(List<BookColumn> list, AVException e) {
//                        KLog.json(list.toString());
                        if (isViewAttached()) {
                            MainView view = getView();
                            if (e == null) {
                                view.setData(list);
                                view.showContent();
                            } else {
                                view.showError(e, pullToRefresh);
                            }
                        }
                    }
                });
    }
}
