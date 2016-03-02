package com.msanjian.dailymenu.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.msanjian.dailymenu.R;
import com.msanjian.dailymenu.adapter.MenuDetailAdapter;
import com.msanjian.dailymenu.data.MenuDetail;
import com.msanjian.dailymenu.utils.ApiUtils;
import com.msanjian.dailymenu.utils.HttpUtils;
import com.msanjian.dailymenu.utils.ResponseHandleUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by longe on 2016/2/17.
 */
public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {


    @Bind(R.id.recyclerViewSearch)
    RecyclerView recyclerViewSearch;
    @Bind(R.id.swipeRefreshLayoutSearch)
    SwipeRefreshLayout swipeRefreshLayoutSearch;
    private String TAG = "SearchFragmentActivity";
    private SearchView searchView;
    private String query;
    private final String SEARCH_MENU = "菜谱查询: ";
    private MenuItem menuItem;
    private ActionBar actionBar;
    private List<MenuDetail> list;
    private MenuDetailAdapter adapter;
    private static boolean firstGetData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        ButterKnife.bind(this);
        actionBar = getSupportActionBar();
        actionBar.setTitle(SEARCH_MENU);
        actionBar.setDisplayHomeAsUpEnabled(true);
        recyclerViewSearch.setHasFixedSize(true);
        recyclerViewSearch.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
    }


    private void getDataFromHttp(String menuName) {

        executePostRequest(ApiUtils.QUERY_URL, new HttpUtils.HttpUtilCallBack() {
            @Override
            public void onFinsh(String response) {
                handleResponse(response);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(SearchActivity.this, "加载失败！", Toast.LENGTH_SHORT).show();
                swipeRefreshLayoutSearch.setRefreshing(false);
            }
        }, "menu", menuName);

    }

    private void handleResponse(final String response) {

        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                if (firstGetData)
                    list = ResponseHandleUtils.handleMenuDetailJsonNotSaveToRealm(getApplication(), response);
                else
                    ResponseHandleUtils.handleMenuDetailJsonNotSaveToRealm(getApplication(), response);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                if (firstGetData) {
                    adapter = new MenuDetailAdapter(getApplication(), list) {
                        @Override
                        protected void onItemClick(View v, int position) {
                            MenuDetail menuDetail = list.get(position);
                            Intent intent = new Intent(getApplication(), MenuStepActivity.class);
                            intent.putExtra("id", menuDetail.getId());
                            startActivity(intent);
                        }
                    };
                    recyclerViewSearch.setAdapter(adapter);
                } else {
                    recyclerViewSearch.getAdapter().notifyDataSetChanged();
                }
                swipeRefreshLayoutSearch.setRefreshing(false);
                swipeRefreshLayoutSearch.setEnabled(false);
                firstGetData = false;
            }
        }.execute(response);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firstGetData = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.onActionViewExpanded();
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.onActionViewCollapsed();
        actionBar.setTitle(SEARCH_MENU + query);
        this.query = query;
        swipeRefreshLayoutSearch.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayoutSearch.setRefreshing(true);
            }
        });
        getDataFromHttp(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onClose() {
        actionBar.setTitle(SEARCH_MENU + query);
        return false;
    }
}
