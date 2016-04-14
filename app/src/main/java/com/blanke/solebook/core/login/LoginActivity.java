package com.blanke.solebook.core.login;

import android.view.View;

import com.avos.avoscloud.AVAnonymousUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.blanke.solebook.R;
import com.blanke.solebook.app.SoleApplication;
import com.blanke.solebook.base.BaseActivity;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.core.main.MainActivity_;
import com.blanke.solebook.utils.ResUtils;
import com.blanke.solebook.utils.SnackUtils;
import com.blanke.solebook.utils.StatusBarCompat;
import com.socks.library.KLog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

/**
 * Created by blanke on 16-2-21.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements PlatformActionListener {

    @ViewById(R.id.contentView)
    View contentView;
    @ViewById(R.id.loadingView)
    View loadView;
    @ViewById(R.id.activity_login_bu_sina)
    View mSinaBt;
    @ViewById(R.id.activity_login_bu_qq)
    View mQQBt;

    private String type;

    @AfterViews
    public void init() {
        KLog.d("111111111111111111");
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorAccent));
        SoleApplication.getApplication(this).init();
        mSinaBt.setVisibility(View.VISIBLE);
        mQQBt.setVisibility(View.VISIBLE);
    }

    @Click(R.id.activity_login_bu_sina)
    void loginSina() {
        loading(true);
        type = AVUser.AVThirdPartyUserAuth.SNS_SINA_WEIBO;
        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.SSOSetting(false);  //设置false表示使用SSO授权方式
        weibo.setPlatformActionListener(this); // 设置分享事件回调
        weibo.authorize();
    }

    @Click(R.id.activity_login_bu_qq)
    void loginQQ() {
        loading(true);
        type = AVUser.AVThirdPartyUserAuth.SNS_TENCENT_WEIBO;
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.SSOSetting(false);  //设置false表示使用SSO授权方式
        qq.setPlatformActionListener(this); // 设置分享事件回调
        qq.authorize();
    }

    @Click(R.id.activity_login_bu_anonymous)
    void anonymous() {
        AVAnonymousUtils.logIn(new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser user, AVException e) {
                KLog.i("AVAnonymous  login success ");
                jumpMain();
            }
        });
    }

    private void loading(boolean isshow) {
        contentView.setVisibility(isshow ? View.GONE : View.VISIBLE);
        loadView.setVisibility(!isshow ? View.GONE : View.VISIBLE);
    }

    private void onError(String msg) {
        SnackUtils.show(loadView, msg);
        loading(false);
    }

    private void onNext(SoleUser soleUser) {
        KLog.json(soleUser.toString());
        //SnackUtils.show(getWindow().getDecorView(), soleUser.toString());
        jumpMain();
    }

    private void jumpMain() {
        MainActivity_.intent(this).start();
        this.finish();
    }

    @Override
    public void onComplete(Platform plat, int i, HashMap<String, Object> hashMap) {
        AVUser.AVThirdPartyUserAuth auth =
                new AVUser.AVThirdPartyUserAuth(plat.getDb().getToken(), String.valueOf(plat.getDb()
                        .getExpiresTime()), type, plat.getDb()
                        .getUserId());
        SoleUser.loginWithAuthData(SoleUser.class, auth, new LogInCallback<SoleUser>() {
            @Override
            public void done(SoleUser user, AVException e) {
                if (e == null) {
//                    KLog.json(plat.getDb().exportData());
                    user.setNickname(plat.getDb().getUserName());
                    String iconUrl = plat.getDb().getUserIcon();
                    if (iconUrl.endsWith("40")) {
                        StringBuffer sb = new StringBuffer(iconUrl.substring(0, iconUrl.length() - 2));
                        sb.append("100");
                        iconUrl = sb.toString();
                    }
                    user.setIconurl(iconUrl);
                    user.setDeviceId(AVInstallation.getCurrentInstallation().getInstallationId());
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            onNext(user);
                        }
                    });
                } else {
                    e.printStackTrace();
                    onError(e.getMessage());
                }
            }
        });
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        onError(throwable.getMessage());
    }

    @Override
    public void onCancel(Platform platform, int i) {
        onError(ResUtils.getResString(this, R.string.msg_login_cancel));
    }
}
