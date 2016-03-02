package com.msanjian.dailymenu.ui;

import android.support.v4.app.Fragment;

import com.msanjian.dailymenu.utils.HttpUtils;

/**
 * Created by longe on 2016/3/2.
 */
public class BaseFragment extends Fragment {

    protected void executeGetRequest(String url, final HttpUtils.HttpUtilCallBack httpUtilCallBack){
        HttpUtils.httpGetRequest(url,httpUtilCallBack,this);
    }

    protected void executePostRequest(String url, final HttpUtils.HttpUtilCallBack httpUtilCallBack, final String key, final String value){
        HttpUtils.httpPostRequest(url, httpUtilCallBack, key,value,this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HttpUtils.CancelAll(this);
    }
}
