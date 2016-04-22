package com.blanke.solebook.core.userhome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.blanke.solebook.R;
import com.blanke.solebook.adapter.BaseRecyclerAdapter;
import com.blanke.solebook.base.BaseFragment;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookComment;
import com.blanke.solebook.bean.UserBookLike;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.details.DetailsActivity;
import com.blanke.solebook.core.userhome.persenter.NewlyCommentPersenterImpl;
import com.blanke.solebook.core.userhome.persenter.NewlyLikePersenterImpl;
import com.blanke.solebook.core.userhome.persenter.UserNewlyPersenter;
import com.blanke.solebook.core.userhome.view.UserNewlyView;
import com.blanke.solebook.utils.DateUtils;
import com.blanke.solebook.utils.ResUtils;
import com.blanke.solebook.utils.SkinUtils;
import com.joanzapata.android.recyclerview.BaseAdapterHelper;
import com.neu.refresh.NeuSwipeRefreshLayout;
import com.neu.refresh.NeuSwipeRefreshLayoutDirection;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.changeskin.SkinManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by Blanke on 16-3-28.
 */
@EFragment(R.layout.fragment_userhome_newly)
public class UserNewlyFragment extends BaseFragment
        implements NeuSwipeRefreshLayout.OnRefreshListener, UserNewlyView {
    private static String ARG_POSITION = "UserNewlyFragment_ARG_POSITION";
    private static String ARG_USERID = "UserNewlyFragment_ARG_USERID";
    private int position;
    @ViewById(R.id.fragment_newly_swipelayout)
    NeuSwipeRefreshLayout mSwipeRefreshLayout;
    @ViewById(R.id.fragment_newly_recyclerview)
    FamiliarRecyclerView mRecyclerView;

    private BaseRecyclerAdapter mAdapter;
    private UserNewlyPersenter mPersenter;
    private String userId;
    private int currentPage = 0;
    private int count = Constants.PAGE_COUNT;

    public static UserNewlyFragment newInstance(int position, String userId) {
        UserNewlyFragment fragment = new UserNewlyFragment_();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POSITION, position);
        bundle.putString(ARG_USERID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        position = args.getInt(ARG_POSITION);
        userId = args.getString(ARG_USERID);
    }

    public void changeTheme(Object o) {
        mSwipeRefreshLayout.setProgressBackgroundColor(SkinUtils.getLoadProgressColorId(getContext()));
    }

    @AfterViews
    public void init() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        changeTheme(null);
        initAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                Object o = mAdapter.getItem(position);
                Book book = null;
                if (o instanceof UserBookLike) {
                    UserBookLike like = (UserBookLike) o;
                    book = like.getBook();
                } else if (o instanceof BookComment) {
                    BookComment comment = (BookComment) o;
                    book = comment.getBook();
                }
                DetailsActivity.start(getActivity(), (ImageView) view.findViewById(R.id.item_newly_booklike_img), book);
            }
        });
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
        mSwipeRefreshLayout.postDelayed(() -> mSwipeRefreshLayout.autoRefresh(), 100);
    }

    private void initAdapter() {
        if (position == 0) {
            mPersenter = new NewlyLikePersenterImpl(this);
            mAdapter = new BaseRecyclerAdapter<UserBookLike>(getActivity(), R.layout.item_newly_booklike) {
                @Override
                protected void convert(BaseAdapterHelper helper, UserBookLike item) {
                    ImageView img = helper.getImageView(R.id.item_newly_booklike_img);
                    ImageLoader.getInstance().displayImage(item.getBook().getImgL(), img, Constants.getImageOptions());
                    helper.getTextView(R.id.item_newly_booklike_title).setText(item.getBook().getTitle());
                    helper.getTextView(R.id.item_newly_booklike_time).setText(DateUtils.getTimestampString(item.getUpdatedAt()));
                    SkinManager.getInstance().injectSkin(img.getRootView());
                }
            };
        } else {
            mPersenter = new NewlyCommentPersenterImpl(this);
            mAdapter = new BaseRecyclerAdapter<BookComment>(getActivity(), R.layout.item_newly_booklike) {
                @Override
                protected void convert(BaseAdapterHelper helper, BookComment item) {
                    ImageView img = helper.getImageView(R.id.item_newly_booklike_img);
                    ImageLoader.getInstance().displayImage(item.getBook().getImgL(), img, Constants.getImageOptions());
                    helper.getTextView(R.id.item_newly_booklike_title)
                            .setText(item.getBook().getTitle());
                    helper.getTextView(R.id.item_newly_booklike_time)
                            .setText(DateUtils.getTimestampString(item.getUpdatedAt()));
                    helper.getTextView(R.id.item_newly_booklike_content)
                            .setText(ResUtils.getResString(getActivity(), R.string.title_comment)
                                    + ":" + item.getContent());
                    SkinManager.getInstance().injectSkin(img.getRootView());
                }
            };
        }
    }

    @Override
    public void onRefresh(NeuSwipeRefreshLayoutDirection neuSwipeRefreshLayoutDirection) {
        mPersenter.loadData(userId, currentPage * count, count);
    }


    @Override
    public void setLikeData(List<UserBookLike> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        mAdapter.addAll(data);
        currentPage++;
    }

    @Override
    public void setCommentData(List<BookComment> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        mAdapter.addAll(data);
        currentPage++;
    }

    @Override
    public void setLoading(boolean isLoading) {
        mSwipeRefreshLayout.setRefreshing(isLoading);
    }
}
