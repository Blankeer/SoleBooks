package com.blanke.solebook.core.details.persenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.GetCallback;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.bean.UserBookLike;
import com.blanke.solebook.core.details.view.DetailsView;

/**
 * Created by blanke on 16-3-16.
 */
public class DetailsPersenterImpl extends DetailsPersenter {
    private SoleUser user;
    private UserBookLike like;

    public DetailsPersenterImpl(DetailsView view, Book book) {
        super(view, book);
        user = SoleUser.getCurrentUser(SoleUser.class);
    }

    @Override
    public void initLikeState() {
        if (user != null && !user.isAnonymous()) {
            UserBookLike.getQuery(UserBookLike.class)
                    .whereEqualTo(UserBookLike.USER, user)
                    .whereEqualTo(UserBookLike.BOOK, book)
                    .getFirstInBackground(new GetCallback<UserBookLike>() {
                        @Override
                        public void done(UserBookLike userBookLike, AVException e) {
                            if (e == null && userBookLike != null) {
                                like = userBookLike;
                                view.setLike(true);
                            }
                        }
                    });
        }
    }

    @Override
    public void setLike(boolean isLike) {
        if (user != null && !user.isAnonymous()) {
            if (isLike) {
                UserBookLike t = new UserBookLike();
                t.setBook(book);
                t.setUser(user);
                t.saveInBackground();
            } else {
                if (like != null) {
                    like.deleteInBackground();
                }
            }
        }
    }
}
