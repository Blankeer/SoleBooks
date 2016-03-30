package com.blanke.solebook.core.userhome.persenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.bean.UserBookLike;
import com.blanke.solebook.core.userhome.view.UserNewlyView;

import java.util.List;

/**
 * Created by Blanke on 16-3-28.
 */
public class NewlyLikePersenterImpl extends UserNewlyPersenter {
    public NewlyLikePersenterImpl(UserNewlyView view) {
        super(view);
    }

    @Override
    public void loadData(String userId, int skip, int limit) {
        view.get().setLoading(true);
        try {
            UserBookLike.getQuery(UserBookLike.class)
                    .whereEqualTo(UserBookLike.USER, AVUser.createWithoutData(SoleUser.class, userId))
                    .orderByDescending("updatedAt")
                    .include(UserBookLike.BOOK)
                    .skip(skip)
                    .limit(limit)
                    .findInBackground(new FindCallback<UserBookLike>() {
                        @Override
                        public void done(List<UserBookLike> list, AVException e) {
                            if (view.get() != null) {
                                view.get().setLikeData(list);
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
