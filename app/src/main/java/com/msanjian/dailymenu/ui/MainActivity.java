package com.msanjian.dailymenu.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.msanjian.dailymenu.R;
import com.msanjian.dailymenu.adapter.MyDrawerLeftAdapter;
import com.msanjian.dailymenu.data.Category;
import com.msanjian.dailymenu.utils.ApiUtils;
import com.msanjian.dailymenu.utils.HttpUtils;
import com.msanjian.dailymenu.utils.ResponseHandleUtils;
import com.msanjian.dailymenu.utils.SharedPreferencesUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;


    private Realm mRealm;
    private RealmResults<Category> parentCategory;
    private String TAG = "MainActivity";
    private String firstParentCategoryId;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private MyDrawerLeftAdapter myDrawerLeftAdapter;
    private boolean categoryStatus = false;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mRealm = Realm.getInstance(this);
        categoryStatus = SharedPreferencesUtils.getCategoryStatus(this);
        if (!categoryStatus) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
            initCategoryData();
        } else {
            initFirstCategory();
        }
        initActionbar();
        myDrawerLeftAdapter = new MyDrawerLeftAdapter(this, parentCategory);
        listView.setAdapter(myDrawerLeftAdapter);
        listView.setItemChecked(0, true);
        listView.setOnItemClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initCategoryData();
            }
        });
    }


    private void initActionbar() {
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.drawer_left_selector);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    private void initCategoryData() {
        Log.d(TAG, "initDrawerLeftData: ");
        HttpUtils.httpGetRequest(this, ApiUtils.CATEGORY_URL, getCallback());
        parentCategory = mRealm.where(Category.class).equalTo("parentId", "0").findAll();
    }

    private HttpUtils.HttpUtilCallBack getCallback() {
        HttpUtils.HttpUtilCallBack httpUtilCallBack = new HttpUtils.HttpUtilCallBack() {
            @Override
            public void onFinsh(String response) {
                handleResponse(response);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(MainActivity.this, "加载失败！", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        };
        return httpUtilCallBack;
    }

    private void handleResponse(final String response) {
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                ResponseHandleUtils.handleCategoryJson(MainActivity.this, response);
                publishProgress();
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                ((MyDrawerLeftAdapter) listView.getAdapter()).notifyDataSetChanged();
                initFirstCategory();
            }

        }.execute(response);

    }

    private void initFirstCategory() {
        parentCategory = mRealm.where(Category.class).equalTo("parentId", "0").findAll();
        firstParentCategoryId = parentCategory.get(0).getId();
        replaceFragment(R.id.container, ContainerFragment.newInstance(firstParentCategoryId));
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);
    }


    protected void replaceFragment(int viewId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }

    protected void replaceFragmentAddToBackStack(int viewId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemClick: ");
        drawerLayout.closeDrawer(GravityCompat.START);
        replaceFragment(R.id.container, ContainerFragment.newInstance(parentCategory.get(position).getId()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()){
            case R.id.actionSearchButton:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onDestroy() {
        mRealm.close();
        super.onDestroy();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
//        replaceFragmentAddToBackStack(R.id.container, SearchFragment.newInstance(query));

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
