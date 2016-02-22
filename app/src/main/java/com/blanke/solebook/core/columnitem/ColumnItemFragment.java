package com.blanke.solebook.core.columnitem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

import com.blanke.solebook.R;
import com.blanke.solebook.adapter.BookItemAdapter;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.core.columnitem.persenter.ColumnItemPersenter;
import com.blanke.solebook.core.columnitem.persenter.ColumnItemPersenterImpl;
import com.blanke.solebook.core.columnitem.view.ColumnItemView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;

/**
 * Created by Blanke on 16-2-22.
 */
@EFragment(R.layout.fragment_column_item)
public class ColumnItemFragment extends MvpLceViewStateFragment<SwipeRefreshLayout, List<Book>, ColumnItemView, ColumnItemPersenter> implements ColumnItemView {
    public static final String ARG_BOOKCOLUMN = "mBookColumn";

    @ViewById(R.id.fragment_columnitem_recyclerview)
    FamiliarRecyclerView mRecyclerView;

    @ViewById(R.id.contentView)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private BookColumn mBookColumn;
    private List<Book> books;
    private BookItemAdapter mAdapter;


    public String getTitle() {
        return mBookColumn.getName();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBookColumn = getArguments().getParcelable(ARG_BOOKCOLUMN);
    }

    @AfterViews
    void init() {
        mAdapter = new BookItemAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);



    }

    @Override
    public LceViewState<List<Book>, ColumnItemView> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<Book> getData() {
        return books;
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @Override
    public ColumnItemPersenter createPresenter() {
        return new ColumnItemPersenterImpl();
    }

    @Override
    public void setData(List<Book> data) {
        books = data;
        mAdapter.setData(books);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().getBookData(mBookColumn, pullToRefresh);
    }
}
