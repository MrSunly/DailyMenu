package com.msanjian.dailymenu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.msanjian.dailymenu.R;
import com.msanjian.dailymenu.data.Category;

import java.util.List;

/**
 * Created by longe on 2016/2/12.
 */
public class MyDrawerLeftAdapter extends BaseAdapter {

    private List<Category> data;
    private Context context;

    public MyDrawerLeftAdapter(Context context,List<Category> data) {
        this.data = data;
        this.context = context;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.drawer_item,null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv);
        tv.setText(getItem(position));
        return convertView;
    }


}
