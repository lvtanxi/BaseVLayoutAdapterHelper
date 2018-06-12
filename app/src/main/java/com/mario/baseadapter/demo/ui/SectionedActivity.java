package com.mario.baseadapter.demo.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.mario.baseadapter.VBaseSectionedAdapter;
import com.mario.baseadapter.decoration.SectionDecoration;
import com.mario.baseadapter.demo.R;
import com.mario.baseadapter.demo.base.BaseRecyclerActivity;
import com.mario.baseadapter.demo.data.AnalogData;
import com.mario.baseadapter.demo.image.CustomImageLoader;
import com.mario.baseadapter.demo.model.Section2Model;
import com.mario.baseadapter.demo.util.RxSchedulers;
import com.mario.baseadapter.holder.VBaseHolderHelper;
import com.mario.baseadapter.listener.OnChildItemClickListener;
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


public class SectionedActivity extends BaseRecyclerActivity {
    private VBaseSectionedAdapter<Section2Model> mSectionedAdapter;
    int pageNo;

    @Override
    protected void initData() {
        super.initData();
        setTitle("分组");
        mSectionedAdapter = new VBaseSectionedAdapter<Section2Model>(R.layout.multilt_text_view, R.layout.recyc_one_item, new GridLayoutHelper(3)) {
            @Override
            public int getItemChildCount(int section) {
                return getItem(section).getVideos().size();
            }

            @Override
            public void convertChildItem(VBaseHolderHelper helper, int section, int relativePosition, int absoPsition) {
                Section2Model.Video video = getItem(section).getVideos().get(relativePosition);
                helper.setText(R.id.tvItem, video.getName())
                      .setText(R.id.tvSubItem,video.getImg());
            }

            @Override
            protected void convert(VBaseHolderHelper helper, Section2Model section2Model, int position) {
                helper.setText(R.id.tv, section2Model.getHeader())
                        .setVisible(R.id.more, section2Model.isMroe());
            }
        };
        addEmptyView();
        mRecyclerView.addItemDecoration(new SectionDecoration(R.layout.multilt_text_view));
    }
    private void addEmptyView() {
        // 初始化HeaderView
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
        mSectionedAdapter.addEmptyView(headerView);
        mDelegateAdapter.addAdapter(mSectionedAdapter.getTargetAdapter());
    }

    @Override
    protected void bindListener() {
        mRefreshLayout.setEnableFooterTranslationContent(false);
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

        mSectionedAdapter.addOnItemClickListener(new OnItemClickListener<Section2Model>() {
            @Override
            public void onItemClick(View view, int position, Section2Model section2Model) {
                Toast.makeText(SectionedActivity.this, section2Model.getHeader(), Toast.LENGTH_SHORT).show();

            }
        });
        mSectionedAdapter.addOnChildItemClickListener(new OnChildItemClickListener() {
            @Override
            public void onChildItemClick(int section, int childPosition, int absolutePosition) {
                Toast.makeText(SectionedActivity.this, "section=" + section + ";childPosition=" + childPosition, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData(final boolean refresh) {
        Observable.timer(5, TimeUnit.SECONDS).compose(new Observable.Transformer<Long, List<Section2Model>>() {
            @Override
            public Observable<List<Section2Model>> call(Observable<Long> longObservable) {
                return Observable.create(new Observable.OnSubscribe<List<Section2Model>>() {
                    @Override
                    public void call(Subscriber<? super List<Section2Model>> subscriber) {
                        subscriber.onNext(pageNo == 0 ? new ArrayList<Section2Model>() : AnalogData.analogSection2Model(pageNo));
                        subscriber.onCompleted();
                    }
                });
            }
        }).compose(RxSchedulers.<List<Section2Model>>io2main())
                .subscribe(new Action1<List<Section2Model>>() {
                    @Override
                    public void call(List<Section2Model> section2Models) {
                        mSectionedAdapter.addItems(section2Models, refresh);
                        stopRefresh(refresh);
                    }
                });
    }

    private void stopRefresh(boolean refresh) {
        pageNo++;
        if (refresh) {
            if (pageNo>3)
                pageNo=0;
            mRefreshLayout.finishRefresh();
            return;
        }
        mRefreshLayout.finishLoadmore();
    }

    @Override
    protected void onProcessLogic() {
        mRefreshLayout.autoRefresh();
    }
}
