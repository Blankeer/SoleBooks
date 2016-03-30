package com.blanke.solebook.core.welcome;

import android.graphics.Color;
import android.widget.ImageView;

import com.blanke.solebook.R;
import com.blanke.solebook.base.BaseActivity;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.core.login.LoginActivity_;
import com.blanke.solebook.core.main.MainActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@Fullscreen
@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivity {

    @ViewById(R.id.activity_welcome_imageview)
    ImageView mImageView;

    private final static long DELAY = 0;

    @AfterViews
    void init() {
        mImageView.setBackgroundColor(Color.BLACK);
        if (isLogin()) {
            jumpMain();
        } else {
            jumpLogin();
        }
    }

    private boolean isLogin() {
        return SoleUser.getCurrentUser() != null;
    }

    @UiThread(delay = DELAY)
    void jumpLogin() {
        LoginActivity_.intent(this).start();
        finish();
    }

    @UiThread(delay = DELAY)
    void jumpMain() {
        MainActivity_.intent(this).start();
        finish();
    }
}
