package com.blanke.solebook.rx.subscribe;

import com.avos.avoscloud.AVQuery;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.rx.subscribe.base.BaseCloudOnSubscribe;

import java.util.List;

/**
 * main 主栏目   榜单/发现  等
 * Created by Blanke on 16-3-2.
 */
public class MainColumnOnSubscribe extends BaseCloudOnSubscribe<List<BookColumn>> {


    public MainColumnOnSubscribe(AVQuery.CachePolicy cachePolicy, long maxCacheAge, long detaly) {
        super(cachePolicy, maxCacheAge, detaly);
    }

    public MainColumnOnSubscribe() {
    }

    @Override
    protected List<BookColumn> execute() throws Exception {
        return prepare(BookColumn.getQuery(BookColumn.class)
                .whereLessThan("order", Constants.CLOUD_MAIN_COLUMN_MAX_ORDER)
                .orderByAscending("order"))
                .find();
    }
}
