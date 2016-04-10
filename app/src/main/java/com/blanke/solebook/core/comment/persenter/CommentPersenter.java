package com.blanke.solebook.core.comment.persenter;

import com.blanke.solebook.base.BaseRxPresenter;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookComment;
import com.blanke.solebook.core.comment.view.CommentView;

import java.util.List;

/**
 * Created by Blanke on 16-3-22.
 */
public abstract class CommentPersenter extends BaseRxPresenter<CommentView, List<BookComment>> {

    abstract public void getBookCommentData(Book book, boolean pullToRefresh, int skip, int limit);

    abstract public void sendBookComment(Book book, BookComment reply, String content);

    abstract public void deleteComment(BookComment bookComment);
}
