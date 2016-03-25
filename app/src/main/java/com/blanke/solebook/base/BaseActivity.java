package com.blanke.solebook.base;

import android.support.v7.app.AppCompatActivity;

import com.avos.avoscloud.AVAnalytics;


/**
 * Created by Administrator on 15-12-30.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }

    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
    }
//    @Override
//    public void onContentChanged() {
//        super.onContentChanged();
//        ButterKnife.bind(this);
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//        setContentView(getLayoutRes());
//        init();
//    }

//    abstract protected int getLayoutRes();

//    abstract protected void init();

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        ButterKnife.unbind(this);
////        RefWatcher refWatcher = BaseApplication.getRefWatcher(this);
////        refWatcher.watch(this);
//    }
}
