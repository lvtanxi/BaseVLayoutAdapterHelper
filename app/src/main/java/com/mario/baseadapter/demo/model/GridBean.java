package com.mario.baseadapter.demo.model;

/**
 * Date: 2017-11-08
 * Time: 14:08
 * Description:
 */

public class GridBean {
    private String pic_url;
    private String function;

    public GridBean(String pic_url, String function) {
        this.pic_url = pic_url;
        this.function = function;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}
