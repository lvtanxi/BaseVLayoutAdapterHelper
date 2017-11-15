package com.mario.baseadapter.demo.ui;

import android.content.res.AssetManager;
import android.view.View;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StaggeredGridLayoutHelper;
import com.google.gson.Gson;
import com.mario.baseadapter.VBaseAdapter;
import com.mario.baseadapter.VBaseSectionedAdapter;
import com.mario.baseadapter.decoration.SectionDecoration;
import com.mario.baseadapter.demo.R;
import com.mario.baseadapter.demo.base.BaseRecyclerActivity;
import com.mario.baseadapter.demo.data.AnalogData;
import com.mario.baseadapter.demo.holder.WaterHolder;
import com.mario.baseadapter.demo.image.CustomImageLoader;
import com.mario.baseadapter.demo.model.BannerBean;
import com.mario.baseadapter.demo.model.GridBean;
import com.mario.baseadapter.demo.model.HomeData;
import com.mario.baseadapter.demo.model.Section2Model;
import com.mario.baseadapter.demo.model.WaterCargo;
import com.mario.baseadapter.demo.util.RxSchedulers;
import com.mario.baseadapter.holder.VBaseHolderHelper;
import com.mario.baseadapter.listener.OnChildItemClickListener;
import com.mario.baseadapter.listener.OnItemClickListener;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;


public class TaobaoActivity extends BaseRecyclerActivity {
    private VBaseAdapter<BannerBean> bannerAdapter;
    private VBaseAdapter<GridBean> gridAdapter;
    private VBaseAdapter<WaterCargo> waterfallAdapter;
    private VBaseSectionedAdapter<Section2Model> mSectionedAdapter;
    @Override
    protected void initData() {
        super.initData();
        setTitle("淘宝首页");
        initAdapter();
    }

    private void initAdapter() {
        mRecyclerView.addItemDecoration(new SectionDecoration(R.layout.multilt_text_view));
        //各个adapter
        bannerAdapter = new VBaseAdapter<BannerBean>(R.layout.recyc_banner,new SingleLayoutHelper()) {
            @Override
            protected void convert(VBaseHolderHelper holder, BannerBean bannerBean, int position) {
                Banner banner = holder.getView(R.id.banner);
                banner.setImages(bannerBean.getPic_url());
                //设置图片加载器
                banner.setImageLoader(new CustomImageLoader());
                banner.start();
            }
        };
        VBaseAdapter<String> newsAdapter = new VBaseAdapter<String>(R.layout.recyc_news,new SingleLayoutHelper()) {
            @Override
            protected void convert(VBaseHolderHelper holder, String s, int position) {
                MarqueeView marqueeView1 = holder.getView(R.id.marqueeView1);
                MarqueeView marqueeView2 = holder.getView(R.id.marqueeView2);

                List<String> info1 = new ArrayList<>();
                info1.add("这个是用来搞笑的，不要在意这写小细节！");
                info1.add("这个是用来搞笑的，不要在意这写小细节！");

                List<String> info2 = new ArrayList<>();
                info2.add("这个是用来搞笑的，不要在意这写小细节！");
                info2.add("啦啦啦啦，我就是来搞笑的！");

                marqueeView1.startWithList(info1);
                marqueeView2.startWithList(info2);
                // 在代码里设置自己的动画
                marqueeView1.startWithList(info1, R.anim.anim_bottom_in, R.anim.anim_top_out);
                marqueeView2.startWithList(info2, R.anim.anim_bottom_in, R.anim.anim_top_out);
            }
        };
        newsAdapter.addItem("1");

        gridAdapter = new VBaseAdapter<GridBean>(R.layout.recyc_grid, getGridLayoutHelp()) {
            @Override
            protected void convert(VBaseHolderHelper helper, GridBean gridBean, int position) {
                helper.setText(R.id.func, gridBean.getFunction());
                CustomImageLoader.loadImage(TaobaoActivity.this,gridBean.getPic_url(),helper.getImageView(R.id.icon));
            }
        };
        waterfallAdapter = new VBaseAdapter<WaterCargo>()
                .setLayout(R.layout.recyc_water)
                .setHolder(WaterHolder.class)
                .setLayoutHelper(getWaterHelper());

        mSectionedAdapter = new VBaseSectionedAdapter<Section2Model>(R.layout.multilt_text_view, R.layout.recyc_one_item, new GridLayoutHelper(3)) {
            @Override
            public int getItemChildCount(int section) {
                return getItem(section).getVideos().size();
            }

            @Override
            public void convertChildItem(VBaseHolderHelper helper, int section, int relativePosition,int absoPsition) {
                Section2Model.Video video = getItem(section).getVideos().get(relativePosition);
              CustomImageLoader.loadImage(TaobaoActivity.this,video.getImg(),helper.getImageView(R.id.imgItem));
                helper.setText(R.id.tvItem, video.getName());
            }

            @Override
            protected void convert(VBaseHolderHelper helper, Section2Model section2Model, int position) {
                helper.setText(R.id.tv, section2Model.getHeader())
                        .setVisible(R.id.more, section2Model.isMroe());
            }
        };

        mDelegateAdapter.addAdapter(bannerAdapter);
        mDelegateAdapter.addAdapter(gridAdapter);
        mDelegateAdapter.addAdapter(newsAdapter);
        mDelegateAdapter.addAdapter(waterfallAdapter);
        mDelegateAdapter.addAdapter(mSectionedAdapter);
    }

