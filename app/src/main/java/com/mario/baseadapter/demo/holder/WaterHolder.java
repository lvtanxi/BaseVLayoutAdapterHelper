package com.mario.baseadapter.demo.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mario.baseadapter.demo.R;
import com.mario.baseadapter.demo.image.CustomImageLoader;
import com.mario.baseadapter.demo.model.WaterCargo;
import com.mario.baseadapter.demo.util.ScreenUtil;
import com.mario.baseadapter.holder.VBaseHolder;
import com.mario.baseadapter.holder.VBaseHolderHelper;


public class WaterHolder extends VBaseHolder<WaterCargo> {

    public WaterHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void convert(VBaseHolderHelper helper, WaterCargo waterCargo, int position) {
        ImageView mPic=helper.getView(R.id.pic);
        ViewGroup.LayoutParams params =mPic.getLayoutParams();
        params.width = ScreenUtil.getScreenWidth(helper.getConvertView().getContext()) / 2 - 2;
        params.height = ScreenUtil.getScreenHeight(helper.getConvertView().getContext()) / 4 + (int)(Math.random()*100);
        CustomImageLoader.loadImage(mPic.getContext(),waterCargo.getPic_url(),mPic);
        helper.setText(R.id.title,waterCargo.getTitle())
        .setText(R.id.price,"¥ " + waterCargo.getPrice())
        .setText(R.id.num,waterCargo.getBuynum() + "人购买");
    }

}
