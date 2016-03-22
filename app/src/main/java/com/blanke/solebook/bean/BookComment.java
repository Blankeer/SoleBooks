package com.blanke.solebook.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by Blanke on 16-3-22.
 */
@AVClassName("BookComment")
public class BookComment extends AVObject {
    public static final String CONTENT = "content";
    public static final String BOOK = "book";
    public static final String USER = "user";

    public void setContent(String content) {
        put(CONTENT, content);
    }

    public void setBook(Book book) {
        put(BOOK, book);
    }

    public void setUser(SoleUser user) {
        put(USER, user);
    }
}
