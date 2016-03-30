package com.blanke.solebook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blanke.solebook.R;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.constants.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blanke on 16-2-26.
 */
public class RandomAdapter extends BaseAdapter {
    private Context context;
    private List<Book> books;

    public RandomAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    public RandomAdapter(Context context) {
        this.context = context;
        books = new ArrayList<>();
    }

    public void addBooks(List<Book> books) {
        this.books.addAll(books);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        books.remove(position);
        notifyDataSetChanged();
    }

    public List<Book> getBooks() {
        return books;
    }

    @Override
    public int getCount() {
        return books == null ? 0 : books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_random_book, parent, false);
        } else {
//            KLog.d(position + " view not null");
        }
//        KLog.d(position + ":" + convertView.toString());
        Integer lastPosition = (Integer) convertView.getTag();
//        if (lastPosition != null && lastPosition == position) {
//            KLog.d("old:"+position);
//            return convertView;
//        }
//        KLog.d(position);
//        convertView.setTag(position);
        ImageView imageView = ViewHolderHelper.get(convertView, R.id.item_random_image);
        TextView textViewTitle = ViewHolderHelper.get(convertView, R.id.item_random_title);
        TextView textViewInfo = ViewHolderHelper.get(convertView, R.id.item_random_info);
        Book item = books.get(position);
        textViewTitle.setText(item.getTitle());
        textViewInfo.setText(item.getIntroContent());
        ImageLoader.getInstance().displayImage(item.getImgL(), imageView, Constants.getImageOptions());
        return convertView;
    }
}
