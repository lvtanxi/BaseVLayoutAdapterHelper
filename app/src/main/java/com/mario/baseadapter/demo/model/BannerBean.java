package com.mario.baseadapter.demo.model;

import java.util.ArrayList;

/**
 * Date: 2017-11-08
 * Time: 14:08
 * Description:
 */

public class BannerBean {
    private ArrayList<String> pic_url;

    public BannerBean(ArrayList<String> pic_url) {
        this.pic_url = pic_url;
    }

    public ArrayList<String> getPic_url() {
        return pic_url;
    }

    public void setPic_url(ArrayList<String> pic_url) {
        this.pic_url = pic_url;
    }
}
