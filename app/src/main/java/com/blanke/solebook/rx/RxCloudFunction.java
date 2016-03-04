package com.blanke.solebook.rx;

import com.blanke.solebook.rx.subscribe.CloudFunctionOnSubscribe;
import com.blanke.solebook.utils.RxUtils;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by Blanke on 16-3-4.
 */
public class RxCloudFunction<T> {
    public Observable<T> executeCloud(String cloudFunctionName, HashMap<String, String> params) {
        return RxUtils.schedulerNewThread(
                Observable.create(new CloudFunctionOnSubscribe<T>(cloudFunctionName, params))
        );
    }
}
