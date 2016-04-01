package com.blanke.solebook.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.avos.avoscloud.AVAnalytics;
import com.blanke.solebook.R;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.utils.AnimUtils;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.melnykov.fab.FloatingActionButton;
import com.socks.library.KLog;

/**
 * 所有 colmn栏目fragment的子类，也就是 viewpager 的 fagment
 * 子类包括  book  等类型
 * Created by Blanke on 16-2-23.
 */
public abstract class BaseColumnFragment<CV extends View, M, V extends MvpLceView<M>, P extends MvpPresenter<V>>
        extends MvpLceFragment<CV, M, V, P> {
    public static final String ARG_BOOKCOLUMN = "BaseColumnFragment_BookColumn";
    protected BookColumn mCurrentBookColumn;
    protected FloatingActionButton fab;

    private boolean isVisible = false;
    private boolean isViewCreate = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        mCurrentBookColumn = getArguments().getParcelable(ARG_BOOKCOLUMN);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
    }

    @Override
    public void onStart() {
        super.onStart();
        isViewCreate = true;
        onLazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = getUserVisibleHint();
        KLog.d(isVisible+","+hashCode());
        if (isVisible) {
            onVisible();
        } else {
            onInVisible();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        KLog.d();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        KLog.d();
    }

    protected abstract void lazyLoad();

    protected void onInVisible() {

    }

    private void onLazyLoad() {
        if (isViewCreate && isVisible) {
            KLog.d();
            lazyLoad();
            changeArrowVisible();
        }
    }

    private void changeArrowVisible() {//改变arrow的hide/show
        boolean flag = false;
        for (int type : Constants.TYPE_HIDE_FAB) {
            if (mCurrentBookColumn.getType() == type) {
                flag = true;
                break;
            }
        }
        if (flag) {
            AnimUtils.fabHide(fab);
        } else {
            AnimUtils.fabShow(fab);
        }
    }

    protected void onVisible() {
        onLazyLoad();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onPause() {
        super.onPause();
        AVAnalytics.onFragmentEnd(getClass().getSimpleName());
    }

    public void onResume() {
        super.onResume();
        AVAnalytics.onFragmentStart(getClass().getSimpleName());
    }
}
