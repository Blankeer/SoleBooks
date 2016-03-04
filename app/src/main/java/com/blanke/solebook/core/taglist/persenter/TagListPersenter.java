package com.blanke.solebook.core.taglist.persenter;

import com.blanke.solebook.base.BaseRxPresenter;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.bean.Tag;
import com.blanke.solebook.core.booklist.view.BookListView;
import com.blanke.solebook.core.taglist.view.TagListView;

import java.util.List;

/**
 * Created by Blanke on 16-2-22.
 */
public abstract class TagListPersenter extends BaseRxPresenter<TagListView, List<Tag>> {
    abstract public void getTagData(boolean pullToRefresh, int skip, int limit);
}
