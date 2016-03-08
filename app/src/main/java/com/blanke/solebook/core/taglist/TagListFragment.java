package com.blanke.solebook.core.taglist;

import android.content.res.Configuration;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blanke.solebook.R;
import com.blanke.solebook.base.BaseColumnFragment;
import com.blanke.solebook.bean.Tag;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.details.DetailsActivity_;
import com.blanke.solebook.core.taglist.persenter.TagListPersenter;
import com.blanke.solebook.core.taglist.persenter.TagListPersenterImpl;
import com.blanke.solebook.core.taglist.view.TagListView;
import com.blanke.solebook.utils.SnackUtils;
import com.minimize.android.rxrecycleradapter.RxDataSource;
import com.minimize.android.rxrecycleradapter.SimpleViewHolder;
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
import rx.functions.Action1;

/**
 * Tag  fragment
 * Created by Blanke on 16-2-22.
 */
@EFragment(R.layout.fragment_tag_item)
public class TagListFragment extends BaseColumnFragment<LinearLayout, List<Tag>, TagListView, TagListPersenter>
        implements TagListView {
    @ViewById(R.id.fragment_tag_recyclerview)
    FamiliarRecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private List<Tag> datas;
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int screenWidth = newConfig.screenWidthDp;
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(screenWidth / 150 + 1, OrientationHelper.VERTICAL));
    }

    @AfterViews
    void init() {
        RxDataSource<Tag> rxDataSource=new RxDataSource<>(datas);
        rxDataSource.bindRecyclerView(mRecyclerView,R.layout.item_recyclerview_tag)
                .subscribe(new Action1<SimpleViewHolder<Tag, ViewDataBinding>>() {
                    @Override
                    public void call(SimpleViewHolder<Tag, ViewDataBinding> holder) {
                        ItemLayoutBinding b = viewHolder.getViewDataBinding();
                    }
                });
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

    @Override
    public TagListPersenter createPresenter() {
        return new TagListPersenterImpl();
    }

    @Override
    public void setData(List<Tag> data) {
        datas=data;
    }

    @Override
    protected void showLightError(String msg) {
        SnackUtils.show(mRecyclerView, msg);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
//        getPresenter().getTagData(mCurrentTagColumn, pullToRefresh, PAGE_COUNT * currentPage, PAGE_COUNT);
    }

}
