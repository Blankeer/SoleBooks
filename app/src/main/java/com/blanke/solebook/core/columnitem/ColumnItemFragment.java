package com.blanke.solebook.core.columnitem;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.blanke.solebook.R;
import com.blanke.solebook.adapter.BookItemAdapter;
import com.blanke.solebook.base.BaseColumnFragment;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.columnitem.persenter.ColumnItemPersenter;
import com.blanke.solebook.core.columnitem.persenter.ColumnItemPersenterImpl;
import com.blanke.solebook.core.columnitem.view.ColumnItemView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import com.neu.refresh.NeuSwipeRefreshLayout;
import com.neu.refresh.NeuSwipeRefreshLayoutDirection;
import com.socks.library.KLog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;

/**
 * book  fragment   recyclerview布局  比如 热门榜单 等页面
 * Created by Blanke on 16-2-22.
 */
@EFragment(R.layout.fragment_column_item)
public class ColumnItemFragment extends BaseColumnFragment<SwipeRefreshLayout, List<Book>, ColumnItemView, ColumnItemPersenter>
        implements ColumnItemView, NeuSwipeRefreshLayout.OnRefreshListener {

    @ViewById(R.id.fragment_columnitem_recyclerview)
    FamiliarRecyclerView mRecyclerView;

    @ViewById(R.id.contentView)
    NeuSwipeRefreshLayout mSwipeRefreshLayout;

    private List<Book> books;
    private BookItemAdapter mAdapter;

    private int currentPage = 0;
    private int PAGE_COUNT = Constants.PAGE_COUNT;

    private boolean isCreateView = false, isVisible = false, isNetworkFinish = false;

    public String getTitle() {
        return mCurrentBookColumn.getName();
    }

    public static ColumnItemFragment newInstance(BookColumn bookColumn) {
        ColumnItemFragment fragment = new ColumnItemFragment_();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_BOOKCOLUMN, bookColumn);
        fragment.setArguments(bundle);
        return fragment;
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mCurrentBookColumn = getArguments().getParcelable(ARG_BOOKCOLUMN);
//    }

    @AfterViews
    void init() {
        mAdapter = new BookItemAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        isCreateView = true;
        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = getUserVisibleHint();
        lazyLoad();
    }

    private void lazyLoad() {//fragment懒加载，可见才网络加载
        if (isCreateView && isVisible && !isNetworkFinish) {
            mSwipeRefreshLayout.autoRefresh();
            isNetworkFinish = true;
        }
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
        mSwipeRefreshLayout.setRefreshing(false);
        if (data == null) {
            return;
        }
        books = data;
        if (currentPage == 0) {
            mAdapter.setData(books);
        } else {
            mAdapter.addData(books);
        }
        mAdapter.notifyDataSetChanged();
        currentPage++;
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().getBookData(mCurrentBookColumn, !pullToRefresh, pullToRefresh, PAGE_COUNT * currentPage, PAGE_COUNT);
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
