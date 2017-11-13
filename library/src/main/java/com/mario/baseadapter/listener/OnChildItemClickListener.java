package com.mario.baseadapter.listener;

/**
 * Date: 2017-11-08
 * Time: 17:59
 * Description: 分组子itemView的点击事件
 */

public interface OnChildItemClickListener {
    /**
     * 点击回调
     * @param section 分组position
     * @param childPosition section下的子itemView下标
     * @param absolutePosition 在adapter的位置(好像有问题，但是没怎么用)
     */
    void onChildItemClick(int section, int childPosition, int absolutePosition);
}
