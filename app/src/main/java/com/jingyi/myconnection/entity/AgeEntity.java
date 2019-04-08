package com.jingyi.myconnection.entity;

import java.util.List;

/**
 * Created by MaRufei
 * on 2019/1/23.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class AgeEntity extends BaseEntity {

    /**
     * code : 0
     * age_ranges : [{"id":1,"name":"18岁以下"}]
     */

    private List<AgeRangesBean> age_ranges;

    public List<AgeRangesBean> getAge_ranges() {
        return age_ranges;
    }

    public void setAge_ranges(List<AgeRangesBean> age_ranges) {
        this.age_ranges = age_ranges;
    }

    public static class AgeRangesBean {
        /**
         * id : 1
         * name : 18岁以下
         */

        private int id;
        private String name;

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
    }
}
