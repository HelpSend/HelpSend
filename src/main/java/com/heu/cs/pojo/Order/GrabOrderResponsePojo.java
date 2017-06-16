package com.heu.cs.pojo.Order;

import java.util.ArrayList;

/**
 * Created by memgq on 2017/5/28.
 */
public class GrabOrderResponsePojo {

    private String status;
    private ArrayList<GrabOrderPojo> message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<GrabOrderPojo> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<GrabOrderPojo> message) {
        this.message = message;
    }

}
