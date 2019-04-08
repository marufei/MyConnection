package com.jingyi.myconnection.entity;

import java.util.List;

/**
 * Created by MaRufei
 * on 2019/1/21.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class AddFriendEntity extends BaseEntity {

    /**
     * code : 0
     * users : [{"id":227,"tel":"122****9484","province_name":"江苏","city_name":"南京"}]
     */

    private List<UsersBean> users;

    public List<UsersBean> getUsers() {
        return users;
    }

    public void setUsers(List<UsersBean> users) {
        this.users = users;
    }

    public static class UsersBean {
        /**
         * id : 227
         * tel : 122****9484
         * province_name : 江苏
         * city_name : 南京
         */

        private int id;
        private String tel;
        private String province_name;
        private String city_name;
        private boolean select;
        private boolean add;

        public boolean isAdd() {
            return add;
        }

        public void setAdd(boolean add) {
            this.add = add;
        }

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getProvince_name() {
            return province_name;
        }

        public void setProvince_name(String province_name) {
            this.province_name = province_name;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }
    }
}
