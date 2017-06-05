package com.heu.cs.pojo;

/**
 * Created by memgq on 2017/6/5.
 */
public class OrderProgressPojo {
    private String putOrder;
    private String receiveOrder;
    private String completeOrder;

    public String getPutOrder() {
        return putOrder;
    }

    public void setPutOrder(String putOrder) {
        this.putOrder = putOrder;
    }

    public String getReceiveOrder() {
        return receiveOrder;
    }

    public void setReceiveOrder(String receiveOrder) {
        this.receiveOrder = receiveOrder;
    }

    public String getCompleteOrder() {
        return completeOrder;
    }

    public void setCompleteOrder(String completeOrder) {
        this.completeOrder = completeOrder;
    }
}
