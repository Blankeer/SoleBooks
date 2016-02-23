package com.blanke.solebook.core.columnitem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;

import com.blanke.solebook.R;
import com.blanke.solebook.adapter.BookItemAdapter;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.columnitem.persenter.ColumnItemPersenter;
import com.blanke.solebook.core.columnitem.persenter.ColumnItemPersenterImpl;
import com.blanke.solebook.core.columnitem.view.ColumnItemView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import com.neu.refresh.NeuSwipeRefreshLayout;
import com.neu.refresh.NeuSwipeRefreshLayoutDirection;
import com.socks.library.KLog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import cn.iwgang.familiarrecyclerview.FamiliarRecyclerViewOnScrollListener;

/**
 * Created by Blanke on 16-2-22.
 */
@EFragment(R.layout.fragment_column_item)
public class ColumnItemFragment extends MvpLceViewStateFragment<SwipeRefreshLayout, List<Book>, ColumnItemView, ColumnItemPersenter>
        implements ColumnItemView, NeuSwipeRefreshLayout.OnRefreshListener {
    public static final String ARG_BOOKCOLUMN = "mBookColumn";

    @ViewById(R.id.fragment_columnitem_recyclerview)
    FamiliarRecyclerView mRecyclerView;

    @ViewById(R.id.contentView)
    NeuSwipeRefreshLayout mSwipeRefreshLayout;

    private BookColumn mBookColumn;
    private List<Book> books;
    private BookItemAdapter mAdapter;

    private int currentPage = 0;
    private int PAGE_COUNT = Constants.PAGE_COUNT;

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
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Click(R.id.fab)
    void fab() {
        KLog.d();
        mSwipeRefreshLayout.autoRefresh();
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
        if (currentPage == 0) {
            mAdapter.setData(books);
        } else {
            mAdapter.addData(books);
        }
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
        currentPage++;
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().getBookData(mBookColumn, pullToRefresh, PAGE_COUNT * currentPage, PAGE_COUNT);
    }

    @Override
    public void onRefresh(NeuSwipeRefreshLayoutDirection direction) {
        if (direction == NeuSwipeRefreshLayoutDirection.TOP) {
            currentPage = 0;
            loadData(true);
            KLog.d();
        } else if (direction == NeuSwipeRefreshLayoutDirection.BOTTOM) {
            KLog.d();
            loadData(true);
        }
    }
}
