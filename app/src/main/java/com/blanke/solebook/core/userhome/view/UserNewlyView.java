package com.blanke.solebook.core.userhome.view;

import com.blanke.solebook.bean.BookComment;
import com.blanke.solebook.bean.UserBookLike;

import java.util.List;

/**
 * Created by Blanke on 16-3-28.
 */
public interface UserNewlyView {
    void setLikeData(List<UserBookLike> data);

    void setCommentData(List<BookComment> data);

    void setLoading(boolean isLoading);
}