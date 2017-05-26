package com.heu.cs.pojo;

import java.util.List;

/**
 * Created by memgq on 2017/5/23.
 */
public class OrderListPojo {
    public List<OrderPojo> getOrderPojoList() {
        return orderPojoList;
    }

    public void setOrderPojoList(List<OrderPojo> orderPojoList) {
        this.orderPojoList = orderPojoList;
    }

    private List<OrderPojo> orderPojoList = null;
}
