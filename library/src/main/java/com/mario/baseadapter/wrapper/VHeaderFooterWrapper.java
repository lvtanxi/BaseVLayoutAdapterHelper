package com.mario.baseadapter.wrapper;

import android.support.v4.util.SparseArrayCompat;
import android.view.View;
import android.view.ViewGroup;

import com.mario.baseadapter.VBaseAdapter;
import com.mario.baseadapter.holder.VBaseHolder;


/**
 * Date: 2017-11-09
 * Time: 10:46
 * Description: 头、尾巴、空包装类
 */

public class VHeaderFooterWrapper extends VBaseWrapper {
    //三种状态的标识
    private static final int BASE_ITEM_TYPE_HEADER = 1024;
    private static final int BASE_ITEM_TYPE_FOOTER = 2048;
    private static final int BASE_ITEM_TYPE_EMPTY = 5096;
    //三种状态的view
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mEmptyViews = new SparseArrayCompat<>();


    public VHeaderFooterWrapper(VBaseAdapter innerAdapter) {
        super(innerAdapter);
    }


    @Override
    public VBaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据viewType返回对象的VBaseHolder
        if (mHeaderViews.get(viewType) != null) {
            return new VBaseHolder(mHeaderViews.get(viewType));
        } else if (mEmptyViews.get(viewType) != null) {
            return new VBaseHolder(mEmptyViews.get(viewType));
        } else if (mFootViews.get(viewType) != null) {
            return new VBaseHolder(mFootViews.get(viewType));
        } else {
            return mInnerAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //根据position判断类型
        if (isHeaderView(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isEmptyView(position)) {
            return mEmptyViews.keyAt(position - getHeadersCount());
        } else if (isFooterView(position)) {
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    /**
     * 返回被包装的adapter的item个数
     */
    public int getRealItemCount() {
        return mInnerAdapter.getItemCount() == 0 ? mEmptyViews.size() : mInnerAdapter.getItemCount();
    }

    @Override
    public void onBindViewHolder(VBaseHolder holder, int position) {
        //判断是否是三种状态
        if (isHeaderView(position) || isFooterView(position) || isEmptyView(position))
            return;
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    /**
     * 是否是头布局
     */
    private boolean isHeaderView(int position) {
        return position < getHeadersCount();
    }

    /**
     * 是否是尾布局
     */
    private boolean isFooterView(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }

    /**
     * 是否是空布局
     */
    private boolean isEmptyView(int position) {
        return position == getHeadersCount() && mInnerAdapter.getItemCount() == 0;
    }

    /**
     * 添加头布局
     */
    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }
    /**
     * 添加尾布局
     */
    public void addFooterView(View view) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }
    /**
     * 添加空布局
     */
    public void addEmptyView(View view) {
        mEmptyViews.put(mEmptyViews.size() + BASE_ITEM_TYPE_EMPTY, view);
    }

    /**
     * 获取头布局个数
     */
    public int getHeadersCount() {
        return mHeaderViews.size();
    }
    /**
     * 获取尾布局个数
     */
    private int getFootersCount() {
        return mFootViews.size();
    }

    @Override
    protected int doSpanSize(int position, int spanCount) {
        //这里要注意，调用被包装adapter的isLineFeed方式的时候需要去除头布局的个数
        if (isLineFeed(position) || mInnerAdapter.isLineFeed(position - getHeadersCount()))
            return spanCount;
        return 1;
    }

    /**
     * 获取真实 item 的索引
     *
     */
    public int getRealItemPosition(int position) {
        return position - getHeadersCount();
    }

    @Override
    protected boolean isLineFeed(int position) {
        int viewType = getItemViewType(position);
        return (mHeaderViews.get(viewType) != null || mEmptyViews.get(viewType) != null || mFootViews.get(viewType) != null);
    }

    /**
     * 是否是头或者尾布局
     */
    public boolean isHeaderViewOrFooterView(int childAdapterPosition) {
        return isHeaderView(childAdapterPosition) || isFooterView(childAdapterPosition);
    }
}