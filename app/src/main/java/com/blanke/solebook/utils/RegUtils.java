package com.blanke.solebook.utils;

/**
 * Created by Blanke on 16-2-20.
 */
public class RegUtils {
    public static final String REG_EMAIL = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+";
    public static final String REG_PHONE = "^1[0-9]{10}$";
    public static final String REG_WORD = "\\w";
    public static final String REG_WORDS = "\\w+";
    public static final String REG_SMSCODE = "[0-9]{6}";
    public static final String REG_PASSWORD = REG_WORD+"{6,12}";

}
