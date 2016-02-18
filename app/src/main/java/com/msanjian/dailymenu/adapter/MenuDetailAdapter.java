package com.msanjian.dailymenu.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.msanjian.dailymenu.R;
import com.msanjian.dailymenu.data.MenuDetail;

import java.util.List;

/**
 * Created by longe on 2016/2/14.
 */
public abstract class MenuDetailAdapter extends RecyclerView.Adapter {

    private List<MenuDetail> menuDetailList;
    private Context context;

    public MenuDetailAdapter(Context context, List<MenuDetail> menuDetailList) {
        this.context = context;
        this.menuDetailList = menuDetailList;
    }

    protected abstract void onItemClick(View v,int position);

    public class MyHodler extends RecyclerView.ViewHolder {
        public ImageView iv;
        public TextView tv;

        public MyHodler(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v,getAdapterPosition());
                }
            });
            iv = (ImageView) itemView.findViewById(R.id.iv);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHodler(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        MyHodler hodler = (MyHodler) viewHolder;
        MenuDetail menuDetail = menuDetailList.get(position);
        hodler.tv.setText(menuDetail.getTitle());
        Glide.with(context)
                .load(menuDetail.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(hodler.iv);
        ViewCompat.setTransitionName(hodler.iv, menuDetail.getImage());
    }

    @Override
    public int getItemCount() {
        return menuDetailList.size();
    }
}
