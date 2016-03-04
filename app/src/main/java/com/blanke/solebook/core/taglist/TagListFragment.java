package com.blanke.solebook.core.taglist;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.blanke.solebook.R;
import com.blanke.solebook.base.BaseColumnFragment;
import com.blanke.solebook.bean.Tag;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.details.DetailsActivity_;
import com.blanke.solebook.core.taglist.persenter.TagListPersenter;
import com.blanke.solebook.core.taglist.persenter.TagListPersenterImpl;
import com.blanke.solebook.core.taglist.view.TagListView;
import com.blanke.solebook.utils.SnackUtils;
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
 * Tag  fragment
 * Created by Blanke on 16-2-22.
 */
@EFragment(R.layout.fragment_tag_item)
public class TagListFragment extends BaseColumnFragment<SwipeRefreshLayout, List<Tag>, TagListView, TagListPersenter>
        implements TagListView, NeuSwipeRefreshLayout.OnRefreshListener {
    private static final long LAZY_DELAY_TIME = Constants.LAZY_DELAY_TIME;
    @ViewById(R.id.fragment_columnitem_recyclerview)
    FamiliarRecyclerView mRecyclerView;

    @ViewById(R.id.contentView)
    NeuSwipeRefreshLayout mSwipeRefreshLayout;
    private TagItemAdapter mAdapter;

    private int currentPage = 0;
    private int PAGE_COUNT = Constants.PAGE_COUNT;

    private boolean isCreateView = false, isVisible = false, isNetworkFinish = false;

    public String getTitle() {
        return mCurrentTagColumn.getName();
    }

    public static TagListFragment newInstance(TagColumn TagColumn) {
        TagListFragment fragment = new TagListFragment_();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_TagCOLUMN, TagColumn);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        KLog.d(mCurrentTagColumn.getName() + hashCode());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        KLog.d(mCurrentTagColumn.getName() + hashCode());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        KLog.d(mCurrentTagColumn.getName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KLog.d(mCurrentTagColumn.getName() + hashCode());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int screenWidth = newConfig.screenWidthDp;
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(screenWidth / 150 + 1, OrientationHelper.VERTICAL));
    }

    @AfterViews
    void init() {
        mAdapter = new TagItemAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
//        KLog.d(isNetworkFinish);
        isCreateView = true;
        lazyLoad();
        mRecyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                Tag Tag = mAdapter.getTags().get(position);
//                DetailsActivity_.intent(getActivity()).Tag(Tag).start();
                DetailsActivity_.start(getActivity(),
                        (ImageView) view.findViewById(R.id.item_Tag_image), Tag);
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

//    @Override
//    public LceViewState<List<Tag>, TagListView> createViewState() {
//        return new CastedArrayListLceViewState<>();
//    }

    //    @Override
    public List<Tag> getData() {
        return mAdapter.getTags() == null ? null : new ArrayList<>(mAdapter.getTags());
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
        KLog.d(mCurrentTagColumn.getName());
        mSwipeRefreshLayout.setRefreshing(false);
        if (data == null || data.size() == 0) {
            return;
        }
        if (currentPage == 0) {
            mAdapter.setData(data);
            mRecyclerView.smoothScrollToPosition(0);
        } else {
            mAdapter.addData(data);
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
        getPresenter().getTagData(mCurrentTagColumn, pullToRefresh, PAGE_COUNT * currentPage, PAGE_COUNT);
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
