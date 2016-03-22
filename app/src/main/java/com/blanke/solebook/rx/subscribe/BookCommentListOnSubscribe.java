package com.blanke.solebook.rx.subscribe;

import com.avos.avoscloud.AVQuery;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.bean.BookComment;
import com.blanke.solebook.rx.subscribe.base.BaseCloudOnSubscribe;

import java.util.List;

/**
 * Created by Blanke on 16-3-2.
 */
public class BookCommentListOnSubscribe extends BaseCloudOnSubscribe<List<Book>> {
    private Book book;
    private int limit, skip;

    public BookCommentListOnSubscribe(AVQuery.CachePolicy cachePolicy, Book book, int limit, int skip) {
        super(cachePolicy);
        this.book = book;
        this.limit = limit;
        this.skip = skip;
    }

    @Override
    protected List<Book> execute() throws Exception {
        return prepare(BookComment.getQuery(BookComment.class)
                .whereEqualTo("book", book)
                .limit(limit)
                .skip(skip)
                .order("-updatedAt"))
                .find();
    }
}
