package com.blanke.solebook.core.nearmap.persenter;

import com.avos.avoscloud.AVGeoPoint;
import com.blanke.solebook.core.nearmap.view.NearMapView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by Blanke on 16-2-26.
 */
public abstract class NearMapPersenter extends MvpBasePresenter<NearMapView> {
    public abstract void getNearFriend(boolean pullToRefresh, AVGeoPoint location, int size);
}
