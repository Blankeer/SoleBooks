package com.blanke.solebook.core.nearmap;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.blanke.solebook.R;
import com.blanke.solebook.base.BaseColumnFragment;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.nearmap.persenter.NearMapPersenter;
import com.blanke.solebook.core.nearmap.persenter.NearMapPersenterImpl;
import com.blanke.solebook.core.nearmap.view.NearMapView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by blanke on 16-4-3.
 */
@EFragment(R.layout.fragment_near_map)
public class NearMapFragment extends BaseColumnFragment<LinearLayout, List<SoleUser>, NearMapView, NearMapPersenter>
        implements NearMapView {
    @ViewById(R.id.fragment_map_map)
    MapView mapView;

    private int size = Constants.PAGE_COUNT;
    private boolean isFirstNetworkFinish = false;
    private AMap aMap;


    public static NearMapFragment newInstance(BookColumn bookColumn) {
        NearMapFragment fragment = new NearMapFragment_();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_BOOKCOLUMN, bookColumn);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void lazyLoad() {
        if (!isFirstNetworkFinish) {
            loadData(false);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
        if (aMap == null) {
            aMap = mapView.getMap();

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.toString();
    }

    @Override
    public NearMapPersenter createPresenter() {
        return new NearMapPersenterImpl();
    }

    @Override
    public void setData(List<SoleUser> data) {

        isFirstNetworkFinish = true;
    }

    @Override
    public void loadData(boolean pullToRefresh) {

    }
}
