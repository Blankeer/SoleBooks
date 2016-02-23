package com.blanke.solebook.utils;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.blanke.solebook.constants.Constants;

/**
 * Created by Blanke on 16-2-23.
 */
public class AvosCacheUtils {
    public static <T extends AVObject> AVQuery<T> CacheELseNetwork(AVQuery<T> query) {
        return query.setCachePolicy(AVQuery.CachePolicy.CACHE_ELSE_NETWORK)
                .setMaxCacheAge(Constants.DAY_AGE * 3);
    }

    public static <T extends AVObject> AVQuery<T> NetworkELseCache(AVQuery<T> query) {
        return query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE)
                .setMaxCacheAge(Constants.DAY_AGE * 3);
    }

}
