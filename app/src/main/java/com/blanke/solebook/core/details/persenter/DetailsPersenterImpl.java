package com.blanke.solebook.core.details.persenter;

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
        user=SoleUser.getCurrentUser(SoleUser.class);
    }

    @Override
    public void initLikeState() {
        if(!user.isAnonymous()){
//            user.getLikes().getQuery().whereContains()
        }
    }
}
