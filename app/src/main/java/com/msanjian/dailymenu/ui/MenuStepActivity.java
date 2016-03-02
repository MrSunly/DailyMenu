package com.msanjian.dailymenu.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.msanjian.dailymenu.R;
import com.msanjian.dailymenu.data.MenuDetail;
import com.msanjian.dailymenu.data.Step;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by longe on 2016/2/15.
 */
public class MenuStepActivity extends BaseActivity implements RequestListener<String, GlideDrawable> {

    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.tableLayoutMaterial)
    TableLayout tableLayoutMaterial;
    @Bind(R.id.tableLayoutStep)
    TableLayout tableLayoutStep;

    private String TAG = "MenuStepActivity";
    private String MENU_ID;
    private Realm realm;
    private String url;
    private String strIngredients;
    private String strBurden;
    private MenuDetail menuDetail;
    private RealmResults<Step> steps;
    private LayoutInflater inflater;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_step);
        ButterKnife.bind(this);
        inflater = LayoutInflater.from(this);
        MENU_ID = getIntent().getStringExtra("id");
        realm = Realm.getInstance(this);
        getDataFromRealm();
        initActionbar();
        loadImage();
        addViewToBurdenOrIngredients(formatBurdenOrIngredients(strIngredients));
        addViewToBurdenOrIngredients(formatBurdenOrIngredients(strBurden));
        addViewToStep();
    }

    private void initActionbar() {
        actionBar = getSupportActionBar();
        actionBar.setTitle(menuDetail.getTitle());
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void addViewToStep() {
        for (int i = 0; i < steps.size(); i++) {
            Step step = steps.get(i);
            View v = inflater.inflate(R.layout.step_view_item, null);
            ((TextView) v.findViewById(R.id.tvStep)).setText(step.getStep());
            ImageView img = (ImageView) v.findViewById(R.id.imgStep);
            Glide.with(this)
                    .load(step.getImg())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(img);
            tableLayoutStep.addView(v);
        }
    }

    private void addViewToBurdenOrIngredients(String[][] strings) {
        for (int i = 0; i < strings[0].length; i++) {
            View v = inflater.inflate(R.layout.material_view_item, null);
            ((TextView) v.findViewById(R.id.tvMaterialName)).setText(strings[0][i]);
            ((TextView) v.findViewById(R.id.tvMaterialNumber)).setText(strings[1][i]);
            tableLayoutMaterial.addView(v);
        }
    }

    private void loadImage() {
        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(this)
                .into(imageView);
    }

    private String[][] formatBurdenOrIngredients(String string) {
        String[] strAll = string.split(";");
        String[][] strNameAndNumber = new String[2][strAll.length];
        for (int i = 0; i < strAll.length; i++) {
            String[] str = strAll[i].split(",");
            strNameAndNumber[0][i] = str[0];
            strNameAndNumber[1][i] = str[1];
        }
        return strNameAndNumber;
    }

    private void getDataFromRealm() {
        menuDetail = realm.where(MenuDetail.class).equalTo("id", MENU_ID).findFirst();
        steps = realm.where(Step.class).equalTo("id", MENU_ID).findAll();
        Log.d(TAG, "getDataFromRealm: " + steps);
        url = menuDetail.getImage();
        strIngredients = menuDetail.getIngredients();
        strBurden = menuDetail.getBurden();
    }


    @Override
    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
        imageView.setImageDrawable(resource);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}


