package com.blanke.solebook.app;

import android.app.Application;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.PushService;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.bean.BookComment;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.bean.Tag;
import com.blanke.solebook.bean.UserBookLike;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.welcome.WelcomeActivity_;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.analytics.AnalyticsConfig;
import com.zhy.changeskin.SkinManager;

/**
 * Created by Blanke on 16-2-19.
 */
public class SoleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initAvos();
        initUM();
        initImageLoader();
        initSkin();
    }

    private void initUM() {
        AnalyticsConfig.setAppkey(this, Constants.APPKEY_UM);
        AnalyticsConfig.setChannel("test");
        AnalyticsConfig.enableEncrypt(true);
    }

    private void initAvos() {
        AVUser.alwaysUseSubUserClass(SoleUser.class);
        AVObject.registerSubclass(Book.class);
        AVObject.registerSubclass(Tag.class);
        AVObject.registerSubclass(BookComment.class);
        AVObject.registerSubclass(BookColumn.class);
        AVObject.registerSubclass(UserBookLike.class);
        AVOSCloud.setDebugLogEnabled(true);
        AVCloud.setProductionMode(false);
        AVAnalytics.setAppChannel("test");
        AVOSCloud.initialize(this, Constants.APPID_AVOS, Constants.APPKEY_AVOS);
        AVInstallation.getCurrentInstallation().saveInBackground();
        PushService.setDefaultPushCallback(this, WelcomeActivity_.class);
    }

    private void initSkin() {
        SkinManager.getInstance().init(this);
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
//                .memoryCacheExtraOptions(500,700)
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 10 * 1000))
//                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }
}
