package com.blanke.solebook.core.login;

import android.content.Intent;
import android.view.View;

import com.avos.avoscloud.AVAnonymousUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.sns.SNS;
import com.avos.sns.SNSType;
import com.blanke.solebook.R;
import com.blanke.solebook.base.BaseActivity;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.main.MainActivity_;
import com.blanke.solebook.rx.RxSNS;
import com.blanke.solebook.utils.SnackUtils;
import com.blanke.solebook.utils.StatusBarCompat;
import com.socks.library.KLog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

/**
 * Created by blanke on 16-2-21.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewById(R.id.contentView)
    View contentView;
    @ViewById(R.id.loadingView)
    View loadView;
    private SNSType type;

    JSONObject authorData = null;

    @AfterViews
    public void init(){
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.colorAccent));
    }

    @Click(R.id.activity_login_bu_sina)
    void loginSina() {
        loading(true);
        type = SNSType.AVOSCloudSNSSinaWeibo;
        RxSNS.snsLogin(this, type, Constants.APPID_SINA,
                Constants.APPSEC_SINA, Constants.REDIRECTURL_SINA)
                .subscribe(this::onNext, this::onError);
    }

    @Click(R.id.activity_login_bu_qq)
    void loginQQ() {
        loading(true);
        type = SNSType.AVOSCloudSNSQQ;
        RxSNS.snsLogin(this, type, Constants.APPID_QQ,
                Constants.APPSEC_QQ, Constants.REDIRECTURL_QQ)
                .subscribe(this::onNext, this::onError);
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

    private void onError(Throwable throwable) {
        KLog.d(throwable.getMessage());
        SnackUtils.show(getWindow().getDecorView(), throwable.getMessage());
        loading(false);
    }

    private void onNext(SoleUser soleUser) {
        KLog.json(soleUser.toString());
        SnackUtils.show(getWindow().getDecorView(), soleUser.toString());
        jumpMain();
    }

    private void jumpMain() {
        MainActivity_.intent(this).start();
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SNS.onActivityResult(requestCode, resultCode, data, type);
    }
}
