package com.jingyi.myconnection.entity;

/**
 * Created by MaRufei
 * on 2019/1/23.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class MineEntity extends BaseEntity {

    /**
     * code : 0
     * user : {"id":1,"tel":"15251815510","invite_code":"4V2WG5","avatar_url":"http://127.0.0.1:8093/static/images/default.png","nickname":"用户355223","member_level":2,"member_level_display":"普通会员","expire_date":"2019-01-07"}
     */

    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * id : 1
         * tel : 15251815510
         * invite_code : 4V2WG5
         * avatar_url : http://127.0.0.1:8093/static/images/default.png
         * nickname : 用户355223
         * member_level : 2
         * member_level_display : 普通会员
         * expire_date : 2019-01-07
         */

        private int id;
        private String tel;
        private String invite_code;
        private String avatar_url;
        private String nickname;
        private int member_level;
        private String member_level_display;
        private String expire_date;

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

        public int getMember_level() {
            return member_level;
        }

        public void setMember_level(int member_level) {
            this.member_level = member_level;
        }

        public String getMember_level_display() {
            return member_level_display;
        }

        public void setMember_level_display(String member_level_display) {
            this.member_level_display = member_level_display;
        }

        public String getExpire_date() {
            return expire_date;
        }

        public void setExpire_date(String expire_date) {
            this.expire_date = expire_date;
        }
    }
}
