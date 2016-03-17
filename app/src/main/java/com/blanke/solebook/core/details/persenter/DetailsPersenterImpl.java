package com.blanke.solebook.core.details.persenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.GetCallback;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.core.details.view.DetailsView;

/**
 * Created by blanke on 16-3-16.
 */
public class DetailsPersenterImpl extends DetailsPersenter {
    private SoleUser user;

    public DetailsPersenterImpl(DetailsView view, Book book) {
        super(view, book);
        user = SoleUser.getCurrentUser(SoleUser.class);
    }

    @Override
    public void initLikeState() {
        if (!user.isAnonymous()) {
            user.getLikes().getQuery()
                    .whereEqualTo("objectId", book.getObjectId())
                    .getFirstInBackground(new GetCallback<Book>() {
                        @Override
                        public void done(Book book, AVException e) {
                            if (e == null && book != null) {
                                view.setLike(true);
                            }
                        }
                    });
        }
    }

    @Override
    public void setLike(boolean isLike) {
        if (!user.isAnonymous()) {
            AVRelation<Book> likes = user.getLikes();
            if (isLike) {
                likes.add(book);
            } else {
                likes.remove(book);
            }
            user.saveInBackground();
        }
    }

}
