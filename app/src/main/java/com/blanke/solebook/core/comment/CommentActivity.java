package com.blanke.solebook.core.comment;

import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blanke.solebook.R;
import com.blanke.solebook.adapter.BaseRecyclerAdapter;
import com.blanke.solebook.base.BaseSwipeMvpLceStateActivity;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookComment;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.comment.persenter.CommentPersenter;
import com.blanke.solebook.core.comment.persenter.CommentPersenterImpl;
import com.blanke.solebook.core.comment.view.CommentView;
import com.blanke.solebook.utils.DateUtils;
import com.blanke.solebook.utils.InputModeUtils;
import com.blanke.solebook.utils.ResUtils;
import com.blanke.solebook.utils.SnackUtils;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;
import com.joanzapata.android.recyclerview.BaseAdapterHelper;
import com.neu.refresh.NeuSwipeRefreshLayout;
import com.neu.refresh.NeuSwipeRefreshLayoutDirection;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.qc.stat.common.User;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

@EActivity(R.layout.activity_comment)
public class CommentActivity extends
        BaseSwipeMvpLceStateActivity<LinearLayout, List<BookComment>, CommentView, CommentPersenter>
        implements CommentView, NeuSwipeRefreshLayout.OnRefreshListener {

    @ViewById(R.id.activity_comment_recyclerview)
    FamiliarRecyclerView mRecyclerView;
    @ViewById(R.id.activity_comment_swipelayout)
    NeuSwipeRefreshLayout mSwipeRefreshLayout;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.activity_comment_edit_mycomment)
    EditText mEditText;

    @Extra
    Book book;

    private CommentPersenter mPersenter;
    private int currentPage = 0;
    private int PAGE_COUNT = Constants.PAGE_COUNT;
    private BaseRecyclerAdapter<BookComment> mAdapter;

    @AfterViews
    void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle(book.getTitle());
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new BaseRecyclerAdapter<BookComment>(this, R.layout.item_recyclerview_bookcomment) {
            @Override
            protected void convert(BaseAdapterHelper helper, BookComment item) {
                SoleUser user = item.getUser();
                helper.getTextView(R.id.item_comment_user).setText(user.getNickname());
                helper.getTextView(R.id.item_comment_content).setText(item.getContent());
                helper.getTextView(R.id.item_comment_time)
                        .setText(DateUtils.getTimestampString(item.getCreatedAt()));
                ImageLoader.getInstance()
                        .displayImage(user.getIconurl()
                                , helper.getImageView(R.id.item_comment_icon));
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                BookComment toComment = mAdapter.getItem(position);
                mEditText.setFocusable(true);
                mEditText.setFocusableInTouchMode(true);
                mEditText.requestFocus();
                InputModeUtils.openInputMode(mEditText);
//                InputModeUtils.toggleInputMode(CommentActivity.this);
                mEditText.setHint("回复" + toComment.getUser().getNickname());
            }
        });
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @NonNull
    @Override
    public CommentPersenter createPresenter() {
        return mPersenter = new CommentPersenterImpl();
    }

    @Override
    public void setData(List<BookComment> data) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (data == null || data.size() == 0) {
            return;
        }
        if (currentPage == 0) {
            mAdapter.clear();
            mAdapter.addAll(data);
        } else {
            mAdapter.addAll(data);
        }
        currentPage++;
    }

    @Click(R.id.activity_comment_bu_send)
    public void clickSendComment() {
        String t = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(t)) {
            SnackUtils.show(mEditText, R.string.msg_comment_add_empty);
            return;
        }
        InputModeUtils.closeInputMode(mEditText);
        mPersenter.sendBookComment(book, t);
    }

    @Override
    protected void showLightError(String msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        SnackUtils.show(mRecyclerView, msg);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().getBookCommentData(book, pullToRefresh, PAGE_COUNT * currentPage, PAGE_COUNT);
    }

    @Override
    public LceViewState<List<BookComment>, CommentView> createViewState() {
        return new CastedArrayListLceViewState<>();
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

    @Override
    public List<BookComment> getData() {
        return mAdapter.getData();
    }

    @Override
    public void showMsg(String msg) {
        showLightError(msg);
    }

    @Override
    public void sendSuccess() {
        showLightError(ResUtils.getResString(this, R.string.msg_comment_send_ok));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
