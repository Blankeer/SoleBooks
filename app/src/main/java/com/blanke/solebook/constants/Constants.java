package com.blanke.solebook.constants;

/**
 * Created by blanke on 16-2-21.
 */
public class Constants {
    public static final String APPID_SINA = "1550326405";
    public static final String APPID_QQ = "1105114711";
    public static final String APPSEC_QQ = "uI7mVrh73fpKMuua";
    public static final String APPSEC_SINA = "8d766414552a564a15bc4db2bbacc437";
    public static final String REDIRECTURL_SINA = "https://leancloud.cn/1.1/sns/callback/1k4u78lawq2pvrjr";
    public static final String REDIRECTURL_QQ = "https://leancloud.cn/1.1/sns/callback/lljmk2jy0vqme5vh";
    public static final long DAY_AGE = 24 * 60 * 60 * 1000;
    public static final int PAGE_COUNT = 20;

    public static String getSinaIconUrl(String uid) {
        return "http://tp1.sinaimg.cn/" + uid + "/180/0/1";
    }
}
