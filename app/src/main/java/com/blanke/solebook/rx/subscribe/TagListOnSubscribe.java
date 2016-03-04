package com.blanke.solebook.rx.subscribe;

import com.avos.avoscloud.AVQuery;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.bean.Tag;
import com.blanke.solebook.rx.subscribe.base.BaseCloudOnSubscribe;

import java.util.List;

/**
 * Created by Blanke on 16-3-2.
 */
public class TagListOnSubscribe extends BaseCloudOnSubscribe<List<Tag>> {
    private int limit, skip;

    public TagListOnSubscribe(AVQuery.CachePolicy cachePolicy, int limit, int skip) {
        super(cachePolicy);
        this.limit = limit;
        this.skip = skip;
    }

    @Override
    protected List<Tag> execute() throws Exception {
        return prepare(Tag.getQuery(Tag.class))
                .limit(limit)
                .skip(skip)
                .find();
    }
}
