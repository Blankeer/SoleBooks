package com.blanke.solebook;

import android.app.Instrumentation;
import android.test.InstrumentationTestCase;
import android.view.View;
import android.widget.Toast;

import com.blanke.solebook.test.TestCurstomImageviewActivity;
import com.socks.library.KLog;

/**
 * Created by Blanke on 16-2-29.
 */
public class CurstomImageviewTest extends InstrumentationTestCase {
    private Instrumentation instrumentation;
    private TestCurstomImageviewActivity testActitity;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        instrumentation = getInstrumentation();
        testActitity = launchActivity("com.blanke.solebook", TestCurstomImageviewActivity.class, null);
    }

    public void testName() throws Exception {
        Thread.sleep(10000);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        KLog.d("activity  test end");
        Thread.sleep(10000);
    }
}
