package com.blanke.solebook.core.nearmap;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.avos.avoscloud.AVGeoPoint;
import com.blanke.solebook.R;
import com.blanke.solebook.base.BaseColumnFragment;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.nearmap.persenter.NearMapPersenter;
import com.blanke.solebook.core.nearmap.persenter.NearMapPersenterImpl;
import com.blanke.solebook.core.nearmap.view.NearMapView;
import com.blanke.solebook.core.userhome.UserHomeActivity;
import com.blanke.solebook.manager.LocalManager;
import com.blanke.solebook.utils.ResUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by blanke on 16-4-3.
 */
@EFragment(R.layout.fragment_near_map)
public class NearMapFragment extends BaseColumnFragment<LinearLayout, List<SoleUser>, NearMapView, NearMapPersenter>
        implements NearMapView, LocationSource {
    @ViewById(R.id.fragment_map_map)
    MapView mapView;
    //    @ViewById(R.id.fragment_near_scrollview)
    NestedScrollView mScrollView;

    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private int size = Constants.PAGE_COUNT;
    private boolean isFirstNetworkFinish = false;
    private AMap aMap;
    private Bundle savedInstanceState;
    private LocalManager localManager;
    private NearMapPersenter mPersenter;
    private AVGeoPoint location;
    private SoleUser user;

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
//            loadData(false);
            setUpMap();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
    }

    @AfterViews
    public void init() {
        createPresenter();
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
                @Override
                public void onTouch(MotionEvent motionEvent) {
                    int action = motionEvent.getAction();
                    if (action == MotionEvent.ACTION_MOVE) {
                        mapView.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
            });
        }
    }

    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
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
        deactivate();
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
        if (mPersenter == null) {
            mPersenter = new NearMapPersenterImpl();
        }
        return mPersenter;
    }

    @Override
    public void setData(List<SoleUser> data) {
        if (data == null) {
            return;
        }
//        KLog.json(data.toString());
        isFirstNetworkFinish = true;
        addMapUsers(data);
    }

    /**
     * 添加附近的人到地图上
     *
     * @param data
     */
    private void addMapUsers(List<SoleUser> data) {
        LatLngBounds.Builder boundBuilder = new LatLngBounds.Builder();//所有标签的位置
        AVGeoPoint local;
        String title, subtitle;
        LatLng latLng;
        CircleImageView imageView;
        ArrayList<LatLng> latLngs = new ArrayList<>();
        for (SoleUser u : data) {
            title = u.getNickname();
            subtitle = u.getCity() + u.getDistrict();
            if (user != null && u.getObjectId().equals(user.getObjectId())) {//自己
                title = ResUtils.getResString(getActivity(), R.string.title_me);
            }
            local = u.getLocation();
//            imageView = new CircleImageView(getContext());
//            imageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
//            ImageLoader.getInstance().displayImage(u.getIconurl(), imageView,
//                    Constants.getImageOptions());
            latLng = addMapMarks(local.getLatitude(), local.getLongitude(),
                    title, subtitle, null);
            latLngs.add(latLng);
            boundBuilder.include(latLng);
        }
        // 移动地图，所有marker自适应显示。LatLngBounds与地图边缘10像素的填充区域
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundBuilder.build(), 10));
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng l = marker.getPosition();
                SoleUser u = data.get(latLngs.indexOf(l));
                UserHomeActivity.start(getActivity(), null, u);
                return false;
            }
        });
    }

    private LatLng addMapMarks(double latitude, double longitude,
                               String title, String subtitle, View markView) {
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions option = new MarkerOptions()
                .position(latLng)
                .title(title)
                .snippet(subtitle);
//                .icon(BitmapDescriptorFactory.fromView(markView));
        aMap.addMarker(option);
        return latLng;
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        user = SoleUser.getCurrentUser(SoleUser.class);
//        if (user == null || user.isAnonymous()) {
//            return;
//        }
        mPersenter.getNearFriend(pullToRefresh, location, Constants.NEAR_FRIEND_SIZE);
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        localManager = new LocalManager(getContext());
        showLoading(false);
        localManager.start(new LocalManager.CallBack() {
            @Override
            public void onSuccess(AMapLocation location) {
                listener.onLocationChanged(location);
                NearMapFragment.this.location = new AVGeoPoint(location.getLatitude(), location.getLongitude());
                loadData(false);
            }

            @Override
            public void onError(String msg) {
                errorView.setText(msg);
                animateErrorViewIn();
                isFirstNetworkFinish = false;
            }
        });
    }

    @Override
    public void deactivate() {
        if (localManager != null) {
            localManager.stop();
        }
    }

}
