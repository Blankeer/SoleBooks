package com.blanke.solebook.rx;

import com.avos.avoscloud.AVUser;
import com.blanke.solebook.rx.subscribe.AVUserLoginOnSubscribe;
import com.blanke.solebook.utils.RxUtils;

import rx.Observable;

/**
 * Created by Blanke on 16-2-20.
 */
public class RxAVUser {
    public Observable<AVUser> login(String username, String password) {
        return RxUtils.schedulerNewThread(Observable.create(new AVUserLoginOnSubscribe(username, password)));
    }
}
