package com.blanke.solebook.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVRelation;

/**
 * Created by Blanke on 16-2-22.
 */
@AVClassName("BookColumn")
public class BookColumn extends AVObject {
    public static final String NAME = "name";
    public static final String BOOKS = "books";

    public String getName() {
        return getString(NAME);
    }

    public AVRelation<Book> getBooks() {
        return getRelation(BOOKS);
    }
}
