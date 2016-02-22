package com.blanke.solebook.rx.subscribe;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Blanke on 16-2-20.
 */
public class AVUserLoginOnSubscribe implements Observable.OnSubscribe<AVUser> {
    private String username;
    private String password;

    public AVUserLoginOnSubscribe(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void call(Subscriber<? super AVUser> subscriber) {
        try {
            subscriber.onNext(AVUser.logIn(username, password));
        } catch (AVException e) {
            e.printStackTrace();
            subscriber.onError(e);
        }
    }
}
