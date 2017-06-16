package com.heu.cs.pojo.User;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by memgq on 2017/6/16.
 */
public class TextInfoPojo {
    private String name;
    private String IdCardNumber;
    private String UserId;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCardNumber() {
        return IdCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        IdCardNumber = idCardNumber;
    }
}
