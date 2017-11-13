package com.mario.baseadapter.demo.model;


import java.util.List;

/**
 * Date: 2016-12-15
 * Time: 19:42
 * Description:
 */
public class Section2Model {
    private boolean isMroe;
    private List<Video> mVideos;
    private String mHeader;

    public Section2Model(String header, boolean isMroe) {
        this.isMroe = isMroe;
        this.mHeader = header;
    }





    public boolean isMroe() {
        return isMroe;
    }

    public void setMroe(boolean mroe) {
        isMroe = mroe;
    }


    public String getHeader() {
        return mHeader;
    }

    public void setHeader(String header) {
        mHeader = header;
    }

    public List<Video> getVideos() {
        return mVideos;
    }

    public void setVideos(List<Video> videos) {
        mVideos = videos;
    }

    public static class Video {
        private String img;
        private String name;

        public Video(String img, String name) {
            this.img = img;
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
