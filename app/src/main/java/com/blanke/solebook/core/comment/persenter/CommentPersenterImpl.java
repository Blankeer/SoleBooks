package com.blanke.solebook.core.comment.persenter;

import com.blanke.solebook.bean.Book;

/**
 * Created by Blanke on 16-3-22.
 */
public class CommentPersenterImpl extends CommentPersenter {

    private boolean pullToRefresh;

    @Override
    public void getBookCommentData(Book book, boolean pullToRefresh, int skip, int limit) {
        this.pullToRefresh = pullToRefresh;
        getView().showLoading(pullToRefresh);
//        RxBookColumn.getBookListData(
//                bookColumn, AVQuery.CachePolicy.NETWORK_ELSE_CACHE
//                , limit, skip)
//                .subscribe(this::onSuccess, this::onFail);
    }

    @Override
    public boolean getPullToRefresh() {
        return pullToRefresh;
    }
}
