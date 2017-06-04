package com.heu.cs.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memgq on 2017/5/24.
 */
public class QueryOrderResponsePojo {
    private String status;
    private List<OrderPojo> message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderPojo> getMessage() {
        return message;
    }

    public void setMessage(List<OrderPojo> message) {
        this.message = message;
    }


}
