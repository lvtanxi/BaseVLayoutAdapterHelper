
package com.mario.baseadapter.listener;

import android.view.View;
/**
 * Date: 2017-11-08
 * Time: 17:59
 * Description: itemView 中某个子View的点击事件
 */
public interface OnItemChildClickListener {
    /**
     * 回调
     * @param view 点击View
     * @param position 点击下标
     */
    void onItemChildClick(View view, int position);
}