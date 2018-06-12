package com.mario.baseadapter.demo.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mario.baseadapter.VBaseAdapter;
import com.mario.baseadapter.demo.R;
import com.mario.baseadapter.demo.base.BaseRecyclerActivity;
import com.mario.baseadapter.demo.data.AnalogData;
import com.mario.baseadapter.demo.image.CustomImageLoader;
import com.mario.baseadapter.demo.util.RxSchedulers;
import com.mario.baseadapter.holder.VBaseHolderHelper;
import com.mario.baseadapter.listener.OnItemChildClickListener;
import com.mario.baseadapter.listener.OnItemClickListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Date: 2017-11-13
 * Time: 17:23
 * Description:
 */

public class EmptyActivity extends BaseRecyclerActivity<String> {
    int pageNo = 0;
    private VBaseAdapter<String> mBaseAdapter;
    @Override
    protected void initData() {
        super.initData();
        setTitle("空布局");
        mRefreshLayout.setEnableAutoLoadMore(true);
        mBaseAdapter = new VBaseAdapter<String>(R.layout.recyc_one_item) {
            @Override
            protected void convert(VBaseHolderHelper helper, String s, int position) {
                helper.setText(R.id.tvItem,s)
                .setItemChildClickListener(R.id.imgItem);
            }
        };
        addEmptyView();//添加头或者尾部文件需要在addAdapter之前
        mDelegateAdapter.addAdapter(mBaseAdapter.getTargetAdapter());
    }

    @Override
    protected void bindListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadData(true);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                loadData(false);
            }
        });
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
        mRefreshLayout.autoRefresh();
    }

    private void loadData(final boolean refresh) {
        Observable.timer(5, TimeUnit.SECONDS).compose(new Observable.Transformer<Long, List<String>>() {
            @Override
            public Observable<List<String>> call(Observable<Long> longObservable) {
                return Observable.create(new Observable.OnSubscribe<List<String>>() {
                    @Override
                    public void call(Subscriber<? super List<String>> subscriber) {
                        subscriber.onNext(pageNo == 0 ? new ArrayList<String>() : AnalogData.analogString(pageNo));
                        subscriber.onCompleted();
                    }
                });
            }
        }).compose(RxSchedulers.<List<String>>io2main())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> section2Models) {
                        mBaseAdapter.addItems(section2Models, refresh);
                        stopRefresh(refresh);
                    }
                });
    }

    private void stopRefresh(boolean refresh) {
        pageNo++;
        if (refresh) {
            if (pageNo > 3)
                pageNo = 0;
            mRefreshLayout.finishRefresh();
            return;
        }
        mRefreshLayout.finishLoadmore();
    }

    private void addEmptyView() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.recyc_banner, (ViewGroup) mRecyclerView.getParent(), false);

        final List<String> urls = new ArrayList<>();
        urls.add("http://img3.imgtn.bdimg.com/it/u=2674591031,2960331950&fm=23&gp=0.jpg");
        urls.add("http://img5.imgtn.bdimg.com/it/u=3639664762,1380171059&fm=23&gp=0.jpg");
        urls.add("http://img0.imgtn.bdimg.com/it/u=1095909580,3513610062&fm=23&gp=0.jpg");
        urls.add("http://img4.imgtn.bdimg.com/it/u=1030604573,1579640549&fm=23&gp=0.jpg");
        urls.add("http://img5.imgtn.bdimg.com/it/u=2583054979,2860372508&fm=23&gp=0.jpg");
        Banner banner = headerView.findViewById(R.id.banner);
        banner.setImages(urls);
        //设置图片加载器
        banner.setImageLoader(new CustomImageLoader());
        banner.start();
        mBaseAdapter.addEmptyView(headerView, true);
    }
}
