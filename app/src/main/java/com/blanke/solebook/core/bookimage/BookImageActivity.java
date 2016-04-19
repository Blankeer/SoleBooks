package com.blanke.solebook.core.bookimage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blanke.solebook.R;
import com.blanke.solebook.base.BaseActivity;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.utils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Blanke on 16-3-23.
 */
@EActivity(R.layout.activity_book_image)
public class BookImageActivity extends BaseActivity {
    private static final String ARG_NAME_URL = "BookImageActivity_url";
    private static final String ARG_NAME_BOOKNAME = "BookImageActivity_bookname";
    private String url;
    @ViewById(R.id.activity_img_img)
    ImageView mImageView;
    private Bitmap bitmap;
    private String bookName;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void start(Activity activity, ImageView imageview, String url, String bookName) {
        Intent intent2 = new Intent(activity, BookImageActivity_.class);
        Bundle bundle = new Bundle();
        bundle.putString(ARG_NAME_URL, url);
        bundle.putString(ARG_NAME_BOOKNAME, bookName);
        intent2.putExtras(bundle);
        if (imageview != null) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                    imageview, activity.getResources().getString(R.string.share_img2));
            activity.startActivity(intent2, options.toBundle());
        } else {
            activity.startActivity(intent2);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle bundle = getIntent().getExtras();
        url = bundle.getString(ARG_NAME_URL);
        bookName = bundle.getString(ARG_NAME_BOOKNAME);
    }

    private void saveImage(String fileName) {
        BitmapUtils.savaImage(this, bitmap, fileName)
                .subscribe(aBoolean -> {
                    if (mImageView != null) {
                        Snackbar.make(mImageView, aBoolean ? R.string.msg_down_img_ok : R.string.msg_down_img_error, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    @AfterViews
    public void init() {
        ImageLoader.getInstance().displayImage(url, mImageView, Constants.getImageOptions(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                onBitmapComplete(loadedImage);
            }
        });
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setOnLongClickListener(v -> {
            new MaterialDialog.Builder(this).content(R.string.msg_down_img)
                    .title(R.string.title_hint)
                    .positiveText(R.string.title_confirm)
                    .negativeText(R.string.title_cancel)
                    .onPositive((dialog, which) -> saveImage(bookName + ".jpg")).show();
            return false;
        });
        mAttacher.setOnViewTapListener((view, x, y) -> onBackPressed());
    }

    private void onBitmapComplete(Bitmap bitmap) {
        BookImageActivity.this.bitmap = bitmap;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mImageView.getLayoutParams();
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        params.height = params.width;
        mImageView.setLayoutParams(params);
    }
}
