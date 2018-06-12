package com.mario.baseadapter.wrapper;

import android.support.v4.util.SparseArrayCompat;
import android.view.View;
import android.view.ViewGroup;

import com.mario.baseadapter.VBaseAdapter;
import com.mario.baseadapter.holder.VBaseHolder;

/**
 * Date: 2017-11-09
 * Time: 10:46
 * Description: 空View的包装类
 */
public class VEmptyWrapper extends VBaseWrapper {
    //EmptyView标识
    private static final int BASE_ITEM_TYPE_EMPTY = Integer.MAX_VALUE - 1;
    //所有的EmptyView
    private SparseArrayCompat<View> mEmptyViews = new SparseArrayCompat<>();

    public VEmptyWrapper(VBaseAdapter innerAdapter) {
        super(innerAdapter);
    }

    /**
     * 判断是否是空View
     */
    private boolean isEmpty() {
        return mEmptyViews.size() > 0 && mInnerAdapter.getItemCount() == 0;
    }


    @Override
    public VBaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //判断是否为EmptyView，是的话手动创建一个新的VBaseHolder
        if (isEmpty() && mEmptyViews.get(viewType) != null)
            return new VBaseHolder(mEmptyViews.get(viewType));
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }




    @Override
    public int getItemViewType(int position) {
        //判断是否为EmptyView，是的话，返回EmptyView标识
        if (isEmpty())
            return BASE_ITEM_TYPE_EMPTY;
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(VBaseHolder holder, int position) {
        //判断是否为EmptyView，是的话就返回不做处理
        if (isEmpty())
            return;
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        if (isEmpty()) return mEmptyViews.size();
        return mInnerAdapter.getItemCount();
    }

    /**
     * 添加EmptyView
     */
    public void addEmptyView(View view) {
        mEmptyViews.put(mEmptyViews.size() + BASE_ITEM_TYPE_EMPTY, view);
    }

    /**
     * 一般情况EmptyView需要满行显示
     */
    @Override
    protected boolean isLineFeed(int position) {
        return isEmpty();
    }

}
