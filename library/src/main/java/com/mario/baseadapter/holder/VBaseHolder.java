
package com.mario.baseadapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mario.baseadapter.listener.OnItemClickListener;
import com.mario.baseadapter.listener.OnNoDoubleClickListener;

/**
 * Date: 2017-11-09
 * Time: 10:46
 * Description: VBaseHolder 基类，主要处理itemView的点击事件与回调
 */
public class VBaseHolder<T> extends RecyclerView.ViewHolder {
    private VBaseHolderHelper mViewHolderHelper;
    protected T mData;
    protected int mPosition;

    public VBaseHolder(View itemView) {
        super(itemView);
        mViewHolderHelper = new VBaseHolderHelper(this);
    }


    public void addOnItemClickListener(final OnItemClickListener<T> itemClickListener) {
        if (itemClickListener != null) {
            itemView.setOnClickListener(new OnNoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (v.getId() == VBaseHolder.this.itemView.getId()) {
                        itemClickListener.onItemClick(v, mPosition, mData);
                    }
                }
            });
        }
    }

    public VBaseHolderHelper getViewHolderHelper() {
        return mViewHolderHelper;
    }

    public void setData(int position, T mData) {
        this.mData = mData;
        this.mPosition = position;
    }

    public void onBindItem(VBaseHolderHelper helper, T t, int position) {

    }

    public int getAdapterPositionWrapper() {
        return mPosition;
    }

}