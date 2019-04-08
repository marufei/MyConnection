package com.jingyi.myconnection.entity;

import java.util.List;

/**
 * Created by MaRufei
 * on 2019/1/9.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class BannerEntity extends BaseEntity {

    /**
     * code : 0
     * banners : [{"id":1,"name":"积木课程全新升级","img_url":"https://uploads.hlqqsm.cn/mbmp/20181130/ueJLrh8fjxusa1zY2GeikOqfROIr88Hp.jpg?imageView2/2/w/720/q/90","banner_type":1,"link":"","content_url":""},{"id":2,"name":"全脑开发家庭版隆重上线","img_url":"https://uploads.hlqqsm.cn/mbmp/20181130/rxePXO0nJGsNzo9aFvcjiLJFeGTLEFDE.jpg?imageView2/2/w/720/q/90","banner_type":2,"link":"","content_url":"http://127.0.0.1:8093/app/banners/2"},{"id":3,"name":"有声读物暖暖上新","img_url":"https://uploads.hlqqsm.cn/mbmp/20181130/2ric70VaEi1AV8tWkJN7Mjh36cP3CMQx.jpg?imageView2/2/w/720/q/90","banner_type":3,"link":"https://www.taobao.com","content_url":""}]
     */

    private List<BannersBean> banners;

    public List<BannersBean> getBanners() {
        return banners;
    }

    public void setBanners(List<BannersBean> banners) {
        this.banners = banners;
    }

    public static class BannersBean {
        /**
         * id : 1
         * name : 积木课程全新升级
         * img_url : https://uploads.hlqqsm.cn/mbmp/20181130/ueJLrh8fjxusa1zY2GeikOqfROIr88Hp.jpg?imageView2/2/w/720/q/90
         * banner_type : 1
         * link :
         * content_url :
         */

        private int id;
        private String name;
        private String img_url;
        private int banner_type;
        private String link;
        private String content_url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public int getBanner_type() {
            return banner_type;
        }

        public void setBanner_type(int banner_type) {
            this.banner_type = banner_type;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getContent_url() {
            return content_url;
        }

        public void setContent_url(String content_url) {
            this.content_url = content_url;
        }
    }
}
