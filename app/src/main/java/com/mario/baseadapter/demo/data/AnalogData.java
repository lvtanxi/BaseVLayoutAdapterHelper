package com.mario.baseadapter.demo.data;


import com.mario.baseadapter.demo.model.MainModel;
import com.mario.baseadapter.demo.model.NormalModel;
import com.mario.baseadapter.demo.model.Section2Model;
import com.mario.baseadapter.demo.ui.EmptyActivity;
import com.mario.baseadapter.demo.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Date: 2016-12-15
 * Time: 11:18
 * Description:模拟数据
 */
public class AnalogData {

    public static List<MainModel> analogMainModel() {
        List<MainModel> array = new ArrayList<>();
        array.add(new MainModel(MainActivity.class, "添加头部和脚"));
        array.add(new MainModel(EmptyActivity.class, "空布局"));
        array.add(new MainModel(MainActivity.class, "分组+空布局"));
        array.add(new MainModel(MainActivity.class, "1拖N布局"));
        array.add(new MainModel(MainActivity.class, "淘宝"));
        return array;
    }

    public static List<NormalModel> analogNormalModel() {
        List<NormalModel> array = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            array.add(new NormalModel("this is title" + i, "this is detail" + i, ""));
        }
        return array;
    }


    private static final String HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK = "http://cdn.duitang.com/uploads/item/201508/21/20150821201845_T5xBs.png";
    private static final String CYM_CHAD = "吕檀溪";


    public static List<Section2Model> analogSection2Model(int pageNo) {
        List<Section2Model> list = new ArrayList<>();
        Section2Model model;
        for (int i = (pageNo - 1) * 10; i < pageNo * 10; i++) {
            model = new Section2Model("this is section" + i, i % 2 == 0);
            List<Section2Model.Video> videos = new ArrayList<>();
            int count = i > 7 ? 6 : i;
            for (int j = 0; j < count; j++) {
                videos.add(new Section2Model.Video(HTTPS_AVATARS1_GITHUBUSERCONTENT_COM_LINK, CYM_CHAD));
            }
            model.setVideos(videos);
            list.add(model);
        }
        return list;
    }

    public static List<String> analogString(int pageNo) {
        List<String> list = new ArrayList<>();
        for (int i = (pageNo - 1) * 50; i < pageNo * 50; i++) {
            list.add("this is item : " + i);
        }
        return list;
    }

}
