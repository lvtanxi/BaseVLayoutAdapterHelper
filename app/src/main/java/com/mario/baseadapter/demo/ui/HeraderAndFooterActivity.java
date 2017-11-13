package com.mario.baseadapter.demo.ui;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.mario.baseadapter.VBaseAdapter;
import com.mario.baseadapter.demo.R;
import com.mario.baseadapter.demo.base.BaseRecyclerActivity;
import com.mario.baseadapter.demo.data.AnalogData;
import com.mario.baseadapter.demo.util.RxSchedulers;
import com.mario.baseadapter.holder.VBaseHolderHelper;
import com.mario.baseadapter.listener.OnItemChildClickListener;
import com.mario.baseadapter.listener.OnItemClickListener;
import com.mario.baseadapter.listener.OnNoDoubleClickListener;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;


/**
 * Date: 2017-11-13
 * Time: 17:23
 * Description:
 */
public class HeraderAndFooterActivity extends BaseRecyclerActivity<String> {
    private VBaseAdapter<String> mBaseAdapter;
    @Override
    protected void initData() {
        super.initData();
        initAdapter();
    }

    private void initAdapter() {
        mBaseAdapter = new VBaseAdapter<String>(R.layout.recyc_one_item) {
            @Override
            protected void onBindItem(VBaseHolderHelper helper, String s, int position) {
                helper.setText(R.id.tvItem,s)
                        .setItemChildClickListener(R.id.imgItem);
            }
        };
        testHaveHeaderAndFooterAdapter();//添加头或者尾部文件需要在addAdapter之前
        mDelegateAdapter.addAdapter(mBaseAdapter.getTargetAdapter());
    }

    private void testHaveHeaderAndFooterAdapter() {
        TextView header1Tv = new TextView(this);
        header1Tv.setBackgroundColor(Color.parseColor("#E15B5A"));
        header1Tv.setTextColor(Color.WHITE);
        header1Tv.setGravity(Gravity.CENTER);
        header1Tv.setPadding(30, 30, 30, 30);
        header1Tv.setText("头部1");
        header1Tv.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                showToast("点击了头部1");
            }
        });
        // 当时 LinearLayoutManager 时，需要设置一下布局参数的宽度为填充父窗体，否则 header 和 footer 的宽度会是包裹内容
        header1Tv.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        mBaseAdapter.addHeaderView(header1Tv);

        TextView footer1Tv = new TextView(this);
        footer1Tv.setBackgroundColor(Color.parseColor("#6C9FFC"));
        footer1Tv.setTextColor(Color.WHITE);
        footer1Tv.setGravity(Gravity.CENTER);
        footer1Tv.setPadding(30, 30, 30, 30);
        footer1Tv.setText("底部1");
        footer1Tv.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                showToast("点击了底部1");
            }
        });
        footer1Tv.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        mBaseAdapter.addFooterView(footer1Tv);

        TextView footer2Tv = new TextView(this);
        footer2Tv.setBackgroundColor(Color.parseColor("#51535B"));
        footer2Tv.setTextColor(Color.WHITE);
        footer2Tv.setGravity(Gravity.CENTER);
        footer2Tv.setPadding(50, 50, 50, 50);
        footer2Tv.setText("底部2");
        footer2Tv.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                showToast("点击了底部2");
            }
        });
        footer2Tv.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        mBaseAdapter.addFooterView(footer2Tv);

    }

    @Override
    protected void bindListener() {
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadmore(false);
        mBaseAdapter.addOnItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void onItemClick(View view, int i, String s) {
                showToast(s);
            }
        });
        mBaseAdapter.addOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(View view, int i) {
                showToast("this is item : " + i+" say ,I'm a image ");
            }
        });
    }


    @Override
    protected void onProcessLogic() {
        Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                subscriber.onNext(AnalogData.analogString(1));
                subscriber.onCompleted();
            }
        }).compose(RxSchedulers.<List<String>>io2main())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> normalModels) {
                        mBaseAdapter.addItems(normalModels);
                    }
                });
    }
}
