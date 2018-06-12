package com.mario.baseadapter.demo.model;

public class NormalModel {
    public String title;
    public String detail;
    public String avatorPath;
    public boolean selected;

    public NormalModel() {
    }

    public NormalModel(String title, String detail, String avatorPath) {
        this.title = title;
        this.detail = detail;
        this.avatorPath = avatorPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAvatorPath() {
        return avatorPath;
    }

    public void setAvatorPath(String avatorPath) {
        this.avatorPath = avatorPath;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}