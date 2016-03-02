package com.msanjian.dailymenu.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.msanjian.dailymenu.App;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by longe on 2016/2/13.
 */
public class HttpUtils {
    private static RequestQueue requestQueue = Volley.newRequestQueue(App.getContext());
    private static String TAG = "HttpUtils";

    public static void httpGetRequest(String url, final HttpUtilCallBack httpUtilCallBack, Object tag) {

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                httpUtilCallBack.onFinsh(response);
                Log.d(TAG, "finish");
                Log.d(TAG, "onResponse: " + response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                httpUtilCallBack.onError(error);
            }
        };

        StringRequest stringRequest = new StringRequest(url, listener, errorListener);
        stringRequest.setTag(tag);
        requestQueue.add(stringRequest);
    }


    public static void httpPostRequest(String url, final HttpUtilCallBack httpUtilCallBack, final String key, final String value, Object tag) {

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                httpUtilCallBack.onFinsh(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                httpUtilCallBack.onError(error);
            }
        };


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(key, value);
                return map;
            }
        };
        stringRequest.setTag(tag);
        requestQueue.add(stringRequest);

    }

    public static void CancelAll(Object tag) {
        requestQueue.cancelAll(tag);
    }


    public interface HttpUtilCallBack {
        void onFinsh(String response);
        void onError(Exception e);
    }
}
