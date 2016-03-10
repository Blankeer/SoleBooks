package com.blanke.solebook.utils;

import android.graphics.Bitmap;

/**
 * Created by Blanke on 16-3-10.
 */
public class BitmapUtils {
    public static Bitmap addBlackBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int array[] = new int[w * h];
        int n = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) { //从上往下扫描
                int color = bitmap.getPixel(j, i);
                color &= 0xFFD0D0D0;
                array[n] = color;
                n++;
            }
        }
        return Bitmap.createBitmap(array, w, h, Bitmap.Config.ARGB_8888);
    }
}
