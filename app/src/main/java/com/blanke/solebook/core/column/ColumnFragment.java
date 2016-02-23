package com.blanke.solebook.core.column;

import android.os.Bundle;
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
 * 模块fragment  包括  榜单  发现 收藏 等
 * Created by Blanke on 16-2-22.
 */
@EFragment(R.layout.fragment_column)
public class ColumnFragment extends BaseMvpLceViewStateFragment<LinearLayout, List<BookColumn>, ColumnView, ColumnPersenter> implements ColumnView {
    public static final String ARGS_BOOKCOLUMN = "ColumnFragment_mCurrentBookColumn";
    @ViewById(R.id.fragment_column_tablayout)
    TabLayout mTabLayout;
    @ViewById(R.id.fragment_column_viewpager)
    ViewPager mViewPager;

    List<BookColumn> subBookColumn;
    ColumnFragmentAdapter pageAdapter;
    BookColumn mCurrentBookColumn;

    public static ColumnFragment newInstance(BookColumn bookColumn) {
        ColumnFragment_ fragment = new ColumnFragment_();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_BOOKCOLUMN, bookColumn);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentBookColumn = getArguments().getParcelable(ARGS_BOOKCOLUMN);
    }

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
        return subBookColumn;
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
        this.subBookColumn = data;
        if (data == null || data.size() == 0) {
            return;
        }
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
        getPresenter().getColumnData(mCurrentBookColumn, pullToRefresh);
    }


}
