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
    public static final String REPLY = "reply";

    public void setContent(String content) {
        put(CONTENT, content);
    }

    public void setBook(Book book) {
        put(BOOK, book);
    }

    public void setUser(SoleUser user) {
        put(USER, user);
    }

    public void setReply(BookComment comment) {
        put(REPLY, comment);
    }

    public String getContent() {
        return getString(CONTENT);
    }

    public SoleUser getUser() {
        try {
            SoleUser re = getAVObject(USER, SoleUser.class);
            return re;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Book getBook() {
        try {
            return getAVObject(BOOK, Book.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BookComment getReply() {
        try {
            return getAVObject(REPLY, BookComment.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
