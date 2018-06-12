package com.mario.baseadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.mario.baseadapter.holder.VBaseHolder;
import com.mario.baseadapter.holder.VBaseHolderHelper;
import com.mario.baseadapter.listener.OnItemChildClickListener;
import com.mario.baseadapter.listener.OnItemClickListener;
import com.mario.baseadapter.wrapper.VEmptyWrapper;
import com.mario.baseadapter.wrapper.VHeaderFooterWrapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;


/**
 * Date: 2017-11-08
 * Time: 13:34
 * Description: Adapter基类
 */
public class VBaseAdapter<T> extends DelegateAdapter.Adapter<VBaseHolder<T>> {
    //布局文件资源ID
    int mResLayout;
    //数据源
    List<T> mDatas;
    //布局管理器
    private LayoutHelper mLayoutHelper;
    //回调监听
    OnItemClickListener<T> mItemClickListener;
    //回调监听
    private OnItemChildClickListener mItemChildClickListener;
    //继承VBaseHolder的Holder
    private Class<? extends VBaseHolder> mClazz;

    //目标Adapter
    private DelegateAdapter.Adapter mTargetAdapter;
    //统计有多少头布局
    private int mHeaderCount;

    private boolean mIsSingleLayoutHelper;

    /**
     * 构造函数分了四种
     * 1、手动设置VBaseHolder
     * 2、默认LinearLayoutHelper
     * 3、自定义LayoutHelper
     * 4、把初始数据也传入
     */
    public VBaseAdapter() {
        this(0);
    }

    public VBaseAdapter(int resLayout) {
        this(new ArrayList<T>(), resLayout, new LinearLayoutHelper());
    }

    public VBaseAdapter(int resLayout, LayoutHelper layoutHelper) {
        this(new ArrayList<T>(), resLayout, layoutHelper);
    }


    public VBaseAdapter(List<T> mDatas, int resLayout, LayoutHelper layoutHelper) {
        this.mLayoutHelper = layoutHelper;
        this.mResLayout = resLayout;
        this.mDatas = mDatas;
        this.mIsSingleLayoutHelper = layoutHelper instanceof SingleLayoutHelper;
    }


    /**
     * 添加 item选项
     */
    public void addItems(List<T> items) {
        if (items != null && !items.isEmpty()) {
            if (mDatas.addAll(items))
                notifyDataSetChangedWrapper();
        }
    }

    /**
     * 添加 item选项 ，isRefresh 为true的时候会清除原来的数据
     */
    public void addItems(List<T> items, boolean isRefresh) {
        if (isRefresh) {
            mDatas.clear();
            notifyDataSetChangedWrapper();
        }
        addItems(items);
    }

    /**
     * 添加 item选项
     */

    public void addItem(T item) {
        mDatas.add(item);
        notifyDataSetChangedWrapper();
    }

    @Override
    public int getItemViewType(int position) {
        return mResLayout;
    }

    /**
     * 手动设置布局
     */
    public VBaseAdapter<T> setLayout(int mResLayout) {
        if (mResLayout == 0) {
            throw new RuntimeException("res is null,please check your params !");
        }
        this.mResLayout = mResLayout;
        return this;
    }

    /**
     * 手动设置LayoutHelper
     */
    public VBaseAdapter<T> setLayoutHelper(LayoutHelper layoutHelper) {
        this.mLayoutHelper = layoutHelper;
        this.mIsSingleLayoutHelper = layoutHelper instanceof SingleLayoutHelper;
        return this;
    }

    /**
     * 绑定ItemView的点击事件
     */
    public void addOnItemClickListener(OnItemClickListener<T> listener) {
        this.mItemClickListener = listener;
    }

    /**
     * 绑定ItemView的ChildView点击事件
     */
    public void addOnItemChildClickListener(OnItemChildClickListener listener) {
        this.mItemChildClickListener = listener;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        if (mLayoutHelper == null)
            throw new RuntimeException("LayoutHelper is null,please check your params !");
        //判断是否需要监听GridLayoutHelper换行的事情
        onAttachedLayoutHelper(mLayoutHelper);
        return mLayoutHelper;
    }


