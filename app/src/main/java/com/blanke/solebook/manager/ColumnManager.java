package com.blanke.solebook.manager;

import com.blanke.solebook.base.BaseColumnFragment;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.booklist.BookListFragment;
import com.blanke.solebook.core.taglist.TagListFragment_;
import com.socks.library.KLog;

/**
 * Created by Blanke on 16-2-23.
 */
public class ColumnManager {

    public static BaseColumnFragment getColumnFragment(BookColumn bookColumn) {
        switch (bookColumn.getType()) {
            case Constants.TYPE_COLUMN_BOOK:
                return BookListFragment.newInstance(bookColumn);
            case Constants.TYPE_COLUMN_TAG:
                return TagListFragment_.builder().build();
        }
        return BookListFragment.newInstance(bookColumn);
    }
}
