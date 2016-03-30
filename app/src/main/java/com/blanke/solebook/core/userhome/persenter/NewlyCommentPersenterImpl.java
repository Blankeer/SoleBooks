package com.blanke.solebook.core.userhome.persenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.blanke.solebook.bean.BookComment;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.core.userhome.view.UserNewlyView;

import java.util.List;

/**
 * Created by Blanke on 16-3-28.
 */
public class NewlyCommentPersenterImpl extends UserNewlyPersenter {
    public NewlyCommentPersenterImpl(UserNewlyView view) {
        super(view);
    }

    @Override
    public void loadData(String userId, int skip, int limit) {
        view.get().setLoading(true);
        try {
            BookComment.getQuery(BookComment.class)
                    .whereEqualTo(BookComment.USER, AVUser.createWithoutData(SoleUser.class, userId))
                    .orderByDescending("updatedAt")
                    .include(BookComment.BOOK)
                    .skip(skip)
                    .limit(limit)
                    .findInBackground(new FindCallback<BookComment>() {
                        @Override
                        public void done(List<BookComment> list, AVException e) {
                            if (view.get() != null) {
                                view.get().setCommentData(list);
                                view.get().setLoading(false);
                            }
                        }
                    });
        } catch (AVException e) {
            e.printStackTrace();
            view.get().setLoading(false);
        }
    }
}
