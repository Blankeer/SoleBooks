package com.blanke.solebook.core.comment;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blanke.solebook.R;
import com.blanke.solebook.adapter.BaseRecyclerAdapter;
import com.blanke.solebook.base.BaseSwipeMvpLceStateActivity;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookComment;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.comment.persenter.CommentPersenter;
import com.blanke.solebook.core.comment.persenter.CommentPersenterImpl;
import com.blanke.solebook.core.comment.view.CommentView;
import com.blanke.solebook.utils.SnackUtils;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;
import com.joanzapata.android.recyclerview.BaseAdapterHelper;
import com.joanzapata.android.recyclerview.QuickAdapter;
import com.neu.refresh.NeuSwipeRefreshLayout;
import com.neu.refresh.NeuSwipeRefreshLayoutDirection;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

@EActivity(R.layout.activity_comment)
public class CommentActivity extends
        BaseSwipeMvpLceStateActivity<SwipeRefreshLayout, List<BookComment>, CommentView, CommentPersenter>
        implements CommentView, NeuSwipeRefreshLayout.OnRefreshListener {

    @ViewById(R.id.activity_comment_recyclerview)
    FamiliarRecyclerView mRecyclerView;
    @ViewById(R.id.contentView)
    NeuSwipeRefreshLayout mSwipeRefreshLayout;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @Extra
    Book book;

    private int currentPage = 0;
    private int PAGE_COUNT = Constants.PAGE_COUNT;
    private BaseRecyclerAdapter mAdapter;

    @AfterViews
    void init() {
        setSupportActionBar(toolbar);
        toolbar.setTitle(book.getTitle());
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new BaseRecyclerAdapter<BookComment>(this, R.layout.item_recyclerview_bookcomment) {
            @Override
            protected void convert(BaseAdapterHelper helper, BookComment item) {
//                helper.getTextView(R.id.item_tag_title).setText(item.getName());
            }
        };
        mRecyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {

            }
        });
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @NonNull
    @Override
    public CommentPersenter createPresenter() {
        return new CommentPersenterImpl();
    }

    @Override
    public void setData(List<BookComment> data) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (data == null || data.size() == 0) {
            return;
        }
        if (currentPage == 0) {
            mAdapter.clear();
            mAdapter.addAll(data);
        } else {
            mAdapter.addAll(data);
        }
        currentPage++;
    }

    @Override
    protected void showLightError(String msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        SnackUtils.show(mRecyclerView, msg);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().getBookCommentData(book, pullToRefresh, PAGE_COUNT * currentPage, PAGE_COUNT);
    }

    @Override
    public LceViewState<List<BookComment>, CommentView> createViewState() {
        return new CastedArrayListLceViewState<>();
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

    @Override
    public List<BookComment> getData() {
        return mAdapter.getData();
    }
}
