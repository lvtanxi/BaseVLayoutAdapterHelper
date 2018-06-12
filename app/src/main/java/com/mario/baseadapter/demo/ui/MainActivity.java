package com.mario.baseadapter.demo.ui;

import android.content.Intent;
import android.view.View;

import com.mario.baseadapter.VBaseAdapter;
import com.mario.baseadapter.demo.R;
import com.mario.baseadapter.demo.base.BaseRecyclerActivity;
import com.mario.baseadapter.demo.data.AnalogData;
import com.mario.baseadapter.demo.model.MainModel;
import com.mario.baseadapter.holder.VBaseHolderHelper;
import com.mario.baseadapter.listener.OnItemClickListener;

public class MainActivity extends BaseRecyclerActivity<MainModel> {
    VBaseAdapter<MainModel> baseAdapter;
    @Override
    protected int loadLayoutId() {
        return R.layout.common_recyclerview;
    }

    @Override
    protected void initData() {
        super.initData();
        baseAdapter = new VBaseAdapter<MainModel>(R.layout.recyc_one_item) {
            @Override
            protected void convert(VBaseHolderHelper helper, MainModel mainModel, int position) {
                helper.setText(R.id.tvItem, mainModel.getName());
            }
        };
        baseAdapter.addItems(AnalogData.analogMainModel());
        mDelegateAdapter.addAdapter(baseAdapter);
    }

    @Override
    protected void bindListener() {
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableAutoLoadMore(false);
        baseAdapter.addOnItemClickListener(new OnItemClickListener<MainModel>() {
            @Override
            public void onItemClick(View view, int position, MainModel mainModel) {
                startActivity(new Intent(MainActivity.this, mainModel.getCl()));
            }
        });
    }


}
