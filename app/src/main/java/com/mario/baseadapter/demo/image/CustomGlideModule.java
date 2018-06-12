package com.mario.baseadapter.demo.image;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Date: 2017-11-13
 * Time: 15:39
 * Description:自定义
 */
@GlideModule
public class CustomGlideModule extends AppGlideModule {
    //基本信息配置
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .build();
        //设置手机默认推荐缓存大小
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
        // 自定义缓存大小.
        int memoryCacheSizeBytes = 1024 * 1024 * 20; // 20mb
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
        //自定义内置磁盘缓存大小(可以指明路径)
        int diskCacheSizeBytes = 1024 * 1024 * 100; // 100 MB
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));

    }

    //isManifestParsingEnabled 设置清单解析，设置为false，避免添加相同的modules两次
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

}
