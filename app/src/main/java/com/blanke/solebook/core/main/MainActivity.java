package com.blanke.solebook.core.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import com.blanke.solebook.R;
import com.blanke.solebook.base.BaseMvpLceViewStateActivity;
import com.blanke.solebook.bean.BookColumn;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.core.column.ColumnFragment;
import com.blanke.solebook.core.column.ColumnFragment_;
import com.blanke.solebook.core.main.persenter.MainPersenter;
import com.blanke.solebook.core.main.persenter.MainPersenterImpl;
import com.blanke.solebook.core.main.view.MainView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.socks.library.KLog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseMvpLceViewStateActivity<View, List<BookColumn>, MainView, MainPersenter>
        implements NavigationView.OnNavigationItemSelectedListener, MainView {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.fab)
    FloatingActionButton fab;
    @ViewById(R.id.contentView)
    DrawerLayout drawer;
    @ViewById(R.id.nav_view)
    NavigationView navigationView;
    ImageView mImageIcon;
    TextView mTvNickName;

    private List<BookColumn> bookColumns;

    private SoleUser currentUser;
    private int mSelectPostion = -1;
    private Fragment mSelectFragment;

    @AfterViews
    void init() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        currentUser = SoleUser.getCurrentUser(SoleUser.class);

    }

    private void replaceFragment(int position) {
        if (position != mSelectPostion) {
            mSelectPostion = position;
            BookColumn item = bookColumns.get(position);
            toolbar.setTitle(item.getName());
            mSelectFragment = getFragment(position, item);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_fragmelayout, mSelectFragment)
                    .commit();
        }
    }

    private Fragment getFragment(int position, BookColumn item) {
        return new ColumnFragment_().builder()
                .arg(ColumnFragment.ARGS_BOOKCOLUMN, item).build();
    }

    private void initNavigationMenu() {
        Menu menu = navigationView.getMenu();
        int random = (int) (Math.random() * 9 + 1);
        int idbase = random << 10;
        int i = 0;
        for (BookColumn item : bookColumns) {
            MenuItem temp = menu.add(0, idbase + i, i, item.getName());
            AVFile iconfile = item.getIcon();
            if (iconfile != null) {
                iconfile.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, AVException e) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        temp.setIcon(new BitmapDrawable(getResources(), bitmap));
                    }
                });
            }
            if (i == 0) {
                temp.setChecked(true);
            }
            i++;
        }
        menu.setGroupCheckable(0, true, true);//single
        navigationView.post(() -> {

            mTvNickName = (TextView) navigationView.findViewById(R.id.nav_nickname);
            mImageIcon = (ImageView) navigationView.findViewById(R.id.nav_icon);
            mTvNickName.setText(currentUser.getNickname());
            ImageLoader.getInstance().displayImage(currentUser.getIconurl(), mImageIcon, Constants.getImageOptions());
        });
        replaceFragment(0);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);
        replaceFragment(item.getOrder());
        drawer.closeDrawers();
        return true;
    }

    @Override
    public LceViewState<List<BookColumn>, MainView> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<BookColumn> getData() {
        return bookColumns;
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
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadBookColumn(pullToRefresh);
    }
}
