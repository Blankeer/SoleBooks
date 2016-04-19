package com.blanke.solebook.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.blanke.solebook.R;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Blanke on 16-3-10.
 */
public class BitmapUtils {
    private static final int blackColor = 0xEEEEEEEE;

    /**
     * 适当加黑,避免全白
     *
     * @param bitmap
     * @return
     */
    public static Bitmap addBlackBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int array[] = new int[w * h];
        int n = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) { //从上往下扫描
                int color = bitmap.getPixel(j, i);
                int alp = color >> 24;
                if (alp == 0) {//透明
                    color |= blackColor;
                } else {
                    color &= blackColor;
                }
                array[n] = color;
                n++;
            }
        }
        return Bitmap.createBitmap(array, w, h, Bitmap.Config.ARGB_8888);
    }

    public static String getPhotoPath(Context context) {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + ResUtils.getResString(context, R.string.app_name);
    }

    public static Observable<Boolean> savaImage(Context context, Bitmap bitmap, String fileName) {
        return Observable.just(fileName).map(s -> {
            try {
                return savaImageInNewThread(context, bitmap, s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private static boolean savaImageInNewThread(Context context, Bitmap bitmap, String fileName) throws IOException {
        File f = new File(getPhotoPath(context), fileName);
        KLog.d(f.getAbsolutePath());
        if (!f.exists()) {
            FileOutputStream out = null;
            f.getParentFile().mkdirs();
            out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        }
        return true;
    }
}
