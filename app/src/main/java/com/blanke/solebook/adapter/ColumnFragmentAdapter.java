package com.blanke.solebook.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.blanke.solebook.base.BaseColumnFragment;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.manager.ColumnManager;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 模块fragment  包括  榜单  发现 收藏 等 的  viewpager  adapter
 * Created by Blanke on 16-2-22.
 */
public class ColumnFragmentAdapter extends FragmentPagerAdapter {
    List<BookColumn> bookColumns;
    SparseArray<BaseColumnFragment> fragments;

    public ColumnFragmentAdapter(FragmentManager fm) {
        super(fm);
        bookColumns = new ArrayList<>();
        fragments = new SparseArray<>();
    }

    public void clear() {
        bookColumns.clear();
        fragments.clear();
    }

    public void addTab(BookColumn bookColumn) {
        bookColumns.add(bookColumn);
    }

    @Override
    public Fragment getItem(int position) {//隐藏bug,ColumnFragmentAdapter#getItem()每次都会重新实例化fragment
        if (fragments.get(position) == null) {
            BaseColumnFragment f = ColumnManager.getColumnFragment(bookColumns.get(position));
            fragments.put(position, f);
        }
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return bookColumns != null ? bookColumns.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return bookColumns.get(position).getName();
    }


}
