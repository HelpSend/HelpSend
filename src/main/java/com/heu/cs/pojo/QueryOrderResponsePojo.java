package com.heu.cs.pojo;

import java.util.ArrayList;

/**
 * Created by memgq on 2017/5/24.
 */
public class QueryOrderResponsePojo {
    private String status;
    private ArrayList<OrderPojo> message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<OrderPojo> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<OrderPojo> message) {
        this.message = message;
    }


}
