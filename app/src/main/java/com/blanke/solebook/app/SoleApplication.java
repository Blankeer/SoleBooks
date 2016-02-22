package com.blanke.solebook.app;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.bean.SoleUser;

/**
 * Created by Blanke on 16-2-19.
 */
public class SoleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AVUser.alwaysUseSubUserClass(SoleUser.class);
        AVObject.registerSubclass(Book.class);
        AVObject.registerSubclass(BookColumn.class);
        AVOSCloud.initialize(this, "l8eot9jDXBhCt40q1BPJqH9a-gzGzoHsz", "fAYpLpd3IBwlaiixg0bM20Rm");
        AVOSCloud.setDebugLogEnabled(true);
    }
}
