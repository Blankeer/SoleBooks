package com.blanke.solebook.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.avos.avoscloud.AVAnalytics;
import com.blanke.solebook.R;
import com.blanke.solebook.bean.BookColumn;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.melnykov.fab.FloatingActionButton;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        mCurrentBookColumn = getArguments().getParcelable(ARG_BOOKCOLUMN);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
    }


    private boolean isVisible = false;
    private boolean isViewCreate = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = getUserVisibleHint();
        if (isVisible) {
            onVisible();
        } else {
            onInVisible();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        isViewCreate = true;
//        onLazyLoad();
    }


    protected abstract void lazyLoad();

    protected void onInVisible() {

    }

    private void onLazyLoad() {
        if (isViewCreate && isVisible) {
            lazyLoad();
        }
    }

    protected void onVisible() {
        onLazyLoad();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onStart() {
        super.onStart();
        isViewCreate = true;
        onLazyLoad();
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
