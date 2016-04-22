package com.blanke.solebook.core.comment;

import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blanke.solebook.R;
import com.blanke.solebook.adapter.BaseRecyclerAdapter;
import com.blanke.solebook.base.BaseSwipeMvpLceStateActivity;
import com.blanke.solebook.bean.Book;
import com.blanke.solebook.bean.BookComment;
import com.blanke.solebook.bean.CommentMenuItem;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.comment.persenter.CommentPersenter;
import com.blanke.solebook.core.comment.persenter.CommentPersenterImpl;
import com.blanke.solebook.core.comment.view.CommentView;
import com.blanke.solebook.core.userhome.UserHomeActivity;
import com.blanke.solebook.utils.DateUtils;
import com.blanke.solebook.utils.DialogUtils;
import com.blanke.solebook.utils.InputModeUtils;
import com.blanke.solebook.utils.ResUtils;
import com.blanke.solebook.utils.SkinUtils;
import com.blanke.solebook.utils.SnackUtils;
import com.blanke.solebook.utils.StatusBarCompat;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;
import com.joanzapata.android.listview.QuickAdapter;
import com.joanzapata.android.recyclerview.BaseAdapterHelper;
import com.neu.refresh.NeuSwipeRefreshLayout;
import com.neu.refresh.NeuSwipeRefreshLayoutDirection;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.socks.library.KLog;
import com.zhy.changeskin.SkinManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
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
    private BookComment reply;
    private SoleUser mUser;

    @AfterViews
    void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        SkinManager.getInstance().register(this);
        EventBus.getDefault().register(this);
        applyTheme(null);
        setTitle(ResUtils.getResString(this, R.string.title_comment));
        mUser = SoleUser.getCurrentUser(SoleUser.class);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new BaseRecyclerAdapter<BookComment>(this, R.layout.item_recyclerview_bookcomment) {
            @Override
            protected void convert(BaseAdapterHelper helper, BookComment item) {
                KLog.json(item.toString());
                final SoleUser user = item.getUser();
                helper.getTextView(R.id.item_comment_user).setText(user.getNickname());
                helper.getTextView(R.id.item_comment_time)
                        .setText(DateUtils.getTimestampString(item.getCreatedAt()));
                BookComment reply = item.getReply();
                TextView tv = helper.getTextView(R.id.item_comment_content);
                if (reply != null) {
                    KLog.json(reply.getUser().toString());
                    KLog.json(reply.getUser().getNickname());
                    tv.setText(ResUtils.getResString(CommentActivity.this, R.string.title_reply)
                            + reply.getUser().getNickname() + ":" + item.getContent());
                } else {
                    helper.getTextView(R.id.item_comment_content).setText(item.getContent());
                }
                ImageView icon = helper.getImageView(R.id.item_comment_icon);
                ImageLoader.getInstance().displayImage(user.getIconurl(), icon, Constants.getImageOptions());
                icon.setOnClickListener(v -> UserHomeActivity.start(CommentActivity.this, icon, user));
                SkinManager.getInstance().injectSkin(tv.getRootView());//item apply theme
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                BookComment toComment = mAdapter.getItem(position);
                SoleUser toUser = toComment.getUser();
                ArrayList<CommentMenuItem> menus = new ArrayList<CommentMenuItem>();
                menus.add(new CommentMenuItem(CommentMenuItem.OP.SHOW, "查看"));
                if (!toUser.equals(mUser)) {//不能回复自己的评论
                    menus.add(new CommentMenuItem(CommentMenuItem.OP.REPLY, "回复"));
                } else {
                    menus.add(new CommentMenuItem(CommentMenuItem.OP.DELETE, "删除"));
                }
                DialogPlusBuilder dialog = DialogPlus.newDialog(CommentActivity.this)
                        .setAdapter(new QuickAdapter<CommentMenuItem>(CommentActivity.this, android.R.layout.simple_list_item_1, menus) {
                            @Override
                            protected void convert(com.joanzapata.android.listview.BaseAdapterHelper helper, CommentMenuItem item) {
                                TextView tv = helper.getTextView(android.R.id.text1);
                                tv.setText(item.getTitle());
                                tv.setTextColor(SkinUtils.getTextHeightColor());
                                helper.getView().setBackgroundColor(SkinUtils.getTextBackgroundColor());
                            }
                        })
                        .setExpanded(false)
                        .setContentBackgroundResource(
                                SkinUtils.getTextBackgroundColorId(CommentActivity.this))
                        .setGravity(Gravity.CENTER)
                        .setCancelable(true);
                long animTime = dialog.getOutAnimation().getDuration();
                dialog.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        dialog.dismiss();
                        if (item instanceof CommentMenuItem) {
                            CommentMenuItem menuItem = (CommentMenuItem) item;
                            CommentMenuItem.OP op = menuItem.getOp();
                            if (op == CommentMenuItem.OP.REPLY) {
                                replyTo(toComment);
                            } else if (op == CommentMenuItem.OP.SHOW) {
                                showComment(toComment, animTime + 500);
                            } else if (op == CommentMenuItem.OP.DELETE) {
                                mPersenter.deleteComment(toComment);
                            }
                        }
                    }
                }).create().show();
            }
        });
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
    }

    /**
     * 显示某条评论,dialog显示
     *
     * @param toComment
     * @param delty
     */
    private void showComment(BookComment toComment, long delty) {
        mSwipeRefreshLayout.postDelayed(() ->
                        DialogUtils.show(CommentActivity.this,
                                toComment.getUser().getNickname(),
                                SkinUtils.getTextColor(),
                                SkinUtils.getTextBackgroundColorId(this),
                                toComment.getContent().split("\\n"))
                , delty);
    }

    /**
     * 回复某个评论
     *
     * @param toComment
     */
    private void replyTo(BookComment toComment) {
        reply = toComment;
        mEditText.setFocusable(true);
        mEditText.setFocusableInTouchMode(true);
        mEditText.requestFocus();
        InputModeUtils.openInputMode(mEditText);
        mEditText.setHint(ResUtils.getResString(CommentActivity.this, R.string.title_reply)
                + ":" + toComment.getUser().getNickname());
    }

    private void resetEditText() {
        mEditText.setHint(R.string.msg_comment_add);
    }

    private void setStatausBarColor() {
        StatusBarCompat.setStatusBarColor(this, SkinUtils.getStatusBarColor());
    }

    private void setEditColor() {
        mEditText.setHintTextColor(SkinUtils.getTextColor());
    }

    private void setSwiptLyaoutColor() {
        mSwipeRefreshLayout.setProgressBackgroundColor(SkinUtils.getLoadProgressColorId(this));
    }

    @Subscriber(tag = Constants.EVENT_THEME_CHANGE)
    public void applyTheme(Object o) {
        setStatausBarColor();
        setSwiptLyaoutColor();
        setEditColor();
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
        mPersenter.sendBookComment(book, reply, t);
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
        mEditText.setText("");
        mSwipeRefreshLayout.autoRefresh();
    }

    @Override
    public void deleteFinish(Exception e) {
        if (e != null) {
            showLightError(ResUtils.getResString(this, R.string.msg_delete_error));
        } else {
            showLightError(ResUtils.getResString(this, R.string.msg_delete_success));
            mSwipeRefreshLayout.autoRefresh();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unregister(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (reply != null) {
            reply = null;
            resetEditText();
        } else {
            super.onBackPressed();
        }
    }
}
