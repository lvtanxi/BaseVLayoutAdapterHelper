package com.mario.baseadapter.demo.ui;

import android.graphics.Color;
import android.view.View;

import com.alibaba.android.vlayout.layout.OnePlusNLayoutHelper;
import com.mario.baseadapter.VBaseAdapter;
import com.mario.baseadapter.demo.R;
import com.mario.baseadapter.demo.base.BaseRecyclerActivity;
import com.mario.baseadapter.demo.data.AnalogData;
import com.mario.baseadapter.demo.model.NormalModel;
import com.mario.baseadapter.demo.util.RxSchedulers;
import com.mario.baseadapter.holder.VBaseHolderHelper;
import com.mario.baseadapter.listener.OnItemClickListener;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Date: 2017-11-14
 * Time: 08:49
 * Description:
 */

public class OnePlusNLayoutActivity extends BaseRecyclerActivity<NormalModel> {
    private VBaseAdapter<NormalModel> mBaseAdapter;
    private VBaseAdapter<NormalModel> mOnePlusNLayoutAdapter;

    @Override
    protected void initData() {
        super.initData();
        setTitle("1拖N布局");
        initAdapter();
    }

    private void initAdapter() {
        mBaseAdapter= new VBaseAdapter<NormalModel>(R.layout.recyc_one_item) {
            @Override
            protected void convert(VBaseHolderHelper helper, NormalModel model, int position) {
                helper.setText(R.id.tvSubItem, model.getTitle());
            }
        };
        /**
         设置1拖N布局
         */
        OnePlusNLayoutHelper onePlusNLayoutHelper = new OnePlusNLayoutHelper(3);
        // 在构造函数里传入显示的Item数
        // 最多是1拖2,即3个

        // 公共属性
        onePlusNLayoutHelper.setItemCount(3);// 设置布局里Item个数
        // onePlusNLayoutHelper.setPadding(20, 20, 20, 20);// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
        //onePlusNLayoutHelper.setMargin(20, 20, 20, 20);// 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
        onePlusNLayoutHelper.setBgColor(Color.WHITE);// 设置背景颜色
        mOnePlusNLayoutAdapter=new VBaseAdapter<>(R.layout.recyc_one_item,onePlusNLayoutHelper);
        mDelegateAdapter.addAdapter(mBaseAdapter.getTargetAdapter());
        mDelegateAdapter.addAdapter(mOnePlusNLayoutAdapter.getTargetAdapter());
    }

    @Override
    protected void onProcessLogic() {
        Observable.create(new Observable.OnSubscribe<List<NormalModel>>() {
            @Override
            public void call(Subscriber<? super List<NormalModel>> subscriber) {
                subscriber.onNext(AnalogData.analogNormalModel());
                subscriber.onCompleted();
            }
        }).compose(RxSchedulers.<List<NormalModel>>io2main())
                .subscribe(new Action1<List<NormalModel>>() {
                    @Override
                    public void call(List<NormalModel> normalModels) {
                        mBaseAdapter.addItems(normalModels);
                        mOnePlusNLayoutAdapter.addItems(normalModels);
                    }
                });
    }

    @Override
    protected void bindListener() {
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(false);
        mBaseAdapter.addOnItemClickListener(new OnItemClickListener<NormalModel>() {
            @Override
            public void onItemClick(View view, int i, NormalModel normalModel) {
                showToast("这是有文字的item"+i);
            }
        });
        mOnePlusNLayoutAdapter.addOnItemClickListener(new OnItemClickListener<NormalModel>() {
            @Override
            public void onItemClick(View view, int i, NormalModel normalModel) {
                showToast("OnePlusNLayout的item"+i);
            }
        });
    }
}
