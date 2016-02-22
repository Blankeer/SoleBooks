package com.blanke.solebook.core.login;

import android.content.Intent;

import com.avos.sns.SNS;
import com.avos.sns.SNSType;
import com.blanke.solebook.R;
import com.blanke.solebook.base.BaseActivity;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.MainActivity_;
import com.blanke.solebook.rx.RxSNS;
import com.blanke.solebook.utils.SnackUtils;
import com.socks.library.KLog;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.json.JSONObject;

/**
 * Created by blanke on 16-2-21.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    private SNSType type;

    JSONObject authorData = null;

    @Click(R.id.activity_login_bu_sina)
    void loginSina() {
        type = SNSType.AVOSCloudSNSSinaWeibo;
        RxSNS.snsLogin(this, type, Constants.APPID_SINA,
                Constants.APPSEC_SINA, Constants.REDIRECTURL_SINA)
                .subscribe(this::onNext, this::onError);
    }

    @Click(R.id.activity_login_bu_qq)
    void loginQQ() {
        type = SNSType.AVOSCloudSNSQQ;
        RxSNS.snsLogin(this, type, Constants.APPID_QQ,
                Constants.APPSEC_QQ, Constants.REDIRECTURL_QQ)
                .subscribe(this::onNext, this::onError);
    }

    private void onError(Throwable throwable) {
        KLog.d(throwable.getMessage());
        SnackUtils.show(getWindow().getDecorView(), throwable.getMessage());
    }

    private void onNext(SoleUser soleUser) {
        KLog.json(soleUser.toString());
        SnackUtils.show(getWindow().getDecorView(), soleUser.toString());
        jumpMain();
    }

    private void jumpMain() {
        MainActivity_.intent(this).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SNS.onActivityResult(requestCode, resultCode, data, type);
    }
}
