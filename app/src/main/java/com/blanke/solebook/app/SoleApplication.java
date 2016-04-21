package com.blanke.solebook.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.support.multidex.MultiDex;

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
import com.blanke.solebook.core.loadres.LoadResActivity;
import com.blanke.solebook.core.login.LoginActivity_;
import com.blanke.solebook.utils.PackageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.socks.library.KLog;
import com.umeng.analytics.AnalyticsConfig;
import com.zhy.changeskin.SkinManager;

import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Blanke on 16-2-19.
 */
public class SoleApplication extends Application {


    public static final String KEY_DEX2_SHA1 = "dex2-SHA1-Digest";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        KLog.d("loadDex", "App attachBaseContext ");
        if (!quickStart()) {//>=5.0的系统默认对dex进行oat优化
            if (needWait(base)) {
                waitForDexopt(base);
            }
            MultiDex.install(this);
        } else {
            return;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (quickStart()) {
            return;
        }
    }

    public boolean quickStart() {
        if (getCurProcessName(this).contains(":mini")) {
            KLog.d("loadDex", ":mini start!");
            return true;
        }
        return false;
    }

    //neead wait for dexopt ?
    private boolean needWait(Context context) {
        String flag = get2thDexSHA1(context);
        KLog.d("loadDex", "dex2-sha1 " + flag);
        SharedPreferences sp = context.getSharedPreferences(
                PackageUtil.getPackageInfo(context).versionName, MODE_MULTI_PROCESS);
        String saveValue = sp.getString(KEY_DEX2_SHA1, "");
        return !flag.equals(saveValue);
    }

    /**
     * Get classes.dex file signature
     *
     * @param context
     * @return
     */
    private String get2thDexSHA1(Context context) {
        ApplicationInfo ai = context.getApplicationInfo();
        String source = ai.sourceDir;
        try {
            JarFile jar = new JarFile(source);
            Manifest mf = jar.getManifest();
            Map<String, Attributes> map = mf.getEntries();
            Attributes a = map.get("classes2.dex");
            return a.getValue("SHA1-Digest");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // optDex finish
    public void installFinish(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                PackageUtil.getPackageInfo(context).versionName, MODE_MULTI_PROCESS);
        sp.edit().putString(KEY_DEX2_SHA1, get2thDexSHA1(context)).commit();
    }


    public static String getCurProcessName(Context context) {
        try {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                    .getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    public void waitForDexopt(Context base) {
        Intent intent = new Intent(this, LoadResActivity.class);
//        ComponentName componentName = new
//                ComponentName("com.blanke.solebook.core.loadres", "LoadResActivity");
//        intent.setComponent(componentName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        base.startActivity(intent);
        long startWait = System.currentTimeMillis();
        long waitTime = 10 * 1000;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            waitTime = 20 * 1000;//实测发现某些场景下有些2.3版本有可能10s都不能完成optdex
        }
        while (needWait(base)) {
            try {
                long nowWait = System.currentTimeMillis() - startWait;
                KLog.d("loadDex", "wait ms :" + nowWait);
                if (nowWait >= waitTime) {
                    return;
                }
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static SoleApplication getApplication(Context context) {
        return (SoleApplication) context.getApplicationContext();
    }

    public void init() {
        initUM();
        initAvos();
        initImageLoader();
        initSkin();
        ShareSDK.initSDK(this);
    }

    private void initUM() {
        AnalyticsConfig.setChannel("test");
        AnalyticsConfig.enableEncrypt(true);
    }

    private void initAvos() {
        KLog.d();
        AVUser.alwaysUseSubUserClass(SoleUser.class);
        AVObject.registerSubclass(Book.class);
        AVObject.registerSubclass(Tag.class);
        AVObject.registerSubclass(BookComment.class);
        AVObject.registerSubclass(BookColumn.class);
        AVObject.registerSubclass(UserBookLike.class);
        AVOSCloud.setDebugLogEnabled(true);
        AVCloud.setProductionMode(true);
        AVAnalytics.setAppChannel("test");
        AVOSCloud.initialize(this, Constants.APPID_AVOS, Constants.APPKEY_AVOS);
        AVInstallation.getCurrentInstallation().saveInBackground();
        PushService.setDefaultPushCallback(this, LoginActivity_.class);
    }

    private void initSkin() {
        KLog.d();
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
