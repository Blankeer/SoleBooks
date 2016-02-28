package com.blanke.solebook.core.booklist;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.blanke.solebook.R;
import com.blanke.solebook.adapter.BookItemAdapter;
import com.blanke.solebook.base.BaseColumnFragment;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.booklist.persenter.BookListPersenter;
import com.blanke.solebook.core.booklist.persenter.BookListPersenterImpl;
import com.blanke.solebook.core.booklist.view.BookListView;
import com.blanke.solebook.core.details.DetailsActivity_;
import com.blanke.solebook.utils.SnackUtils;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;
import com.neu.refresh.NeuSwipeRefreshLayout;
import com.neu.refresh.NeuSwipeRefreshLayoutDirection;
import com.socks.library.KLog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * book  fragment   recyclerview布局  比如 热门榜单 等页面
 * Created by Blanke on 16-2-22.
 */
@EFragment(R.layout.fragment_book_item)
public class BookListFragment extends BaseColumnFragment<SwipeRefreshLayout, List<Book>, BookListView, BookListPersenter>
        implements BookListView, NeuSwipeRefreshLayout.OnRefreshListener {
    private static final long LAZY_DELAY_TIME = Constants.LAZY_DELAY_TIME;
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

    public static BookListFragment newInstance(BookColumn bookColumn) {
        BookListFragment fragment = new BookListFragment_();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_BOOKCOLUMN, bookColumn);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        KLog.d(mCurrentBookColumn.getName() + hashCode());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        KLog.d(mCurrentBookColumn.getName() + hashCode());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        KLog.d(mCurrentBookColumn.getName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KLog.d(mCurrentBookColumn.getName() + hashCode());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int screenWidth = newConfig.screenWidthDp;
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(screenWidth / 150 + 1, OrientationHelper.VERTICAL));
    }

    @AfterViews
    void init() {
        mAdapter = new BookItemAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
//        KLog.d(isNetworkFinish);
        isCreateView = true;
        lazyLoad();
        mRecyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                Book book = getData().get(position);
                DetailsActivity_.intent(getActivity()).book(book).start();
            }
        });
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = getUserVisibleHint();
        lazyLoad();
    }

    private void lazyLoad() {//fragment懒加载，可见才网络加载
        if (isCreateView && isVisible && !isNetworkFinish) {
            mSwipeRefreshLayout.postDelayed(() -> mSwipeRefreshLayout.autoRefresh(), LAZY_DELAY_TIME);
            isNetworkFinish = true;
        }
        if (isVisible && isCreateView && fab != null) {
            fab.setOnClickListener(v -> mRecyclerView.smoothScrollToPosition(0));
            fab.attachToRecyclerView(mRecyclerView);
        }
    }

    @Override
    public LceViewState<List<Book>, BookListView> createViewState() {
        return new CastedArrayListLceViewState<>();
    }

    @Override
    public List<Book> getData() {
        return books == null ? null : new ArrayList<>(books);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @Override
    public BookListPersenter createPresenter() {
        return new BookListPersenterImpl();
    }

    @Override
    public void setData(List<Book> data) {
        KLog.d(mCurrentBookColumn.getName());
        mSwipeRefreshLayout.setRefreshing(false);
        if (data == null || data.size() == 0) {
            return;
        }
        books = data;
        if (currentPage == 0) {
            mAdapter.setData(books);
        } else {
            mAdapter.addData(books);
        }
//        mAdapter.notifyDataSetChanged();
        currentPage++;
    }

    protected void showLightError(String msg) {
        SnackUtils.show(mRecyclerView, msg);
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
        } else if (direction == NeuSwipeRefreshLayoutDirection.BOTTOM) {
            loadData(true);
        }
    }
}
