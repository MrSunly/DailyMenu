package com.msanjian.dailymenu.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.msanjian.dailymenu.R;
import com.msanjian.dailymenu.data.Category;
import com.msanjian.dailymenu.data.MenuDetail;
import com.msanjian.dailymenu.data.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by longe on 2016/2/13.
 */
public class ResponseHandleUtils {
    private static String TAG = "responseHandleUtils";
    private static List<MenuDetail> list;

    public static void handleCategoryJson(Context context, String response) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("resultcode").equals("200")) {
                Log.d(TAG, "handleCategoryJson: ");
                JSONArray parent = jsonObject.getJSONArray("result");
                for (int i = 0; i < parent.length(); i++) {
                    JSONObject parentCategory = parent.getJSONObject(i);
                    String parentId = "0";
                    String id = parentCategory.getString("parentId");
                    String name = parentCategory.getString("name");
                    Category category = new Category(id, name, parentId);
                    realm.copyToRealm(category);
                    JSONArray sub = parentCategory.getJSONArray("list");
                    for (int j = 0; j < sub.length(); j++) {
                        JSONObject subCategory = sub.getJSONObject(j);
                        id = subCategory.getString("id");
                        name = subCategory.getString("name");
                        parentId = subCategory.getString("parentId");
                        Category categorySub = new Category(id, name, parentId);
                        realm.copyToRealm(categorySub);
                    }
                }
                SharedPreferencesUtils.SaveCategoryStatus(context, true);
            } else {
//                getDataError(context);
            }
        } catch (JSONException e) {
            e.printStackTrace();
//            getDataError(context);
        }
        realm.commitTransaction();
        realm.close();
    }

    private static void getDataError(Context context) {
        Toast.makeText(context, R.string.getDataFail, Toast.LENGTH_SHORT).show();
    }


    public static void handleMenuDetailJson(Context context, String response, String parentId) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        try {
            JSONObject jsonObject1 = new JSONObject(response);
            if (jsonObject1.getString("resultcode").equals("200")) {
                Log.d(TAG, "handleMenuDetailJson: ");
                JSONObject result = jsonObject1.getJSONObject("result");
                JSONArray jsonData = result.getJSONArray("data");
                for (int i = 0; i < jsonData.length(); i++) {
                    JSONObject jsonObject = jsonData.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String title = jsonObject.getString("title");
                    String ingredients = jsonObject.getString("ingredients");
                    String burden = jsonObject.getString("burden");
                    JSONArray albums = jsonObject.getJSONArray("albums");
                    String image = albums.getString(0);
                    MenuDetail menuDetail = new MenuDetail(title, ingredients, id, burden, image, parentId);
                    if (realm.where(Step.class).equalTo("id", id).findAll().isEmpty()) {
                        realm.copyToRealm(menuDetail);
                        JSONArray jsonSteps = jsonObject.getJSONArray("steps");
                        for (int j = 0; j < jsonSteps.length(); j++) {
                            JSONObject jsonStep = jsonSteps.getJSONObject(j);
                            String img = jsonStep.getString("img");
                            String step = jsonStep.getString("step");
                            Step step1 = new Step(id, img, step);
                            realm.copyToRealm(step1);
                        }
                    }
                }
            }
//                getDataError(context);
        } catch (JSONException e) {
            e.printStackTrace();
//            getDataError(context);
        }
        realm.commitTransaction();
        realm.close();
    }


    public static List<MenuDetail> handleMenuDetailJsonNotSaveToRealm(Context context, String response) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        if (list == null){
            list = new ArrayList<>();
        }
        list.clear();
        try {
            JSONObject jsonObject1 = new JSONObject(response);
            if (jsonObject1.getString("resultcode").equals("200")) {
                Log.d(TAG, "handleMenuDetailJson: ");
                JSONObject result = jsonObject1.getJSONObject("result");
                JSONArray jsonData = result.getJSONArray("data");
                for (int i = 0; i < jsonData.length(); i++) {
                    JSONObject jsonObject = jsonData.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String title = jsonObject.getString("title");
                    String ingredients = jsonObject.getString("ingredients");
                    String burden = jsonObject.getString("burden");
                    JSONArray albums = jsonObject.getJSONArray("albums");
                    String image = albums.getString(0);
                    MenuDetail menuDetail = new MenuDetail(title, ingredients, id, burden, image, "");
                    list.add(menuDetail);
                    if (realm.where(MenuDetail.class).equalTo("id", id).findAll().isEmpty()) {
                        realm.copyToRealm(menuDetail);
                        JSONArray jsonSteps = jsonObject.getJSONArray("steps");
                        for (int j = 0; j < jsonSteps.length(); j++) {
                            JSONObject jsonStep = jsonSteps.getJSONObject(j);
                            String img = jsonStep.getString("img");
                            String step = jsonStep.getString("step");
                            Step step1 = new Step(id, img, step);
                            realm.copyToRealm(step1);
                        }
                    }
                }
            }
//                getDataError(context);
        } catch (JSONException e) {
            e.printStackTrace();
//            getDataError(context);
        }
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "handleMenuDetailJsonNotSaveToRealm: "+list);
        return list;
    }

}
