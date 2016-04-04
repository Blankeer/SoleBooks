package com.blanke.solebook.core.nearmap.persenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.blanke.solebook.bean.SoleUser;

import java.util.List;

/**
 * Created by blanke on 16-4-3.
 */
public class NearMapPersenterImpl extends NearMapPersenter {
    @Override
    public void getNearFriend(boolean pullToRefresh, AVGeoPoint location, int size) {
        if (location == null) {
            return;
        }
        getView().showLoading(pullToRefresh);
        AVQuery<SoleUser> query = AVUser.getQuery(SoleUser.class);
        query.whereNear(SoleUser.LOCATION, location);
        query.setLimit(size);
        query.findInBackground(new FindCallback<SoleUser>() {
            @Override
            public void done(List<SoleUser> list, AVException e) {
                if (getView() != null) {
                    getView().setData(list);
                    getView().showContent();
                }
            }
        });
    }
}
