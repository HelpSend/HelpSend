package com.heu.cs.pojo;

/**
 * Created by memgq on 2017/5/29.
 */
public class GrabOrderDetailsResponsePojo {
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GrapOrderDetailsPojo getMessage() {
        return message;
    }

    public void setMessage(GrapOrderDetailsPojo message) {
        this.message = message;
    }

    private GrapOrderDetailsPojo message;
}
