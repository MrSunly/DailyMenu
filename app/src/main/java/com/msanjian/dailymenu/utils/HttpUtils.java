package com.msanjian.dailymenu.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by longe on 2016/2/13.
 */
public class HttpUtils {
    private static RequestQueue requestQueue = null;
    private static String TAG = "HttpUtils";

    public static void httpGetRequest(Context context, String url, final HttpUtilCallBack httpUtilCallBack) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                httpUtilCallBack.onFinsh(response);
                Log.d(TAG, "finish");
                Log.d(TAG, "onResponse: "+response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                httpUtilCallBack.onError(error);
            }
        };


        StringRequest stringRequest = new StringRequest(url, listener, errorListener);
        requestQueue.add(stringRequest);

    }


    public static void httpPostRequest(Context context, String url, final HttpUtilCallBack httpUtilCallBack, final String key, final String value) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }

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


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put(key, value);
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }


    public interface HttpUtilCallBack {
        void onFinsh(String response);
        void onError(Exception e);
    }
}
