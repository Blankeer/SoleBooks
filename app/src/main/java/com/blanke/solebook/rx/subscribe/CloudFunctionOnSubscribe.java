package com.blanke.solebook.rx.subscribe;

import com.avos.avoscloud.AVCloud;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.rx.subscribe.base.BaseCloudOnSubscribe;

import java.util.HashMap;

/**
 * Created by Blanke on 16-3-4.
 */
public class CloudFunctionOnSubscribe<T> extends BaseCloudOnSubscribe<T> {
    private HashMap<String, String> params;
    private String cloudFunctionName;

    public CloudFunctionOnSubscribe(String cloudFunctionName, HashMap<String, String> params) {
        this.cloudFunctionName = cloudFunctionName;
        this.params = params;
    }

    @Override
    protected T execute() throws Exception {
        return AVCloud.rpcFunction(cloudFunctionName,params);
    }
}
