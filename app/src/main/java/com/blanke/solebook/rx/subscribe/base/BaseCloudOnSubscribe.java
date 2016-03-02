package com.blanke.solebook.rx.subscribe.base;

import com.avos.avoscloud.AVQuery;
import com.blanke.solebook.constants.Constants;


import rx.Observable;
import rx.Subscriber;


/**
 * Created by Blanke on 16-3-2.
 */
public abstract class BaseCloudOnSubscribe<T> implements Observable.OnSubscribe<T> {
    protected AVQuery.CachePolicy cachePolicy = AVQuery.CachePolicy.CACHE_ELSE_NETWORK;
    protected long maxCacheAge = -1;
    protected long detaly = Constants.DELAY_NETWORK;

    public BaseCloudOnSubscribe() {
    }

    public BaseCloudOnSubscribe(AVQuery.CachePolicy cachePolicy) {
        this.cachePolicy = cachePolicy;
    }

    public BaseCloudOnSubscribe(AVQuery.CachePolicy cachePolicy, long maxCacheAge, long detaly) {
        this.cachePolicy = cachePolicy;
        this.maxCacheAge = maxCacheAge;
        this.detaly = detaly;
    }

    protected AVQuery prepare(AVQuery query) {
        return query.setCachePolicy(cachePolicy)
                .setMaxCacheAge(maxCacheAge);
    }

    /**
     * 具体的逻辑实现，必须是同步的
     *
     * @return
     * @throws Exception
     */
    protected abstract T execute() throws Exception;

    /**
     * 模板方法 先调用功能逻辑，在sleep再根据 调用subscri的next或error
     *
     * @param subscriber
     */
    @Override
    public void call(Subscriber<? super T> subscriber) {
        try {
            T re = execute();
            doDetaly();
            subscriber.onNext(re);
        } catch (Exception e) {
            e.printStackTrace();
            subscriber.onError(e);
        }
    }

    protected void doDetaly() throws InterruptedException {
        Thread.sleep(detaly);
    }

    public AVQuery.CachePolicy getCachePolicy() {
        return cachePolicy;
    }

    public void setCachePolicy(AVQuery.CachePolicy cachePolicy) {
        this.cachePolicy = cachePolicy;
    }

    public long getMaxCacheAge() {
        return maxCacheAge;
    }

    public void setMaxCacheAge(long maxCacheAge) {
        this.maxCacheAge = maxCacheAge;
    }

    public long getDetaly() {
        return detaly;
    }

    public void setDetaly(long detaly) {
        this.detaly = detaly;
    }
}
