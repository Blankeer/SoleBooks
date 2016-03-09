package com.blanke.solebook.core.search;

import android.media.Image;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blanke.solebook.R;
import com.blanke.solebook.adapter.SearchResAdapter;
import com.blanke.solebook.base.BaseMvpLceViewStateActivity;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.details.DetailsActivity;
import com.blanke.solebook.core.search.persenter.SearchResPersenter;
import com.blanke.solebook.core.search.persenter.SearchResPersenterImpl;
import com.blanke.solebook.core.search.view.SearchResView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;

/**
 * Created by Blanke on 16-2-26.
 */
@EActivity(R.layout.activity_searchres)
public class SearchResActivity extends BaseMvpLceViewStateActivity<LinearLayout, List<Book>, SearchResView, SearchResPersenter>
        implements SearchResView {
    @ViewById(R.id.fragment_searchres_recyclerview)
    RecyclerViewPager mRecyclerView;

    private List<Book> books;
    @Extra
    String key;
    private SearchResAdapter mAdapter;
    private int page_count = Constants.PAGE_COUNT;

    @AfterViews
    void init() {
        mAdapter = new SearchResAdapter(this);
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
//            @Override
//            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
//                startDetails((ImageView) view.findViewById(R.id.item_search_image),
//                        books.get(position));
//            }
//        });
    }

    private void startDetails(ImageView imageView, Book b) {
        DetailsActivity.start(SearchResActivity.this, imageView, b);
    }

    @Override
    public LceViewState<List<Book>, SearchResView> createViewState() {
        return new CastedArrayListLceViewState<>();
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
    public SearchResPersenter createPresenter() {
        return new SearchResPersenterImpl();
    }

    @Override
    public void setData(List<Book> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        if (data.size() == 1) {
            startDetails(null, data.get(0));
            finish();
        } else {
            this.books = data;
            mAdapter.setData(data);
        }
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().getSearchRes(pullToRefresh, page_count, key);
    }
}
