package com.blanke.solebook.core.comment.persenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.DeleteCallback;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookComment;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.rx.RxBookComment;


/**
 * Created by Blanke on 16-3-22.
 */
public class CommentPersenterImpl extends CommentPersenter {

    private boolean pullToRefresh;

    @Override
    public void getBookCommentData(Book book, boolean pullToRefresh, int skip, int limit) {
        this.pullToRefresh = pullToRefresh;
        getView().showLoading(pullToRefresh);
        RxBookComment.getBookCommentListData(
                book, limit, skip).subscribe(this::onSuccess, this::onFail);
    }

    @Override
    public void sendBookComment(Book book, BookComment reply, String content) {
        if (getView() == null) {
            return;
        }
        SoleUser user = SoleUser.getCurrentUser(SoleUser.class);
        if (user == null || user.isAnonymous()) {
            return;
        }
        RxBookComment.sendBookComment(book, reply, content, user)
                .subscribe(bookComments -> getView().sendSuccess()
                        , this::onFail);
    }

    @Override
    public void deleteComment(BookComment bookComment) {
        bookComment.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(AVException e) {
                if (getView() != null) {
                    getView().deleteFinish(e);
                }
            }
        });
    }

    @Override
    public boolean getPullToRefresh() {
        return pullToRefresh;
    }
}
