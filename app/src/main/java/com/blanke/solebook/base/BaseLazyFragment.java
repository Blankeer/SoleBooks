package com.blanke.solebook.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.avos.avoscloud.AVAnalytics;

/**
 * Created by Blanke on 16-2-23.
 */
public abstract class BaseLazyFragment extends Fragment {
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
        isViewCreate = true;
        onLazyLoad();
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
