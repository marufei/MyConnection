package com.jingyi.myconnection.entity;

import java.util.List;

/**
 * Created by MaRufei
 * on 2018/8/21.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class RegionEntity extends BaseEntity {

    /**
     * code : 0
     * regions : [{"adcode":"110000","padcode":"100000","name":"北京市","level":"province"},{"adcode":"120000","padcode":"100000","name":"天津市","level":"province"},{"adcode":"130000","padcode":"100000","name":"河北省","level":"province"},{"adcode":"140000","padcode":"100000","name":"山西省","level":"province"},{"adcode":"150000","padcode":"100000","name":"内蒙古自治区","level":"province"},{"adcode":"210000","padcode":"100000","name":"辽宁省","level":"province"},{"adcode":"220000","padcode":"100000","name":"吉林省","level":"province"},{"adcode":"230000","padcode":"100000","name":"黑龙江省","level":"province"},{"adcode":"310000","padcode":"100000","name":"上海市","level":"province"},{"adcode":"320000","padcode":"100000","name":"江苏省","level":"province"},{"adcode":"330000","padcode":"100000","name":"浙江省","level":"province"},{"adcode":"340000","padcode":"100000","name":"安徽省","level":"province"},{"adcode":"350000","padcode":"100000","name":"福建省","level":"province"},{"adcode":"360000","padcode":"100000","name":"江西省","level":"province"},{"adcode":"370000","padcode":"100000","name":"山东省","level":"province"},{"adcode":"410000","padcode":"100000","name":"河南省","level":"province"},{"adcode":"420000","padcode":"100000","name":"湖北省","level":"province"},{"adcode":"430000","padcode":"100000","name":"湖南省","level":"province"},{"adcode":"440000","padcode":"100000","name":"广东省","level":"province"},{"adcode":"450000","padcode":"100000","name":"广西壮族自治区","level":"province"},{"adcode":"460000","padcode":"100000","name":"海南省","level":"province"},{"adcode":"500000","padcode":"100000","name":"重庆市","level":"province"},{"adcode":"510000","padcode":"100000","name":"四川省","level":"province"},{"adcode":"520000","padcode":"100000","name":"贵州省","level":"province"},{"adcode":"530000","padcode":"100000","name":"云南省","level":"province"},{"adcode":"540000","padcode":"100000","name":"西藏自治区","level":"province"},{"adcode":"610000","padcode":"100000","name":"陕西省","level":"province"},{"adcode":"620000","padcode":"100000","name":"甘肃省","level":"province"},{"adcode":"630000","padcode":"100000","name":"青海省","level":"province"},{"adcode":"640000","padcode":"100000","name":"宁夏回族自治区","level":"province"},{"adcode":"650000","padcode":"100000","name":"新疆维吾尔自治区","level":"province"}]
     */

    private List<RegionsBean> regions;

    public List<RegionsBean> getRegions() {
        return regions;
    }

    public void setRegions(List<RegionsBean> regions) {
        this.regions = regions;
    }

    public static class RegionsBean {
        /**
         * adcode : 110000
         * padcode : 100000
         * name : 北京市
         * level : province
         */

        private String adcode;
        private String padcode;
        private String name;
        private String level;
        private boolean select;

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public String getPadcode() {
            return padcode;
        }

        public void setPadcode(String padcode) {
            this.padcode = padcode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }
}
