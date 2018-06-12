package com.mario.baseadapter;

import android.support.annotation.LayoutRes;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.mario.baseadapter.holder.VBaseHolder;
import com.mario.baseadapter.holder.VBaseHolderHelper;
import com.mario.baseadapter.listener.OnChildItemClickListener;
import com.mario.baseadapter.listener.OnNoDoubleClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Date: 2017-11-08
 * Time: 13:34
 * Description: 分组adapter(设计相对简单)
 */
public abstract class VBaseSectionedAdapter<M> extends VBaseAdapter<M> {
    //存储Sectioned对应的Position
    private final SparseIntArray mHeaderLocationMap;
    //存储Position对应的Sectioned
    private final SparseIntArray mSectionMap;
    //存储Position对应的子View的Position
    private final SparseIntArray mPositionMap;
    //条目布局
    private int mSectionId;
    //回调监听
    private OnChildItemClickListener mChildItemClickListener;


    /**
     * 两个构造
     */
    public VBaseSectionedAdapter(@LayoutRes int sectionId, @LayoutRes int normalId) {
        this(sectionId, normalId, new LinearLayoutHelper());
    }

    public VBaseSectionedAdapter(@LayoutRes int sectionId, @LayoutRes int normalId, LayoutHelper layoutHelper) {
        super(new ArrayList<M>(), normalId, layoutHelper);
        mSectionId = sectionId;
        mHeaderLocationMap = new SparseIntArray();
        mSectionMap = new SparseIntArray();
        mPositionMap = new SparseIntArray();
    }


    @Override
    public int getItemViewType(int position) {
        //判断是条目还是子view
        return isSectioned(position) ? mSectionId : mResLayout;
    }

    @Override
    public void onBindViewHolder(VBaseHolder<M> holder, int position) {
        //判断是否条目
        if (isSectioned(position)) {
            //获取Sectioned
            position = mHeaderLocationMap.get(position);
            //设置一个Sectioned给holder.itemView
            holder.itemView.setTag(position);
            //渲染条目
            convert(holder.getViewHolderHelper(), getItem(position), position);
        } else {
            //获取Sectioned
            final int section = getSectionIndex(position);
            //获取全局位置
            final int absPos = position - (section + 1);
            //设置一个Sectioned给holder.itemView
            holder.itemView.setTag(position);
            //标识一下是子view
            holder.itemView.setContentDescription("child");
            //渲染ziview
            convertChildItem(holder.getViewHolderHelper(), section, mPositionMap.get(position), absPos);
        }
    }


    @Override
    public VBaseHolder<M> onCreateViewHolder(ViewGroup parent, int viewType) {
        VBaseHolder<M> baseViewHolder = super.onCreateViewHolder(parent, viewType);
        bindAllClickListener(baseViewHolder, viewType);
        return baseViewHolder;
    }

    /**
     * 监听事件(这里可能需要处理一下，减少不必要的监听事件)
     */
    private void bindAllClickListener(VBaseHolder<M> baseViewHolder, int viewType) {
        //首先是取消在VBaseAdapter绑定的点击事件
        baseViewHolder.itemView.setOnClickListener(null);
        if (mSectionId == viewType && mItemClickListener == null)
            return;
        else if (mResLayout == viewType && mChildItemClickListener == null)
            return;

        //重新绑定我们的点击事件
        baseViewHolder.itemView.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                //获取position
                Object vTag = view.getTag();
                //判断是否是我们想要的
                if (vTag != null && vTag instanceof Integer) {
                    //处理position
                    int realPosition = Integer.valueOf(vTag.toString());
                    //判断是否是条目(之前字条目设置了标识的)
                    if (view.getContentDescription() == null) {
                        //回调
                        if (mItemClickListener != null)
                            mItemClickListener.onItemClick(view, realPosition, getItem(realPosition));
                    } else {
                        //判断子View的事件问题
                        if (mChildItemClickListener != null) {
                            //获取条目position
                            final int section = getSectionIndex(realPosition);
                            //获取据对位置
                            final int absPos = realPosition - (section + 1);
                            //回调
                            mChildItemClickListener.onChildItemClick(section, mPositionMap.get(realPosition), absPos);
                        }
                    }
                }

            }
        });
    }

    /**
     * 获取条目中子View的个数
     */
    protected abstract int getItemChildCount(int section);

    /**
     * 渲染条子View
     *
     * @param helper        VBaseHolderHelper
     * @param section       条目position
     * @param childPosition 条目下的childPosition
     * @param absoPsition   据对position
     */
   protected abstract void convertChildItem(VBaseHolderHelper helper, int section, int childPosition, int absoPsition);

    /**
     * 判断是否是条目
     */
    private boolean isSectioned(int position) {
        return mHeaderLocationMap.get(position, -1) != -1;
    }

    /**
     * 根据position获取条目位置
     */
    private int getSectionIndex(int itemPosition) {
        //判断是否已经计算过了，避免重读计算
        if (mSectionMap.get(itemPosition, -1) == -1 || mPositionMap.get(itemPosition, -1) == -1) {
            //枷锁计算
            synchronized (mHeaderLocationMap) {
                Integer lastSectionIndex = -1;
                int count = mHeaderLocationMap.size();
                for (int i = 0; i < count; i++) {
                    //得到条目
                    if (itemPosition > mHeaderLocationMap.keyAt(i)) {
                        lastSectionIndex = mHeaderLocationMap.keyAt(i);
                    } else {
                        break;
                    }
                }
                //存储次itemPosition的条目位置和itemPosition子view的位置
                mSectionMap.put(itemPosition, mHeaderLocationMap.get(lastSectionIndex));
                mPositionMap.put(itemPosition, itemPosition - lastSectionIndex - 1);
            }
        }
        return mSectionMap.get(itemPosition);

    }

    @Override
    public final int getItemCount() {
        //计算ItemCount
        int count = 0;
        for (int s = 0; s < mDatas.size(); s++) {
            int itemCount = getItemChildCount(s);
            if (itemCount > 0) {
                //这里先存储条目位置
                mHeaderLocationMap.put(count, s);
                count += itemCount + 1;
            }
        }
        return count;
    }

    @Override
    public void addItems(List<M> items) {
        super.addItems(items);
        //暂时放到这里
        mHeaderLocationMap.clear();
        mSectionMap.clear();
        mPositionMap.clear();
    }

    @Override
    public boolean isLineFeed(int position) {
        return isSectioned(position);
    }


    public void addOnChildItemClickListener(OnChildItemClickListener childItemClickListener) {
        mChildItemClickListener = childItemClickListener;
    }


}
