package com.blanke.solebook.manager;

import com.blanke.solebook.base.BaseColumnFragment;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.columnitem.ColumnItemFragment;

/**
 * Created by Blanke on 16-2-23.
 */
public class ColumnManager {

    public static BaseColumnFragment getColumnFragment(BookColumn bookColumn) {
        switch (bookColumn.getType()) {
            case Constants.TYPE_COLUMN_BOOK:
                return ColumnItemFragment.newInstance(bookColumn);
        }
        return ColumnItemFragment.newInstance(bookColumn);
    }
}
