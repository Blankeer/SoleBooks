package com.blanke.solebook.core.details.persenter;

import com.blanke.solebook.bean.Book;
import com.blanke.solebook.core.details.view.DetailsView;

/**
 * Created by blanke on 16-3-16.
 */
public abstract class DetailsPersenter {
    protected DetailsView view;
    protected Book book;

    public DetailsPersenter(DetailsView view, Book book) {
        this.view = view;
        this.book = book;
    }

    public abstract void initLikeState();
    public abstract void setLike(boolean isLike);
}
