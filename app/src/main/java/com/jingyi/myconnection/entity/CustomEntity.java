package com.jingyi.myconnection.entity;

/**
 * Created by MaRufei
 * on 2018/10/31.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class CustomEntity extends BaseEntity {

    /**
     * code : 0
     * service_phone : 123456
     * service_time : 工作日9:00-18:00
     */

    private String service_phone;
    private String service_time;

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone;
    }

    public String getService_time() {
        return service_time;
    }

    public void setService_time(String service_time) {
        this.service_time = service_time;
    }
}
