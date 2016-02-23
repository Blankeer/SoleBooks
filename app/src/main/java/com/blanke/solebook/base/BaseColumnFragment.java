package com.blanke.solebook.base;

import android.os.Bundle;
import android.view.View;

import com.blanke.solebook.bean.BookColumn;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateFragment;

/**
 * 所有 colmn栏目fragment的子类，也就是 viewpager 的 fagment
 * 子类包括  book  等类型
 * Created by Blanke on 16-2-23.
 */
public abstract class BaseColumnFragment<CV extends View, M, V extends MvpLceView<M>, P extends MvpPresenter<V>> extends MvpLceViewStateFragment<CV, M, V, P> {
    public static final String ARG_BOOKCOLUMN = "BaseColumnFragment_BookColumn";
    protected BookColumn mCurrentBookColumn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentBookColumn = getArguments().getParcelable(ARG_BOOKCOLUMN);
    }
}
