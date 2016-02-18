package com.msanjian.dailymenu.data;

import io.realm.RealmObject;

/**
 * Created by longe on 2016/2/14.
 */
public class MenuDetail extends RealmObject {
    private String id;
    private String title;
    private String ingredients;
    private String burden;
    private String image;
    private String parentId;

    public MenuDetail() {
    }

    public MenuDetail(String title, String ingredients, String id, String burden, String image, String parentId) {
        this.title = title;
        this.ingredients = ingredients;
        this.id = id;
        this.burden = burden;
        this.image = image;
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getBurden() {
        return burden;
    }

    public void setBurden(String burden) {
        this.burden = burden;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}

