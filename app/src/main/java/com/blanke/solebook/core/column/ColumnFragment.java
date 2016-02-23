package com.blanke.solebook.core.column;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.blanke.solebook.R;
import com.blanke.solebook.adapter.ColumnFragmentAdapter;
import com.blanke.solebook.base.BaseMvpLceViewStateFragment;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.core.column.persenter.ColumnPersenter;
import com.blanke.solebook.core.column.persenter.ColumnPersenterImpl;
import com.blanke.solebook.core.column.view.ColumnView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by Blanke on 16-2-22.
 */
@EFragment(R.layout.fragment_column)
public class ColumnFragment extends BaseMvpLceViewStateFragment<LinearLayout, List<BookColumn>, ColumnView, ColumnPersenter> implements ColumnView {
    @ViewById(R.id.fragment_column_tablayout)
    TabLayout mTabLayout;
    @ViewById(R.id.fragment_column_viewpager)
    ViewPager mViewPager;

    List<BookColumn> data;
    ColumnFragmentAdapter pageAdapter;

    @AfterViews
    void init() {
        pageAdapter = new ColumnFragmentAdapter(getChildFragmentManager());
    }

    @Override
    public LceViewState<List<BookColumn>, ColumnView> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<BookColumn> getData() {
        return data;
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @Override
    public ColumnPersenter createPresenter() {
        return new ColumnPersenterImpl();
    }

    @Override
    public void setData(List<BookColumn> data) {
        this.data = data;
        for (BookColumn item : data) {
            mTabLayout.addTab(mTabLayout.newTab().setText(item.getName()));
            pageAdapter.addTab(item);
        }
        mViewPager.setOffscreenPageLimit(data.size());
        mViewPager.setAdapter(pageAdapter);
        if (data.size() > 1) {
            mTabLayout.setVisibility(View.VISIBLE);
            mTabLayout.setupWithViewPager(mViewPager);
        } else {
            mTabLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().getColumnData(pullToRefresh);
    }


}
