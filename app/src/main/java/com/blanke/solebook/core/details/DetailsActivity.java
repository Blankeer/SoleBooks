package com.blanke.solebook.core.details;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blanke.solebook.R;
import com.blanke.solebook.base.BaseSwipeBackActivity;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.bookimage.BookImageActivity_;
import com.blanke.solebook.core.comment.CommentActivity_;
import com.blanke.solebook.core.details.persenter.DetailsPersenter;
import com.blanke.solebook.core.details.persenter.DetailsPersenterImpl;
import com.blanke.solebook.core.details.view.DetailsView;
import com.blanke.solebook.utils.BitmapUtils;
import com.blanke.solebook.utils.DialogUtils;
import com.blanke.solebook.utils.FastBlur;
import com.blanke.solebook.utils.SystemUiUtils;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_details)
public class DetailsActivity extends BaseSwipeBackActivity implements DetailsView {

    public static final String ARG_NAME_BEAN = "DetailsActivity_bean";
    @ViewById(R.id.activity_details_img)
    ImageView mIcon;
    @ViewById(R.id.activity_details_text_book_info)
    ExpandableTextView mBookTextInfo;
    @ViewById(R.id.activity_details_text_dir)
    ExpandableTextView mBookTextDir;
    @ViewById(R.id.toolbar2)
    Toolbar toolbar;
    @ViewById(R.id.activity_details_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @ViewById(R.id.activity_details_appbar)
    AppBarLayout mAppBarLayout;
    @ViewById(R.id.activity_details_like)
    LikeButton mLikeButton;
    @ViewById(R.id.activity_details_coordlayout)
    CoordinatorLayout mCoordinatorLayout;

    private DetailsPersenter mPersenter;
    @Extra
    Book book;
    private double h = 0;
    @ViewById(R.id.activity_details_text_author)
    TextView activity_details_text_author;
    @ViewById(R.id.activity_details_text_publisher)
    TextView activity_details_text_publisher;
    @ViewById(R.id.activity_details_text_pubdate)
    TextView activity_details_text_pubdate;
    @ViewById(R.id.activity_details_text_page)
    TextView activity_details_text_page;
    @ViewById(R.id.activity_details_text_price)
    TextView activity_details_text_price;
    @ViewById(R.id.activity_details_text_binding)
    TextView activity_details_text_binding;
    @ViewById(R.id.activity_details_text_isbn)
    TextView activity_details_text_isbn;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void start(Activity activity, ImageView imageview, Book book) {
        Intent intent2 = new Intent(activity, DetailsActivity_.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_NAME_BEAN, book);
        intent2.putExtras(bundle);
        if (imageview != null) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                    imageview, activity.getResources().getString(R.string.share_img));
            activity.startActivity(intent2, options.toBundle());
        } else {
            activity.startActivity(intent2);
        }
    }

    @AfterViews
    void init() {
        Bundle bundle = getIntent().getExtras();
        book = bundle.getParcelable(ARG_NAME_BEAN);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (SystemUiUtils.checkDeviceHasNavigationBar(this)) {//判断是否有navigationbar
            mCoordinatorLayout.setPadding(0, 0, 0, SystemUiUtils.getNavigationBarHeight(this));
        }
        mCollapsingToolbarLayout.setTitle(book.getTitle());
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.Toolbar_expanded_text);//展开后的字体大小等

        ImageLoader.getInstance().displayImage(book.getImgL(), mIcon, Constants.getImageOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                onImageComplete(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        activity_details_text_author.setText(book.getAuthor());
        activity_details_text_publisher.setText(book.getPublisher());
        activity_details_text_pubdate.setText(book.getPubdate());
        activity_details_text_page.setText(book.getPages());
        activity_details_text_price.setText(book.getPrice());
        activity_details_text_binding.setText(book.getBinding());
        activity_details_text_isbn.setText(book.getIsbn());
        mBookTextInfo.setText(book.getTitle() + "\n" + book.getIntroContent());
        mBookTextDir.setText(book.getDir());
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                h = mCollapsingToolbarLayout.getMeasuredHeight()
                        - mCollapsingToolbarLayout.getMinimumHeight();
                h *= 0.9;
                float a = (float) ((h + verticalOffset) / h);
//                KLog.d(h + "," + verticalOffset + "," + a);
                mIcon.setAlpha(a);
            }
        });
        mLikeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                mPersenter.setLike(true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                mPersenter.setLike(false);
            }
        });


        mPersenter = new DetailsPersenterImpl(this, book);
        mPersenter.initLikeState();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void onImageComplete(Bitmap bitmap) {
        mCollapsingToolbarLayout.setBackground(new BitmapDrawable(getResources(),
                FastBlur.doBlur(BitmapUtils.addBlackBitmap(bitmap), 80, false)));
    }

    //    @Click(R.id.activity_details_book_info)
//    public void clickBookInfo() {
//        mBookTextInfo.toggle();
//    }
    @Click(R.id.activity_details_img)
    public void clickImage() {
        BookImageActivity_.start(this, mIcon, book.getImgL(), book.getTitle());
    }

    @Click(R.id.activity_details_dir)
    public void clickDir() {
        DialogUtils.show(this, R.string.title_dir, book.getDir().split("\n"));
    }

    @Click(R.id.activity_details_text_author)
    public void clickAuthor() {
        DialogUtils.show(this, R.string.title_author, book.getIntroAuthor().split("\n"));
    }

    @Click(R.id.activity_details_comment)
    public void clickCommentr() {
        CommentActivity_.intent(this).book(book).start();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setLike(boolean isLike) {
        mLikeButton.setLiked(isLike);
    }

}
