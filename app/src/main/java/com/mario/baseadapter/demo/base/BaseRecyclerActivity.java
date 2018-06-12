package com.mario.baseadapter.demo.base;

import android.support.v7.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.mario.baseadapter.demo.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;


/**
 * User: 吕勇
 * Date: 2017-05-04
 * Time: 14:52
 * Description:列表基类(layout中请包含common_recyclerview中的相关id)
 */
public abstract class BaseRecyclerActivity<T> extends BaseActivity {
    protected RefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected VirtualLayoutManager mVirtualLayoutManager;
    protected DelegateAdapter mDelegateAdapter;

    @Override
    protected int loadLayoutId() {
        return R.layout.common_title_recyclerview;
    }

    @Override
    protected void initViews() {
        mRefreshLayout = $(R.id.common_refresh_layout);
        mRecyclerView = $(R.id.common_recyclerview);
    }



    @Override
    protected void initData() {
        mRefreshLayout.setDisableContentWhenLoading(true);
        //设置LayoutManager
        mVirtualLayoutManager=new VirtualLayoutManager(this);
        mRecyclerView.setLayoutManager(mVirtualLayoutManager);
        //总adapter
        mDelegateAdapter= new DelegateAdapter(mVirtualLayoutManager, true);
        mRecyclerView.setAdapter(mDelegateAdapter);
    }




    @Override
    protected void onDestroy() {
        mRecyclerView = null;
        super.onDestroy();
    }



}
