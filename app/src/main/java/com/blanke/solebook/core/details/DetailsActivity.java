package com.blanke.solebook.core.details;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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
import com.blanke.solebook.utils.AnimUtils;
import com.blanke.solebook.utils.BitmapUtils;
import com.blanke.solebook.utils.DialogUtils;
import com.blanke.solebook.utils.SkinUtils;
import com.blanke.solebook.utils.StatusBarCompat;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhy.changeskin.SkinManager;

import net.qiujuer.genius.blur.StackBlur;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


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
    @ViewById(R.id.activity_details_theme)
    ImageView mThemeImg;
    private int textColor, textBackground;//theme
    private boolean isNight;

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
        SkinManager.getInstance().register(this);
        EventBus.getDefault().register(this);
        StatusBarCompat.translucentStatusBar(this);
        applyTheme(null);
//        if (SystemUiUtils.checkDeviceHasNavigationBar(this) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//判断是否有navigationbar
//            mCoordinatorLayout.setPadding(0, 0, 0, SystemUiUtils.getNavigationBarHeight(this));
//        }
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

    private void initThemeImg() {
        isNight = SkinManager.getInstance().needChangeSkin();
        if (isNight) {//现在是夜间模式
            mThemeImg.setImageResource(R.drawable.icon_theme_day);
        } else {
            mThemeImg.setImageResource(R.drawable.icon_theme_night);
        }
    }

    @Subscriber(tag = Constants.EVENT_THEME_CHANGE)
    public void applyTheme(Object o) {
        textColor = SkinManager.getInstance().getResourceManager().getColor(Constants.RES_COLOR_TEXT);
        textBackground = SkinManager.getInstance().getResourceManager().getColor(Constants.RES_COLOR_TEXT_B);
        initThemeImg();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void onImageComplete(Bitmap bitmap) {
        mCollapsingToolbarLayout.setBackground(new BitmapDrawable(getResources(),
                StackBlur.blurNativelyPixels(BitmapUtils.addBlackBitmap(bitmap), Constants.BLUE_VALUE, false)));
    }

    @Click(R.id.activity_details_img)
    public void clickImage() {
        BookImageActivity_.start(this, mIcon, book.getImgL(), book.getTitle());
    }

    @Click(R.id.activity_details_theme)
    public void clickTheme() {
        AnimUtils.toggleTheme(mThemeImg,
                isNight ? R.drawable.icon_theme_day : R.drawable.icon_theme_night
                , () -> SkinUtils.toggleTheme());
    }

    @Click(R.id.activity_details_text_author)
    public void clickAuthor() {
        DialogUtils.show(this, R.string.title_author, textColor, textBackground, book.getIntroAuthor().split("\n"));
    }

    @Click(R.id.activity_details_comment)
    public void clickCommentr() {
        CommentActivity_.intent(this).book(book).start();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unregister(this);
        EventBus.getDefault().unregister(this);
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
}
