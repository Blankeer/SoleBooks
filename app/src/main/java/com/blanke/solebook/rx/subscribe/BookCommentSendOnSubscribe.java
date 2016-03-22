package com.blanke.solebook.rx.subscribe;

import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookComment;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.rx.subscribe.base.BaseCloudOnSubscribe;

import java.util.List;

/**
 * 发送评论
 * Created by Blanke on 16-3-2.
 */
public class BookCommentSendOnSubscribe extends BaseCloudOnSubscribe<List<Book>> {
    private Book book;
    private SoleUser user;
    private String content;

    public BookCommentSendOnSubscribe(Book book, SoleUser user, String content) {
        this.book = book;
        this.user = user;
        this.content = content;
    }

    @Override
    protected List<Book> execute() throws Exception {
        BookComment bookComment=new BookComment();
        bookComment.setBook(book);
        bookComment.setContent(content);
        bookComment.setUser(user);
        bookComment.save();
        return null;
    }
}
