package com.mario.baseadapter.demo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2017-11-08
 * Time: 14:08
 * Description:
 */

public class HomeData {
    private BannerBean banner;
    private ArrayList<GridBean> grid;
    private ArrayList<Headlines> headlines;
    private ArrayList<TmallMarket> tmarket;
    private ArrayList<String> force;
    private ArrayList<String> haohuo;
    private ArrayList<WaterCargo> watercargo;
    private List<Section2Model> mSection2Models;

    public HomeData(BannerBean banner, ArrayList<GridBean> grid, ArrayList<Headlines> headlines,
                    ArrayList<TmallMarket> tmarket) {
        this.banner = banner;
        this.grid = grid;
        this.headlines = headlines;
        this.tmarket = tmarket;
    }

    public BannerBean getBanner() {
        return banner;
    }

    public void setBanner(BannerBean banner) {
        this.banner = banner;
    }

    public ArrayList<GridBean> getGrid() {
        return grid;
    }

    public void setGrid(ArrayList<GridBean> grid) {
        this.grid = grid;
    }

    public ArrayList<Headlines> getHeadlines() {
        return headlines;
    }

    public void setHeadlines(ArrayList<Headlines> headlines) {
        this.headlines = headlines;
    }

    public ArrayList<TmallMarket> getTmarket() {
        return tmarket;
    }

    public void setTmarket(ArrayList<TmallMarket> tmarket) {
        this.tmarket = tmarket;
    }

    public ArrayList<String> getForce() {
        return force;
    }

    public void setForce(ArrayList<String> force) {
        this.force = force;
    }

    public ArrayList<String> getHaohuo() {
        return haohuo;
    }

    public void setHaohuo(ArrayList<String> haohuo) {
        this.haohuo = haohuo;
    }

    public ArrayList<WaterCargo> getWatercargo() {
        return watercargo;
    }

    public void setWatercargo(ArrayList<WaterCargo> watercargo) {
        this.watercargo = watercargo;
    }

    public List<Section2Model> getSection2Models() {
        return mSection2Models;
    }

    public void setSection2Models(List<Section2Model> section2Models) {
        mSection2Models = section2Models;
    }
}
