package com.blanke.solebook.core.random;

import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.blanke.solebook.R;
import com.blanke.solebook.adapter.RandomAdapter;
import com.blanke.solebook.base.BaseMvpLceViewStateActivity;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.random.persenter.RandomPersenter;
import com.blanke.solebook.core.random.persenter.RandomPersenterImpl;
import com.blanke.solebook.core.random.view.RandomView;
import com.blanke.solebook.utils.StatusBarCompat;
import com.blanke.solebook.view.flingswipe.SwipeFlingAdapterView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by Blanke on 16-2-26.
 */
@EActivity(R.layout.fragment_random)
public class RandomActivity extends BaseMvpLceViewStateActivity<LinearLayout, List<Book>, RandomView, RandomPersenter>
        implements RandomView {
    @ViewById(R.id.fragment_random_swipeview)
    SwipeFlingAdapterView mSwipeFlingAdapterView;

    private RandomAdapter mAdapter;

    private int page_count = Constants.PAGE_COUNT;
    private int page = 0;

    @AfterViews
    void init() {
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorAccent));
        mAdapter = new RandomAdapter(this);
        mSwipeFlingAdapterView.setAdapter(mAdapter);
        mSwipeFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                mAdapter.remove(0);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
            }

            @Override
            public void onRightCardExit(Object dataObject) {
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                loadData(true);
            }

            @Override
            public void onScroll(float progress, float scrollXProgress) {

            }

        });
        mSwipeFlingAdapterView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(MotionEvent event, View v, Object dataObject) {

            }

        });
    }

    @Override
    public LceViewState<List<Book>, RandomView> createViewState() {
        return new CastedArrayListLceViewState<>();
    }

    @Override
    public List<Book> getData() {
        return mAdapter.getBooks();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @Override
    public RandomPersenter createPresenter() {
        return new RandomPersenterImpl();
    }

    @Override
    public void setData(List<Book> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        mAdapter.addBooks(data);
        page++;
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().getSearchRes(pullToRefresh, page_count);
    }
}
