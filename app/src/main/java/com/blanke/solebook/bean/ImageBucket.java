
package com.blanke.solebook.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ImageBucket implements Parcelable {
    public int count;
    public String bucketName;
    public ArrayList<ImageItem> bucketList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.count);
        dest.writeString(this.bucketName);
        dest.writeTypedList(bucketList);
    }

    public ImageBucket() {
    }

    protected ImageBucket(Parcel in) {
        this.count = in.readInt();
        this.bucketName = in.readString();
        this.bucketList = in.createTypedArrayList(ImageItem.CREATOR);
    }

    public static final Creator<ImageBucket> CREATOR = new Creator<ImageBucket>() {
        public ImageBucket createFromParcel(Parcel source) {
            return new ImageBucket(source);
        }

        public ImageBucket[] newArray(int size) {
            return new ImageBucket[size];
        }
    };

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public List<ImageItem> getBucketList() {
        return bucketList;
    }

    public void setBucketList(ArrayList<ImageItem> bucketList) {
        this.bucketList = bucketList;
    }
}
