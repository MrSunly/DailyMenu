package com.msanjian.dailymenu.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.msanjian.dailymenu.R;
import com.msanjian.dailymenu.adapter.MyPagerAdapter;
import com.msanjian.dailymenu.data.Category;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by longe on 2016/2/14.
 */
public class ContainerFragment extends Fragment {

    @Bind(R.id.pagerSlidingTabStrip)
    PagerSlidingTabStrip pagerSlidingTabStrip;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private static String TAG = "ContainerFragment";
    private String PARENT_ID;
    private RealmResults<Category> subCategory;
    private Realm realm;

    public static ContainerFragment newInstance(String parentId){
        Log.d(TAG, "newInstance: ");
        ContainerFragment containerFragment = new ContainerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("parentId", parentId);
        containerFragment.setArguments(bundle);
        return containerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PARENT_ID = getArguments().getString("parentId");
        realm = Realm.getInstance(getActivity());
        initSubCategory(PARENT_ID);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container_fragment, container, false);
        ButterKnife.bind(this, view);

        viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(),subCategory));
        pagerSlidingTabStrip.setViewPager(viewPager);
        pagerSlidingTabStrip.setIndicatorColor(R.color.colorPrimary);
        pagerSlidingTabStrip.setIndicatorHeight(10);
        return view;
    }

    private void initSubCategory(String parentId) {
        subCategory = realm.where(Category.class).equalTo("parentId",parentId).findAll();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
        Log.d(TAG, "onDestroyView: realm close");
        ButterKnife.unbind(this);
    }
}
