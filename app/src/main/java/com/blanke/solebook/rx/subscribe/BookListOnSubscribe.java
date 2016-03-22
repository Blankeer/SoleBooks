package com.blanke.solebook.rx.subscribe;

import com.avos.avoscloud.AVQuery;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.rx.subscribe.base.BaseCloudOnSubscribe;

import java.util.List;

/**
 * Created by Blanke on 16-3-2.
 */
public class BookListOnSubscribe extends BaseCloudOnSubscribe<List<Book>> {
    private BookColumn column;
    private int limit, skip;

    public BookListOnSubscribe(AVQuery.CachePolicy cachePolicy, BookColumn column, int limit, int skip) {
        super(cachePolicy);
        this.column = column;
        this.limit = limit;
        this.skip = skip;
    }

    @Override
    protected List<Book> execute() throws Exception {
        return prepare(column.getBooks().getQuery(Book.class)
                .limit(limit)
                .skip(skip)
                .order("top,-updatedAt"))
                .find();
    }
}
