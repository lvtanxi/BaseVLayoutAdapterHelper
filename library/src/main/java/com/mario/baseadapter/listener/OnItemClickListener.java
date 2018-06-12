package com.mario.baseadapter.listener;

import android.view.View;

/**
 * Date: 2017-11-08
 * Time: 13:34
 * Description: ItemView的点击事件
 */
public interface OnItemClickListener<T> {
    /**
     * 回调
     * @param view 点击的View
     * @param position itemView的位置
     * @param t adapter的泛型实体
     */
    void onItemClick(View view, int position, T t);
}
