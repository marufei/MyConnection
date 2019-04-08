package com.jingyi.myconnection.entity;

/**
 * Created by MaRufei
 * on 2019/1/21.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class LoginEntity extends BaseEntity {


    /**
     * code : 0
     * data : {"member_level":2,"access_token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xMjcuMC4wLjE6ODA5M1wvYXBpXC9sb2dpbiIsImlhdCI6MTU0ODIzMDc5MywiZXhwIjoxNTQ4NjYyNzkzLCJuYmYiOjE1NDgyMzA3OTMsImp0aSI6IjRLV0FGOU5hd0RmSlBod2giLCJzdWIiOjEsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjciLCJwYXNzd29yZF9oYXNoIjoiJDJ5JDEwJHdtdEJRNFZhZTl1NjJLZy41dFpiTU9ESUpEZXdMbUpqZ3c4QlZqUllRWWtnQ1JscEJ6YkltIn0.G9XynomJSUVy-MJ-W-0Dq9PK9uopRE24Qoely1fadbc","token_type":"bearer","expires_in":432000}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * member_level : 2
         * access_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xMjcuMC4wLjE6ODA5M1wvYXBpXC9sb2dpbiIsImlhdCI6MTU0ODIzMDc5MywiZXhwIjoxNTQ4NjYyNzkzLCJuYmYiOjE1NDgyMzA3OTMsImp0aSI6IjRLV0FGOU5hd0RmSlBod2giLCJzdWIiOjEsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjciLCJwYXNzd29yZF9oYXNoIjoiJDJ5JDEwJHdtdEJRNFZhZTl1NjJLZy41dFpiTU9ESUpEZXdMbUpqZ3c4QlZqUllRWWtnQ1JscEJ6YkltIn0.G9XynomJSUVy-MJ-W-0Dq9PK9uopRE24Qoely1fadbc
         * token_type : bearer
         * expires_in : 432000
         */

        private int member_level;
        private String access_token;
        private String token_type;
        private int expires_in;

        public int getMember_level() {
            return member_level;
        }

        public void setMember_level(int member_level) {
            this.member_level = member_level;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }
    }
}
