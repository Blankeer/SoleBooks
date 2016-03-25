package com.blanke.solebook.core.bookimage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blanke.solebook.R;
import com.blanke.solebook.base.BaseActivity;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.utils.BitmapUtils;
import com.blanke.solebook.utils.ResUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.lang.ref.WeakReference;

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

    @AfterViews
    public void init() {
        ImageLoader.getInstance().displayImage(url, mImageView, Constants.getImageOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                BookImageActivity.this.bitmap = bitmap;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mImageView.getLayoutParams();
                params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                params.height = params.width;
                mImageView.setLayoutParams(params);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setOnLongClickListener(v -> {
            new MsgDialog(bitmap, mImageView, bookName, ResUtils.getResString(BookImageActivity.this, R.string.msg_down_img))
                    .show(getSupportFragmentManager(), "dialog");
            return false;
        });
        mAttacher.setOnViewTapListener((view, x, y) -> onBackPressed());
    }

    static class MsgDialog extends DialogFragment {
        String title;
        Bitmap bitmap;
        WeakReference<ImageView> imageViewWeakReference;
        String name;

        public MsgDialog(Bitmap bitmap, ImageView imageView, String name, String title) {
            this.bitmap = bitmap;
            this.title = title;
            this.name = name;
            imageViewWeakReference = new WeakReference<>(imageView);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            ImageView imageView = imageViewWeakReference.get();
            if (imageView == null) {
                return null;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(title);
            builder.setPositiveButton("确定", (dialog, id) -> {
                BitmapUtils.savaImage(imageView.getContext(), bitmap, name)
                        .subscribe(aBoolean -> {
                            if (imageView != null) {
                                Snackbar.make(imageView, aBoolean ? R.string.msg_down_img_ok : R.string.msg_down_img_error, Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        });
                dialog.dismiss();
            });
            builder.setNegativeButton("取消", (dialog, id) -> {
                dialog.dismiss();
            });
            return builder.create();
        }
    }

}
