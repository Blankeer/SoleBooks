package com.blanke.solebook.core.details;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.blanke.solebook.R;
import com.blanke.solebook.base.BaseActivity;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.constants.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.socks.library.KLog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_details)
public class DetailsActivity extends BaseActivity {

    @ViewById(R.id.activity_details_img)
    ImageView mIcon;
    @ViewById(R.id.activity_details_book_info)
    TextView mBookTextInfo;
    @ViewById(R.id.activity_details_author_info)
    TextView mAuthorTextInfo;
    @ViewById(R.id.toolbar2)
    Toolbar toolbar;
    @ViewById(R.id.activity_details_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;


    @Extra
    Book book;

    @AfterViews
    void init() {
        KLog.d(book);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        mCollapsingToolbarLayout.setTitle(book.getTitle());
//        setTitle(book.getTitle());
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.Toolbar_expanded_text);

        ImageLoader.getInstance().displayImage(book.getImgL(), mIcon, Constants.getImageOptions());
        mBookTextInfo.setText(book.getIntroContent());
        mAuthorTextInfo.setText(book.getIntroAuthor());
    }
}
