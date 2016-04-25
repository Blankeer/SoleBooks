package com.blanke.solebook.core.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amap.api.location.AMapLocation;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.blanke.solebook.R;
import com.blanke.solebook.base.BaseMvpLceViewStateActivity;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.column.ColumnFragment;
import com.blanke.solebook.core.feedback.FeedActivity;
import com.blanke.solebook.core.login.LoginActivity_;
import com.blanke.solebook.core.main.persenter.MainPersenter;
import com.blanke.solebook.core.main.persenter.MainPersenterImpl;
import com.blanke.solebook.core.main.view.MainView;
import com.blanke.solebook.core.scan.CommonScanActivity_;
import com.blanke.solebook.core.search.SearchResActivity_;
import com.blanke.solebook.core.settings.SettingsActivity_;
import com.blanke.solebook.core.userhome.UserHomeActivity;
import com.blanke.solebook.manager.LocalManager;
import com.blanke.solebook.utils.BitmapUtils;
import com.blanke.solebook.utils.SkinUtils;
import com.blanke.solebook.utils.StatusBarCompat;
import com.blanke.solebook.view.CurstumSearchView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;
import com.melnykov.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.socks.library.KLog;
import com.zhy.changeskin.SkinManager;

import net.qiujuer.genius.blur.StackBlur;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseMvpLceViewStateActivity<View, List<BookColumn>, MainView, MainPersenter>
        implements NavigationView.OnNavigationItemSelectedListener, MainView {
    private static final String FRAGGMENT_TAG = "FRAGGMENT_TAG";
    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.contentView)
    DrawerLayout drawer;
    @ViewById(R.id.nav_view)
    NavigationView navigationView;
    ImageView mImageIcon;
    TextView mTvNickName;
    @ViewById(R.id.search_view)
    CurstumSearchView searchView;
    @ViewById(R.id.fab)
    FloatingActionButton fab;
    @ViewById(R.id.activity_main_coordlayout)
    CoordinatorLayout mCoordinatorLayout;
    @ViewById(R.id.nav_head_layout)
    View navLayout;

    private List<BookColumn> bookColumns;

    private SoleUser currentUser;
    private int mSelectPostion = -1;
    private Fragment mSelectFragment;
    private ActionBarDrawerToggle toggle;
    private Fragment[] fragments;
    private boolean isVisible;
    private FeedbackAgent agent;
    private Snackbar backPressSnackbar;//两次按下返回退出
    private LocalManager localManager;
    private View mLogout;

    @AfterViews
    void init() {
        EventBus.getDefault().register(this);
        applyTheme(null);
        long t1 = System.currentTimeMillis();
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        searchView.setOnQueryTextListener(new CurstumSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchResActivity_.intent(MainActivity.this).key(query).start();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setVoiceViewClickListener(new CurstumSearchView.VoiceViewClickListener() {
            @Override
            public void onClick(View v) {
                CommonScanActivity_.intent(MainActivity.this).start();
                searchView.closeSearch();
            }
        });
        KLog.d("init time:" + (System.currentTimeMillis() - t1));
    }

    private void initCloud() {
        agent = new FeedbackAgent(this);
        agent.sync();
    }

    private void replaceFragment(int position) {
        long t1 = System.currentTimeMillis();
        if (position != mSelectPostion) {
            searchView.closeSearch();//选择其他menu，关闭搜索框
            mSelectPostion = position;
            BookColumn item = bookColumns.get(position);
            toolbar.setTitle(item.getName());
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            hideAllFragment(trans);
            if (fragments[position] != null) {
                trans.show(fragments[position]);
            } else {
                trans.add(R.id.activity_main_fragmelayout, getNewFragment(position, item), FRAGGMENT_TAG + position);
            }
            trans.commit();
            mSelectFragment = fragments[position];
            KLog.d("replaceFragment time:" + (System.currentTimeMillis() - t1));
        }
    }

    private void hideAllFragment(FragmentTransaction trans) {
        for (Fragment f : fragments) {
            if (f != null) {
                trans.hide(f);
            }
        }
    }

    private Fragment getNewFragment(int position, BookColumn item) {
        if (fragments[position] == null) {
            fragments[position] = ColumnFragment.newInstance(item);
        }
        return fragments[position];
    }

    private void initNavigationMenu() {
        long t1 = System.currentTimeMillis();
        currentUser = SoleUser.getCurrentUser(SoleUser.class);
        Menu menu = navigationView.getMenu();
        int random = (int) (Math.random() * 9 + 1);
        int idbase = random << 10;
        int i = 0;
        fragments = new Fragment[bookColumns.size()];
        for (BookColumn item : bookColumns) {
            MenuItem temp = menu.add(0, idbase + i, i, item.getName());
            AVFile iconfile = item.getIcon();
            if (iconfile != null) {
                iconfile.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, AVException e) {
                        if (bytes != null) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            temp.setIcon(new BitmapDrawable(getResources(), bitmap));
                        }
                    }
                });
            }
            if (i == 0) {
                temp.setChecked(true);
            }
            fragments[i] = getSupportFragmentManager().findFragmentByTag(FRAGGMENT_TAG + i);//找寻丢失的fragment
            i++;
        }
        menu.setGroupCheckable(0, true, true);//single
        if (currentUser != null && !currentUser.isAnonymous()) {
            navigationView.postDelayed(() -> {
                mTvNickName = (TextView) navigationView.findViewById(R.id.nav_nickname);
                mImageIcon = (ImageView) navigationView.findViewById(R.id.nav_icon);
                navLayout = navigationView.findViewById(R.id.nav_head_layout);
                mLogout = navigationView.findViewById(R.id.nav_logout);
                mLogout.setVisibility(View.VISIBLE);
                //退出登录
                mLogout.setOnClickListener(v -> new MaterialDialog.Builder(this)
                        .title(R.string.title_hint).content(R.string.msg_logout)
                        .positiveText(R.string.title_confirm).negativeText(R.string.title_cancel)
                        .backgroundColor(SkinUtils.getWindowColor())
                        .contentColor(SkinUtils.getTextHeightColor())
                        .titleColor(SkinUtils.getTextHeightColor())
                        .onPositive((dialog, which) -> logout()).show());
                String nick = currentUser.getNickname();
                mTvNickName.setText(nick == null ? "" : nick);
                ImageLoader.getInstance().displayImage(currentUser.getIconurl(), mImageIcon, Constants.getImageOptions(), new SimpleImageLoadingListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        navLayout.setBackground(new BitmapDrawable(getResources(),
                                StackBlur.blurNativelyPixels(BitmapUtils.addBlackBitmap(loadedImage), Constants.BLUE_VALUE, false)));
                    }
                });
                mImageIcon.setOnClickListener(v -> UserHomeActivity.start(MainActivity.this, mImageIcon, currentUser));
            }, 800);
        }
        KLog.d("initNavigationMenu time:" + (System.currentTimeMillis() - t1));
    }

    private void logout() {
        SoleUser.logOut();
        LoginActivity_.intent(this).start();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            if (backPressSnackbar == null) {
                backPressSnackbar = Snackbar.make(mCoordinatorLayout, R.string.msg_backpressed, Snackbar.LENGTH_LONG);
            }
            if (backPressSnackbar.isShown()) {
                super.onBackPressed();
            } else {
                backPressSnackbar.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int groupId = item.getGroupId();
        if (groupId == 0) {
            item.setChecked(true);
            drawer.postDelayed(() -> replaceFragment(item.getOrder()), 0);
        } else if (groupId == R.id.navigation_group_setting) {
            int id = item.getItemId();
            switch (id) {
                case R.id.navigation_feedback:
//                    agent.startDefaultThreadActivity();
                    Intent intent = new Intent(this, FeedActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_chose_theme:
                    SkinUtils.toggleTheme();
                    break;
                case R.id.navigation_setting:
                    SettingsActivity_.intent(this).start();
                    break;
            }
        }
        drawer.closeDrawers();
        return true;
    }

    private void setStatusBarColor() {
        int c = SkinManager.getInstance().getResourceManager().getColor(Constants.RES_COLOR_STATUSBAR);
        StatusBarCompat.setStatusBarColorByDrawerLayout(this, drawer, c);
    }

    private void setFabColor() {
        int c = SkinManager.getInstance().getResourceManager().getColor("fab_background");
        int c2 = SkinManager.getInstance().getResourceManager().getColor("fab_press");
        fab.setColorNormal(c);
        fab.setColorRipple(c2);
        fab.setColorPressed(c);
    }

    private void setMenuColor() {
        int[] states_check = new int[]{android.R.attr.state_checked};
        int[] states_normal = new int[]{};
        int c = SkinManager.getInstance().getResourceManager().getColor(Constants.RES_COLOR_TEXT_HIGHT);//文字颜色
        int checkColor = getResources().getColor(R.color.colorAccent);//文字选中颜色
        ColorStateList colorList = new ColorStateList
                (new int[][]{states_check, states_normal}, new int[]{checkColor, c});
        navigationView.setItemTextColor(colorList);
    }

    private void initLocation() {
        if (currentUser == null || currentUser.isAnonymous()) {
            return;
        }
        localManager = new LocalManager(this);
        localManager.start(new LocalManager.CallBack() {
            @Override
            public void onSuccess(AMapLocation location) {
                AVGeoPoint point = new AVGeoPoint(location.getLatitude(), location.getLongitude());
                String city = location.getCity();
                String district = location.getDistrict();
                currentUser.setCity(city);
                currentUser.setDistrict(district);
                currentUser.setLocation(point);
                currentUser.saveInBackground();
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    @Subscriber(tag = Constants.EVENT_THEME_CHANGE)
    public void applyTheme(Object o) {
        setStatusBarColor();
        setFabColor();
        setMenuColor();
    }

    @Override
    public LceViewState<List<BookColumn>, MainView> createViewState() {
        return new CastedArrayListLceViewState<>();
    }

    @Override
    public List<BookColumn> getData() {
        return new ArrayList<>(bookColumns);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @NonNull
    @Override
    public MainPersenter createPresenter() {
        return new MainPersenterImpl();
    }

    @Override
    public void setData(List<BookColumn> data) {
        this.bookColumns = data;
        initNavigationMenu();
        replaceFragment(0);
        initCloud();
        initLocation();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadBookColumn(pullToRefresh);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
