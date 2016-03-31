package com.blanke.solebook.core.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.blanke.solebook.R;
import com.blanke.solebook.base.BaseMvpLceViewStateActivity;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.column.ColumnFragment;
import com.blanke.solebook.core.feedback.FeedActivity;
import com.blanke.solebook.core.main.persenter.MainPersenter;
import com.blanke.solebook.core.main.persenter.MainPersenterImpl;
import com.blanke.solebook.core.main.view.MainView;
import com.blanke.solebook.core.scan.CommonScanActivity_;
import com.blanke.solebook.core.search.SearchResActivity_;
import com.blanke.solebook.core.userhome.UserHomeActivity;
import com.blanke.solebook.view.CurstumSearchView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;
import com.jaeger.library.StatusBarUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.socks.library.KLog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import qiu.niorgai.StatusBarCompat;

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

    private List<BookColumn> bookColumns;

    private SoleUser currentUser;
    private int mSelectPostion = -1;
    private Fragment mSelectFragment;
    private ActionBarDrawerToggle toggle;
    private Fragment[] fragments;
    private boolean isVisible;
    private FeedbackAgent agent;

    @AfterViews
    void init() {
//        StatusBarUtil.setColorForDrawerLayout(this, drawer, getResources().getColor(R.color.colorAccent));
//        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.colorAccent));
        StatusBarCompat.translucentStatusBar(this);
        long t1 = System.currentTimeMillis();
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
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
        }
        KLog.d("replaceFragment time:" + (System.currentTimeMillis() - t1));
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
        if (currentUser != null) {
            navigationView.postDelayed(() -> {
                mTvNickName = (TextView) navigationView.findViewById(R.id.nav_nickname);
                mImageIcon = (ImageView) navigationView.findViewById(R.id.nav_icon);
                String nick = currentUser.getNickname();
                mTvNickName.setText(nick == null ? "" : nick);
                ImageLoader.getInstance().displayImage(currentUser.getIconurl(), mImageIcon, Constants.getImageOptions());
                mImageIcon.setOnClickListener(v -> UserHomeActivity.start(MainActivity.this, mImageIcon, currentUser));
            }, 800);
        }
        KLog.d("initNavigationMenu time:" + (System.currentTimeMillis() - t1));
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
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
            }
        }
        drawer.closeDrawers();
        return true;
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
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadBookColumn(pullToRefresh);
    }
}
