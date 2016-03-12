package com.blanke.solebook.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
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
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blanke on 16-2-22.
 */
public class BookItemAdapter extends RecyclerView.Adapter<BookItemAdapter.BookViewHolder> {
    private Context context;
    private List<Book> books;

    public BookItemAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    public BookItemAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Book> data) {
        this.books = data;
        notifyItemRangeRemoved(0, getItemCount());
        notifyItemRangeInserted(0, data.size());
    }

    public List<Book> getBooks() {
        return books;
    }

    public void addData(List<Book> data) {
        if (books == null) {
            books = new ArrayList<>();
        }
        int temp = getItemCount();
        this.books.addAll(data);
//        notifyItemRangeInserted(temp, data.size());
        notifyDataSetChanged();
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_recyclerview_book, parent, false));
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.textView.setText(book.getTitle());
        ImageLoader.getInstance()
                .displayImage(book.getImgL(), holder.imageView, Constants.getImageOptions()
                        , new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                                new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
//                                    @Override
//                                    public void onGenerated(Palette palette) {
//                                        Palette.Swatch swatch =
//                                                palette.getVibrantSwatch();
//                                        if (swatch != null) {
//                                            holder.textView.setBackgroundColor(swatch.getRgb());
//                                        }
//                                    }
//                                });
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }
                        });
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public BookViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_book_image);
            textView = (TextView) itemView.findViewById(R.id.item_book_title);
        }
    }
}
