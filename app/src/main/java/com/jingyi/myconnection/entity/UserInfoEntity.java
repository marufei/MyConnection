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
public class UserInfoEntity extends BaseEntity {

    /**
     * code : 0
     * users : [{"id":3,"tel":"12224592446","invite_code":"123456","avatar_url":"http://127.0.0.1:8093/static/images/default.png","nickname":"用户382377"}]
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
         * id : 3
         * tel : 12224592446
         * invite_code : 123456
         * avatar_url : http://127.0.0.1:8093/static/images/default.png
         * nickname : 用户382377
         */

        private int id;
        private String tel;
        private String invite_code;
        private String avatar_url;
        private String nickname;

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

        public String getInvite_code() {
            return invite_code;
        }

        public void setInvite_code(String invite_code) {
            this.invite_code = invite_code;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