    private LayoutHelper getWaterHelper() {
        StaggeredGridLayoutHelper staggerHelper = new StaggeredGridLayoutHelper(2, 8);
        staggerHelper.setMargin(0, 20, 0, 10);
        return staggerHelper;
    }

    @Override
    protected void bindListener() {
        super.bindListener();
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadmore(false);
        gridAdapter.addOnItemClickListener(new OnItemClickListener<GridBean>() {
            @Override
            public void onItemClick(View view, int position, GridBean mData) {
                showToast(mData.getFunction() + "position:" + position);
            }
        });
        waterfallAdapter.addOnItemClickListener(new OnItemClickListener<WaterCargo>() {
            @Override
            public void onItemClick(View view, int position, WaterCargo mData) {
                showToast(mData.getTitle() + "position:" + position);
            }
        });
        mSectionedAdapter.addOnItemClickListener(new OnItemClickListener<Section2Model>() {
            @Override
            public void onItemClick(View view, int position, Section2Model mData) {
               showToast( mData.getHeader() + "position:" + position);
            }
        });
        mSectionedAdapter.addOnChildItemClickListener(new OnChildItemClickListener() {
            @Override
            public void onChildItemClick(int section, int relativePosition, int absolutePosition) {
                showToast(mSectionedAdapter.getItem(section).getVideos().get(relativePosition).getName() + "position:" + relativePosition);
            }
        });
    }

    @Override
    protected void onProcessLogic() {
        Observable.timer(5, TimeUnit.SECONDS).compose(new Observable.Transformer<Long, HomeData>() {
            @Override
            public Observable<HomeData> call(Observable<Long> longObservable) {
                return Observable.create(new Observable.OnSubscribe<HomeData>() {
                    @Override
                    public void call(Subscriber<? super HomeData> subscriber) {
                        subscriber.onNext(getJson());
                        subscriber.onCompleted();
                    }
                });
            }
        }).compose(RxSchedulers.<HomeData>io2main())
                .subscribe(new Action1<HomeData>() {
                    @Override
                    public void call(HomeData homeData) {
                        bannerAdapter.addItem(homeData.getBanner());
                        gridAdapter.addItems(homeData.getGrid());
                        waterfallAdapter.addItems(homeData.getWatercargo());
                        mSectionedAdapter.addItems(homeData.getSection2Models());
                    }
                });
    }


    private LayoutHelper getGridLayoutHelp() {
        //设置Grid布局
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(5);
        //是否自动扩展
        gridLayoutHelper.setAutoExpand(false);
     /*   gridLayoutHelper.setPadding(0, 16, 0, 16);
        gridLayoutHelper.setVGap(16);// 控制子元素之间的垂直间距
        gridLayoutHelper.setHGap(0);// 控制子元素之间的水平间距
        gridLayoutHelper.setBgColor(Color.WHITE);*/
        return gridLayoutHelper;
    }

    private HomeData getJson() {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open("data.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        HomeData homeData = new Gson().fromJson(stringBuilder.toString(), HomeData.class);
        homeData.setSection2Models(AnalogData.analogSection2Model(1));
        return homeData;
    }
}
