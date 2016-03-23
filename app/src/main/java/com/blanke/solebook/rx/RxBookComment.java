package com.blanke.solebook.rx;

import com.avos.avoscloud.AVQuery;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookComment;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.rx.subscribe.BookCommentListOnSubscribe;
import com.blanke.solebook.rx.subscribe.BookCommentSendOnSubscribe;
import com.blanke.solebook.utils.RxUtils;

import java.util.List;

import rx.Observable;

/**
 * Created by Blanke on 16-3-2.
 */
public class RxBookComment {

    public static Observable<List<BookComment>> getBookCommentListData(Book book, int limit, int skip) {
        return RxUtils.schedulerNewThread(
                Observable.create(new BookCommentListOnSubscribe(AVQuery.CachePolicy.NETWORK_ELSE_CACHE, book, limit, skip))
        );
    }

    public static Observable<List<BookComment>> sendBookComment(Book book, BookComment reply, String comment, SoleUser user) {
        return RxUtils.schedulerNewThread(
                Observable.create(new BookCommentSendOnSubscribe(book,reply, user, comment))
        );
    }

}
