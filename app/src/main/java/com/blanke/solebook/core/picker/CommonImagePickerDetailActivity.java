package com.blanke.solebook.core.picker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.blanke.solebook.R;
import com.blanke.solebook.adapter.ViewHolderHelper;
import com.blanke.solebook.base.BaseActivity;
import com.blanke.solebook.bean.ImageItem;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_common_image_picker_detail)
public class CommonImagePickerDetailActivity extends BaseActivity {

    public static final String KEY_BUNDLE_RESULT_IMAGE_PATH = "KEY_BUNDLE_RESULT_IMAGE_PATH";

    @ViewById(R.id.common_image_picker_detail_grid_view)
    GridView commonImagePickerDetailGridView;

    private BaseAdapter mAdapter;
    private List<ImageItem> mGridListData = null;


    protected void getBundleExtras(Bundle extras) {
        mGridListData = extras.getParcelableArrayList(CommonImagePickerListActivity
                .KEY_BUNDLE_ALBUM_PATH);

        String title = extras.getString(CommonImagePickerListActivity.KEY_BUNDLE_ALBUM_NAME);
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
    }


    @AfterViews
    protected void init() {

        mAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return mGridListData == null ? 0 : mGridListData.size();
            }

            @Override
            public Object getItem(int position) {
                return mGridListData.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(CommonImagePickerDetailActivity.this)
                            .inflate(R.layout.grid_item_common_image_picker, parent, false);
                }
                ImageView mItemImage = ViewHolderHelper.get(convertView, R.id.grid_item_common_image_picker_image);
                ImageItem itemData = mGridListData.get(position);
                if (null != itemData) {
                    String imagePath = itemData.getImagePath();
                    if (!TextUtils.isEmpty(imagePath)) {
                        ImageLoader.getInstance().displayImage("file://" + imagePath, mItemImage);
                    }
                }
                return convertView;
            }
        };
        commonImagePickerDetailGridView.setAdapter(mAdapter);
        commonImagePickerDetailGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(KEY_BUNDLE_RESULT_IMAGE_PATH, mGridListData.get(position).getImagePath());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
