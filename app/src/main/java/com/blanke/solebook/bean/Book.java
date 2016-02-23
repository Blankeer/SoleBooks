package com.blanke.solebook.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by Blanke on 16-2-22.
 */
@AVClassName("Book")
public class Book extends AVObject {
    public static final String TITLE = "title";
    public static final String IMG_L = "img_l";
    public static final String IMG_M = "img_m";

    public String getTitle() {
        return getString(TITLE);
    }

    public String getImgL() {
        return getString(IMG_L);
    }

    public String getImgM() {
        return getString(IMG_M);
    }

}
