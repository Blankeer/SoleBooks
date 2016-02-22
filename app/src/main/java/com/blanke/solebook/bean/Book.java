package com.blanke.solebook.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by Blanke on 16-2-22.
 */
@AVClassName("Book")
public class Book extends AVObject {
    public static final String TITLE = "title";

    public String getTitle() {
        return getString(TITLE);
    }

}
