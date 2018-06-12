package com.mario.baseadapter.demo.model;

/**
 * Date: 2016-12-15
 * Time: 11:18
 * Description:
 */
public class MainModel {

    private String name;
    private Class<?> mCl;

    public MainModel() {
    }

    public MainModel(Class<?> cl, String name) {
        mCl = cl;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getCl() {
        return mCl;
    }

    public void setCl(Class<?> cl) {
        mCl = cl;
    }
}
