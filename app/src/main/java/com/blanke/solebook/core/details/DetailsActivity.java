package com.blanke.solebook.core.details;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blanke.solebook.R;
import com.blanke.solebook.base.BaseActivity;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.main.MainActivity;
import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.kogitune.activity_transition.ExitActivityTransition;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.socks.library.KLog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

@EActivity(R.layout.activity_details)
public class DetailsActivity extends BaseActivity {

    public static final String ARG_NAME_BEAN = "DetailsActivity_bean";
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
    private ExitActivityTransition exitTransition;
    private Bundle savedInstanceState;


    public static void start(Activity activity, View view, Book book) {
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
//                imageview, activity.getResources().getString(R.string.share_img));
        Intent intent2 = new Intent(activity, DetailsActivity_.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_NAME_BEAN, book);
        intent2.putExtras(bundle);
//        activity.startActivity(intent2, options.toBundle());
        ActivityTransitionLauncher.with(activity).from(view).launch(intent2);
    }

    @AfterViews
    void init() {
        exitTransition = ActivityTransition.with(getIntent()).to(mIcon).start(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        book = bundle.getParcelable(ARG_NAME_BEAN);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        mCollapsingToolbarLayout.setTitle(book.getTitle());
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.Toolbar_expanded_text);//展开后的字体大小等

        ImageLoader.getInstance().displayImage(book.getImgL(), mIcon, Constants.getImageOptions());
        mBookTextInfo.setText(book.getIntroContent());
        mAuthorTextInfo.setText(book.getIntroAuthor());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState=savedInstanceState;
    }

    @Override
    public void onBackPressed() {
        exitTransition.exit(this);
    }
}
