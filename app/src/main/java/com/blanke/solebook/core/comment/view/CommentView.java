package com.blanke.solebook.core.comment.view;

import com.blanke.solebook.bean.BookComment;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

/**
 * Created by Blanke on 16-3-22.
 */
public interface CommentView extends MvpLceView<List<BookComment>> {
    public void showMsg(String msg);

    public void sendSuccess();

    public void deleteFinish(Exception e);
}
