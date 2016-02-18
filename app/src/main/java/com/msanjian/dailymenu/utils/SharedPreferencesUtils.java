package com.msanjian.dailymenu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by longe on 2016/2/14.
 */
public class SharedPreferencesUtils {

    public static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void SaveCategoryStatus(Context context,boolean status){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean("status",status);
        editor.commit();
    }

    public static boolean getCategoryStatus(Context context){
        return getSharedPreferences(context).getBoolean("status",false);
    }


}
