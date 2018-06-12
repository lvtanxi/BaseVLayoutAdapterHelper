package com.mario.baseadapter.wrapper;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.mario.baseadapter.VBaseAdapter;
import com.mario.baseadapter.holder.VBaseHolder;


/**
 * Date: 2017-11-09
 * Time: 10:46
 * Description: 包装类基类
 */

abstract class VBaseWrapper extends  DelegateAdapter.Adapter<VBaseHolder> {
    //被包装的adapter
    VBaseAdapter mInnerAdapter;

    VBaseWrapper(VBaseAdapter innerAdapter) {
        mInnerAdapter = innerAdapter;
    }


    @Override
    public LayoutHelper onCreateLayoutHelper() {
        onAttachedLayoutHelper(mInnerAdapter.getLayoutHelper());
        return mInnerAdapter.getLayoutHelper();
    }


    /**
     * 处理GridLayoutHelper中手动换行的。(后期可以加个开关)
     * @param layoutHelper VBaseAdapter 的LayoutHelper
     */
    private void onAttachedLayoutHelper(LayoutHelper layoutHelper) {
        if (layoutHelper instanceof GridLayoutHelper) {
            final GridLayoutHelper gridLayoutHelper = (GridLayoutHelper) layoutHelper;
            gridLayoutHelper.setAutoExpand(false);
            gridLayoutHelper.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return doSpanSize(position-getStartPosition(),gridLayoutHelper.getSpanCount());
                }
            });
        }
    }

    /**
     * 计算返回多少格数
     * @param position 当前position
     * @param spanCount GridLayoutHelper 总数
     */
    protected int doSpanSize(int position,int spanCount) {
        if (isLineFeed(position)||mInnerAdapter.isLineFeed(position))
            return spanCount;
        return 1;
    }

    /**
     * 判断是否需要换行
     * @param position  当前position
     */
    protected abstract boolean isLineFeed(int position);
}
