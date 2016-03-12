package com.blanke.solebook;

import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.test.InstrumentationTestCase;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.blanke.solebook.test.TestCurstomImageviewActivity;
import com.socks.library.KLog;

/**
 * Created by Blanke on 16-2-29.
 */
public class CurstomImageviewTest extends InstrumentationTestCase {
    private Instrumentation instrumentation;
    private TestCurstomImageviewActivity testActitity;
    int i=0;
    @Override
    public void setUp() throws Exception {
        super.setUp();
        instrumentation = getInstrumentation();
    }

    public void testName() throws Exception {
        testActitity = launchActivity("com.blanke.solebook", TestCurstomImageviewActivity.class, null);
//        new Handler().postAtFrontOfQueue()
        Thread.sleep(10000);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        KLog.d("activity  test end   什么鬼shenmegui");
        Thread.sleep(10000);
    }
}
