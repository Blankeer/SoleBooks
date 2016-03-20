package com.blanke.solebook.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * Created by Blanke on 16-2-22.
 */
@AVClassName("Book")
public class Book extends AVObject {
    public static final String TABLE_NAME = "Book";
    public static final String TITLE = "title";
    public static final String IMG_L = "img_l";
    public static final String IMG_M = "img_m";
    public static final String INTRO_CONTENT = "intro_content";
    public static final String INTRO_AUTHOR = "intro_author";
    public static final String TAGS = "tags";
    public static final String DIR = "dir";
    public static final String PAGES = "pages";
    public static final String AUTHOR = "author";
    public static final String BINDING = "binding";
    public static final String ISBN = "isbn";
    public static final String PRICE = "price";
    public static final String PUBLISHER = "publisher";
    public static final String PUBDATE = "pubdate";

    public String getTitle() {
        return getString(TITLE);
    }


    public List<String> getTags() {
        return getList(TAGS);
    }

    public String getImgL() {
        return getString(IMG_L);
    }

    public String getImgM() {
        return getString(IMG_M);
    }

    public String getIntroContent() {
        return getString(INTRO_CONTENT);
    }

    public String getIntroAuthor() {
        return getString(INTRO_AUTHOR);
    }

    public String getDir() {
        return getString(DIR);
    }

    public String getPages() {
        return getString(PAGES);
    }

    public String getAuthor() {
        return getString(AUTHOR);
    }

    public String getBinding() {
        return getString(BINDING);
    }

    public String getIsbn() {
        return getString(ISBN);
    }

    public String getPrice() {
        return getString(PRICE);
    }

    public String getPublisher() {
        return getString(PUBLISHER);
    }

    public String getPubdate() {
        return getString(PUBDATE);
    }
}