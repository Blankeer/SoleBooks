package com.blanke.solebook.rx;

import com.avos.avoscloud.AVQuery;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.bean.Tag;
import com.blanke.solebook.rx.subscribe.BookListOnSubscribe;
import com.blanke.solebook.rx.subscribe.MainColumnOnSubscribe;
import com.blanke.solebook.rx.subscribe.SubColumnOnSubscribe;
import com.blanke.solebook.rx.subscribe.TagListOnSubscribe;
import com.blanke.solebook.utils.RxUtils;

import java.util.List;

import rx.Observable;

/**
 * Created by Blanke on 16-3-2.
 */
public class RxBookColumn {
    public static Observable<List<BookColumn>> getMainColumnData() {
        return RxUtils.schedulerNewThread(
                Observable.create(new MainColumnOnSubscribe())
        );
    }

    public static Observable<List<BookColumn>> getSubColumnData(BookColumn columnParent) {
        return RxUtils.schedulerNewThread(
                Observable.create(new SubColumnOnSubscribe(columnParent))
        );
    }

    public static Observable<List<Book>> getBookListData(BookColumn column, AVQuery.CachePolicy cachePolicy, int limit, int skip) {
        return RxUtils.schedulerNewThread(
                Observable.create(new BookListOnSubscribe(cachePolicy, column, limit, skip))
        );
    }

    public static Observable<List<Tag>> getTagListData(AVQuery.CachePolicy cachePolicy, int limit, int skip) {
        return RxUtils.schedulerNewThread(
                Observable.create(new TagListOnSubscribe(cachePolicy, limit, skip))
        );
    }
}
