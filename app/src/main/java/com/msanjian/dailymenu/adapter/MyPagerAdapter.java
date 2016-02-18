package com.msanjian.dailymenu.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.msanjian.dailymenu.data.Category;
import com.msanjian.dailymenu.ui.ImageFragment;

import java.util.List;

/**
 * Created by longe on 2016/2/14.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    private String TAG = "MypagerAdapter";
    private List<Category> subCategory;

    public MyPagerAdapter(FragmentManager fm, List<Category> subCategory) {
        super(fm);
        this.subCategory = subCategory;
        Log.d(TAG, "MyPagerAdapter: ");
    }

    @Override
    public ImageFragment getItem(int position) {
        Log.d(TAG, "ImageFragment: newInstance");
        return ImageFragment.newInstance(subCategory.get(position).getId());
    }

    @Override
    public int getCount() {
//        Log.d(TAG, "getCount: size"+subCategory.size());
        return subCategory.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        Log.d(TAG, "getPageTitle: ");
        return subCategory.get(position).getName();
    }
}
