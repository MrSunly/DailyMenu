package com.msanjian.dailymenu.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.msanjian.dailymenu.R;
import com.msanjian.dailymenu.adapter.MenuDetailAdapter;
import com.msanjian.dailymenu.data.MenuDetail;
import com.msanjian.dailymenu.utils.ApiUtils;
import com.msanjian.dailymenu.utils.HttpUtils;
import com.msanjian.dailymenu.utils.ResponseHandleUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by longe on 2016/2/14.
 */
public class ImageFragment extends Fragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private String TAG = "ImageFragment";
    private String PARENT_ID;
    private Realm realm;
    private RealmResults<MenuDetail> menuDetails;
    private MenuDetailAdapter menuDetailAdapter;


    public static ImageFragment newInstance(String parentId) {
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("parentId", parentId);
        Log.d("parentid", "newInstance: " + parentId);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getInstance(getActivity());
        PARENT_ID = getArguments().getString("parentId");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        if (menuDetails.isEmpty()) {
            Log.d(TAG, "emnuDetails is empty ");
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    Log.d(TAG, "run: set swipeRefresh true");
                }
            });
            getDataFromHttp();
        } else swipeRefreshLayout.setEnabled(false);
    }

    private void init() {
        menuDetails = realm.where(MenuDetail.class).equalTo("parentId", PARENT_ID).findAll();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        menuDetailAdapter = new MenuDetailAdapter(getContext(), menuDetails) {

            @Override
            protected void onItemClick(View v, int position) {
                MenuDetail menuDetail = menuDetails.get(position);
                Intent intent = new Intent(getActivity(), MenuStepActivity.class);
                intent.putExtra("id", menuDetail.getId());
                startActivity(intent);
                Log.d(TAG, "startActivity");
            }
        };
        recyclerView.setAdapter(menuDetailAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromHttp();
            }
        });
    }

    private void getDataFromHttp() {
        HttpUtils.httpGetRequest(getContext(), ApiUtils.IMAGE_URL + PARENT_ID, new HttpUtils.HttpUtilCallBack() {
            @Override
            public void onFinsh(String response) {
                handleResponse(response);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getActivity(), "加载失败！", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void handleResponse(final String response) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                ResponseHandleUtils.handleMenuDetailJson(getContext(), response, PARENT_ID);
                publishProgress();
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                recyclerView.getAdapter().notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(false);
                Log.d(TAG, "run: set swipeRefresh false");
            }

        }.execute(response);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
