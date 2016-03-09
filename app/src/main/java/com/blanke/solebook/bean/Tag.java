package com.blanke.solebook.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by Blanke on 16-3-4.
 */
@AVClassName("Tag")
public class Tag extends AVObject {
    public static final String NAME = "name";

    public String getName() {
        return getString(NAME);
    }
}
