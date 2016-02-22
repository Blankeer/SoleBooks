package com.blanke.solebook.rx;

import android.app.Activity;

import com.avos.sns.SNSType;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.rx.subscribe.SNSOnSubscribe;
import com.blanke.solebook.utils.RxUtils;

import rx.Observable;

/**
 * Created by blanke on 16-2-22.
 */
public class RxSNS {
    public static Observable<SoleUser> snsLogin(Activity activity, SNSType type, String appid, String appsec, String redirecturl) {
        return RxUtils.schedulerNewThread(
                Observable.create(
                        new SNSOnSubscribe(activity, type, appid, appsec, redirecturl)));
    }
}
