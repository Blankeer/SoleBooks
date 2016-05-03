package com.blanke.solebook.core.random;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blanke.solebook.R;
import com.blanke.solebook.adapter.RandomAdapter;
import com.blanke.solebook.base.BaseColumnFragment;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.details.DetailsActivity;
import com.blanke.solebook.core.random.persenter.RandomPersenter;
import com.blanke.solebook.core.random.persenter.RandomPersenterImpl;
import com.blanke.solebook.core.random.view.RandomView;
import com.blanke.solebook.view.flingswipe.SwipeFlingAdapterView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by Blanke on 16-3-30.
 */
@EFragment(R.layout.fragment_random)
public class RandomFragment extends BaseColumnFragment<LinearLayout, List<Book>, RandomView, RandomPersenter>
        implements RandomView {
    @ViewById(R.id.fragment_random_swipeview)
    SwipeFlingAdapterView mSwipeFlingAdapterView;

    private RandomAdapter mAdapter;

    private int page_count = Constants.PAGE_COUNT;
    private int page = 0;
    private boolean isFirstNetworkFinish = false;

    public static RandomFragment newInstance(BookColumn bookColumn) {
        RandomFragment fragment = new RandomFragment_();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_BOOKCOLUMN, bookColumn);
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void init() {
        mAdapter = new RandomAdapter(getActivity());
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
                ImageView iv = (ImageView) v.findViewById(R.id.item_random_image);
                DetailsActivity.start(getActivity(), iv, (Book) dataObject);
            }
        });
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
        isFirstNetworkFinish = true;
        mAdapter.addBooks(data);
        page++;
    }


    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().getSearchRes(pullToRefresh, page_count);
    }

    @Override
    protected void lazyLoad() {
        if (!isFirstNetworkFinish) {
            loadData(false);
        }
    }
}
