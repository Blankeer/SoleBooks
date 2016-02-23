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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blanke on 16-2-22.
 */
public class BookItemAdapter extends RecyclerView.Adapter<BookViewHolder> {
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
    }

    public void addData(List<Book> data) {
        if (books == null) {
            books = new ArrayList<>();
        }
        this.books.addAll(data);
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
                .displayImage(book.getImgL(), holder.imageView, Constants.getImageOptions());
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }

}

class BookViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textView;

    public BookViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.item_book_image);
        textView = (TextView) itemView.findViewById(R.id.item_book_title);
    }
}
