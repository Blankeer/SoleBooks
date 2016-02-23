package com.blanke.solebook.core.main.persenter;

import com.blanke.solebook.core.main.view.MainView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by Blanke on 16-2-23.
 */
public abstract class MainPersenter extends MvpBasePresenter<MainView> {
    public abstract void loadBookColumn(boolean pullToRefresh);
}
