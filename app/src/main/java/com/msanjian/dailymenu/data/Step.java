package com.msanjian.dailymenu.data;

import io.realm.RealmObject;

/**
 * Created by longe on 2016/2/14.
 */
public class Step extends RealmObject{
    private String id;
    private String img;
    private String step;

    public Step() {
    }

    public Step(String id, String img, String step) {
        this.id = id;
        this.img = img;
        this.step = step;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}
