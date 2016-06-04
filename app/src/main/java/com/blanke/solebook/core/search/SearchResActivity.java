package com.blanke.solebook.core.search;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blanke.solebook.R;
import com.blanke.solebook.adapter.BaseRecyclerAdapter;
import com.blanke.solebook.base.BaseMvpLceViewStateActivity;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.details.DetailsActivity;
import com.blanke.solebook.core.search.persenter.SearchResPersenter;
import com.blanke.solebook.core.search.persenter.SearchResPersenterImpl;
import com.blanke.solebook.core.search.view.SearchResView;
import com.blanke.solebook.utils.StatusBarCompat;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;
import com.joanzapata.android.recyclerview.BaseAdapterHelper;
import com.joanzapata.android.recyclerview.BaseQuickAdapter;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.changeskin.SkinManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

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
    private BaseRecyclerAdapter<Book> mAdapter;
    private int page_count = Constants.PAGE_COUNT;

    @AfterViews
    void init() {
        SkinManager.getInstance().register(this);
        applyTheme(null);
        mAdapter = new BaseRecyclerAdapter<Book>(this, R.layout.item_searchres_book) {
            @Override
            protected void convert(BaseAdapterHelper helper, Book book) {
                helper.getTextView(R.id.item_search_title).setText(book.getTitle());
                helper.getTextView(R.id.item_search_info).setText(book.getIntroContent());
                ImageLoader.getInstance()
                        .displayImage(book.getImgL(), helper.getImageView(R.id.item_search_image), Constants.getImageOptions());
            }
        };
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startDetails((ImageView) view.findViewById(R.id.item_search_image),
                        books.get(position));
            }
        });
    }

    public void setStatusBarColor() {
        int c = SkinManager.getInstance().getResourceManager().getColor(Constants.RES_COLOR_STATUSBAR);
        StatusBarCompat.setStatusBarColor(this, c);
    }

    private void applyTheme(Object o) {
        setStatusBarColor();
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
        if (data == null) {
            return;
        }
        int size = data.size();
        if (size == 1) {//当搜索结果只有一个时，直接跳转到该book的详情页
            startDetails(null, data.get(0));
            finish();
        } else if (size == 0) {
            Toast.makeText(SearchResActivity.this, R.string.msg_search_none, Toast.LENGTH_LONG).show();
            finish();
        } else {
            this.books = data;
            mAdapter.addAll(books);
        }
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().getSearchRes(pullToRefresh, page_count, key);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unregister(this);
    }
}
