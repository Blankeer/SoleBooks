package com.blanke.solebook.rx.subscribe;

import com.avos.avoscloud.AVQuery;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.rx.subscribe.base.BaseCloudOnSubscribe;

import java.util.List;

/**
 * 子栏目  比如 榜单 下的  新书榜  热门榜
 * Created by Blanke on 16-3-2.
 */
public class SubColumnOnSubscribe extends BaseCloudOnSubscribe<List<BookColumn>> {
    private BookColumn columnParent;

    public SubColumnOnSubscribe(BookColumn columnParent) {
        this.columnParent = columnParent;
    }

    public SubColumnOnSubscribe(AVQuery.CachePolicy cachePolicy, long maxCacheAge, long detaly, BookColumn columnParent) {
        super(cachePolicy, maxCacheAge, detaly);
        this.columnParent = columnParent;
    }

    @Override
    protected List<BookColumn> execute() throws Exception {
        return prepare(columnParent.getSubs().getQuery(BookColumn.class)
                .orderByAscending("order"))
                .find();
    }
}
