
package com.blanke.solebook.core.picker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blanke.solebook.R;
import com.blanke.solebook.adapter.ViewHolderHelper;
import com.blanke.solebook.base.BaseActivity;
import com.blanke.solebook.bean.ImageBucket;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_common_image_picker_list)
public class CommonImagePickerListActivity extends BaseActivity {

    private static final int IMAGE_PICKER_DETAIL_REQUEST_CODE = 200;

    public static final String KEY_BUNDLE_ALBUM_PATH = "KEY_BUNDLE_ALBUM_PATH";
    public static final String KEY_BUNDLE_ALBUM_NAME = "KEY_BUNDLE_ALBUM_NAME";

    @ViewById(R.id.common_image_picker_list_view)
    ListView mImagePickerListView;

    @ViewById(R.id.loadingView)
    View loadView;
    private BaseAdapter mAdapter = null;
    private AsyncTask<Void, Void, List<ImageBucket>> mAlbumLoadTask = null;

    private List<ImageBucket> mData;


    @AfterViews
    protected void initViewsAndEvents() {
//        setTitle(getResources().getString(R.string.title_image_picker));
        mData = new ArrayList<>();
        mAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return mData == null ? 0 : mData.size();
            }

            @Override
            public Object getItem(int position) {
                return mData.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(CommonImagePickerListActivity.this)
                            .inflate(R.layout.list_item_common_image_picker, parent, false);
                }
                ImageView mItemImage = ViewHolderHelper.get(convertView, R.id
                        .list_item_common_image_picker_thumbnail);
                TextView mItemTitle = ViewHolderHelper.get(convertView, R.id
                        .list_item_common_image_picker_title);
                ImageBucket itemData = mData.get(position);
                if (null != itemData) {
                    String imagePath = itemData.bucketList.get(0).getImagePath();
                    if (!TextUtils.isEmpty(imagePath)) {
                        ImageLoader.getInstance().displayImage("file://" + imagePath,
                                mItemImage);
                    }
                    int count = itemData.count;
                    String title = itemData.bucketName;
                    if (!TextUtils.isEmpty(title)) {
                        mItemTitle.setText(title + "(" + count + ")");
                    }
                }
                return convertView;
            }
        };
        mImagePickerListView.setAdapter(mAdapter);

        mImagePickerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle extras = new Bundle();
                extras.putParcelableArrayList(KEY_BUNDLE_ALBUM_PATH, mData.get(position).bucketList);
                extras.putString(KEY_BUNDLE_ALBUM_NAME, mData.get(position).bucketName);

//                readyGoForResult(CommonImagePickerDetailActivity.class,
//                        IMAGE_PICKER_DETAIL_REQUEST_CODE, extras);
            }
        });

        mAlbumLoadTask = new AsyncTask<Void, Void, List<ImageBucket>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showLoading(true);
                ImagePickerHelper.getHelper().init(CommonImagePickerListActivity.this);
            }

            @Override
            protected List<ImageBucket> doInBackground(Void... params) {
                return ImagePickerHelper.getHelper().getImagesBucketList();
            }

            @Override
            protected void onPostExecute(List<ImageBucket> list) {
                showLoading(false);
                mData = list;
                mAdapter.notifyDataSetChanged();
            }
        };

        mAlbumLoadTask.execute();
    }

    private void showLoading(boolean isshow) {
        loadView.setVisibility(isshow ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mAlbumLoadTask && !mAlbumLoadTask.isCancelled()) {
            mAlbumLoadTask.cancel(true);
            mAlbumLoadTask = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == IMAGE_PICKER_DETAIL_REQUEST_CODE) {
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
