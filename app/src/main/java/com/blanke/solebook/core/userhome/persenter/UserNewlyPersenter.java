package com.blanke.solebook.core.userhome.persenter;

import com.blanke.solebook.core.userhome.view.UserNewlyView;

import java.lang.ref.WeakReference;

/**
 * Created by Blanke on 16-3-28.
 */
public abstract class UserNewlyPersenter {
    protected WeakReference<UserNewlyView> view;

    public UserNewlyPersenter(UserNewlyView view) {
        this.view = new WeakReference<UserNewlyView>(view);
    }

    abstract public void loadData(String userId, int skip, int limit);
}
