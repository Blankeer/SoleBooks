package com.blanke.solebook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class SearchResAdapter extends RecyclerView.Adapter<SearchResAdapter.SearchViewHolder> {
    private Context context;
    private List<Book> books;

    public SearchResAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    public SearchResAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Book> data) {
        this.books = data;
        notifyItemRangeRemoved(0, getItemCount());
        notifyItemRangeInserted(0, data.size());
    }

    public void addData(List<Book> data) {
        if (books == null) {
            books = new ArrayList<>();
        }
        int temp = getItemCount();
        this.books.addAll(data);
        notifyItemRangeInserted(temp, data.size());
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_searchres_book, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        Book book = books.get(position);
        holder.textViewTitle.setText(book.getTitle());
        holder.textViewInfo.setText(book.getIntroContent());
        ImageLoader.getInstance()
                .displayImage(book.getImgL(), holder.imageView, Constants.getImageOptions());
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }


    static class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewTitle, textViewInfo;

        public SearchViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_search_image);
            textViewTitle = (TextView) itemView.findViewById(R.id.item_search_title);
            textViewInfo = (TextView) itemView.findViewById(R.id.item_search_info);
        }
    }
}