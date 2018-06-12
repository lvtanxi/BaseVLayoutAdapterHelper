package com.mario.baseadapter.demo.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youth.banner.loader.ImageLoader;

/**
 * Date: 2017-11-13
 * Time: 15:47
 * Description:
 */

public class CustomImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        loadImage(context,path.toString(),imageView);
    }

    public static void loadImage(Context context, String path, ImageView imageView){
        GlideApp.with(context).load(path).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into
                (imageView);
    }
}
