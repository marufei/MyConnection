package com.jingyi.myconnection.entity;

import java.util.List;

/**
 * Created by MaRufei
 * on 2019/1/22.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class HangEntity extends BaseEntity {

    /**
     * code : 0
     * professions : [{"id":1,"name":"互联网/电子商务"}]
     */

    private List<ProfessionsBean> professions;

    public List<ProfessionsBean> getProfessions() {
        return professions;
    }

    public void setProfessions(List<ProfessionsBean> professions) {
        this.professions = professions;
    }

    public static class ProfessionsBean {
        /**
         * id : 1
         * name : 互联网/电子商务
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
