package com.msanjian.dailymenu;

import android.app.Application;
import android.content.Context;

/**
 * Created by longe on 2016/3/2.
 */
public class App extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }

}
