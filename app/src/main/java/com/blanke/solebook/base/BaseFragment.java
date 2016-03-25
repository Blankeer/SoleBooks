package com.blanke.solebook.base;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.avos.avoscloud.AVAnalytics;


/**
 * Created by Administrator on 15-12-30.
 */
public abstract class BaseFragment extends Fragment {
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