    @Override
    public VBaseHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        //为0的时候默认是原始布局文件
        if (viewType == 0)
            viewType = mResLayout;
        VBaseHolder<T> baseHolder = null;
        //获取ItemView
        View inflate = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        //判断是否是穿的VBaseHolder
        if (mClazz != null) {
            if (mClazz.isMemberClass()) {
                if ((mClazz.getModifiers() != (Modifier.PUBLIC | Modifier.STATIC)) && mClazz.getModifiers() != (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL))
                    throw new RuntimeException(mClazz + " 是内部类，需要public static 修饰");
            }
            try {
                //根据VBaseHolder的构造器，反射生成VBaseHolder实体对象
                Constructor<? extends VBaseHolder> mClazzConstructor = mClazz.getConstructor(View.class);
                if (mClazzConstructor != null)
                    baseHolder = mClazzConstructor.newInstance(inflate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            baseHolder = new VBaseHolder<>(inflate);
        //绑定两个监听事件
        if (baseHolder != null) {
            baseHolder.addOnItemClickListener(mItemClickListener);
            baseHolder.getViewHolderHelper().addOnItemChildClickListener(mItemChildClickListener);
        }
        return baseHolder;
    }

    /**
     * 设置VBaseHolder class 模板
     */
    public VBaseAdapter<T> setHolder(Class<? extends VBaseHolder> mClazz) {
        if (mClazz == null) {
            throw new RuntimeException("clazz is null,please check your params !");
        }
        this.mClazz = mClazz;
        return this;
    }

    @Override
    public void onBindViewHolder(VBaseHolder<T> holder, int position) {
        //设置ItemView点击时的数据
        holder.setData(position, getItem(position));
        //判断是什么形态的VBaseHolder，然后渲染数据
        if (mClazz == null)
            convert(holder.getViewHolderHelper(), getItem(position), position);
        else
            holder.convert(holder.getViewHolderHelper(), getItem(position), position);
    }

    /**
     * 根据下标获取data
     */
    public T getItem(int position) {
        return mDatas.get(position);
    }

    /**
     * 获取目标adapter。若果我们有包装类，所以设置的时候请调用此方法
     */
    public DelegateAdapter.Adapter getTargetAdapter() {
        return mTargetAdapter == null ? this : mTargetAdapter;
    }

    /**
     * 添加头View
     */
    public void addHeaderView(View headerView) {
        getHeaderFooterWrapper().addHeaderView(headerView);
        mHeaderCount++;
    }

    /**
     * 获取头、尾巴、空包装类，并设置mTargetAdapter
     */
    private VHeaderFooterWrapper getHeaderFooterWrapper() {
        if (mTargetAdapter == null || !(mTargetAdapter instanceof VHeaderFooterWrapper)) {
            synchronized (this) {
                if (mTargetAdapter == null)
                    mTargetAdapter = new VHeaderFooterWrapper(this);
            }
        }
        return (VHeaderFooterWrapper) mTargetAdapter;
    }

    /**
     * 添加尾View
     */
    public void addFooterView(View footerView) {
        getHeaderFooterWrapper().addFooterView(footerView);
    }

    /**
     * 添加空View
     */
    public void addEmptyView(View emptyView) {
        addEmptyView(emptyView, true);
    }

    /**
     * 添加空View
     *
     * @param isEmptyWrapper 因为有两个包装类有空布局，所以判断用哪一个
     */
    public void addEmptyView(View emptyView, boolean isEmptyWrapper) {
        if (isEmptyWrapper)
            getEmptyWrapper().addEmptyView(emptyView);
        else
            getHeaderFooterWrapper().addEmptyView(emptyView);
    }

    /**
     * 获取空View的包装类
     */
    private VEmptyWrapper getEmptyWrapper() {
        if (mTargetAdapter == null || !(mTargetAdapter instanceof VEmptyWrapper)) {
            synchronized (this) {
                if (mTargetAdapter == null)
                    mTargetAdapter = new VEmptyWrapper(this);
            }
        }
        return (VEmptyWrapper) mTargetAdapter;
    }

    /**
     * 数据变化调用这个，因为有包装类，所以我们只能调用TargetAdapter来实现数据刷新
     */
    public void notifyDataSetChangedWrapper() {
        getTargetAdapter().notifyDataSetChanged();
    }

    /**
     * 填数据
     */
    protected void convert(VBaseHolderHelper helper, T t, int position) {
    }

    @Override
    public int getItemCount() {
        if (mIsSingleLayoutHelper && !dataIsEmpty())
            return 1;
        return mDatas.size();
    }

    /**
     *
     */
    public LayoutHelper getLayoutHelper() {
        return mLayoutHelper;
    }

    /**
     * 清除所有数据
     */
    public void clearData() {
        mDatas.clear();
        notifyDataSetChangedWrapper();
    }

    /**
     * 判断是否换行
     */
    public boolean isLineFeed(int position) {
        return false;
    }

    /**
     * 判断是否需要监听GridLayoutHelper换行的事情(好像应该放在BaseAdapter中)
     */
    private void onAttachedLayoutHelper(LayoutHelper layoutHelper) {
        if (layoutHelper instanceof GridLayoutHelper) {
            final GridLayoutHelper gridLayoutHelper = (GridLayoutHelper) layoutHelper;
            gridLayoutHelper.setAutoExpand(false);
            gridLayoutHelper.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isLineFeed(position - getStartPosition() - mHeaderCount))
                        return gridLayoutHelper.getSpanCount();
                    return 1;
                }
            });
        }
    }

    public boolean dataIsEmpty() {
        return mDatas == null || mDatas.size() == 0;
    }

}
