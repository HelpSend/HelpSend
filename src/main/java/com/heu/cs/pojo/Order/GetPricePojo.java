package com.heu.cs.pojo.Order;

/**
 * Created by memgq on 2017/6/7.
 */
public class GetPricePojo {
    private LocationPojo start;
    private LocationPojo end;

    public void setStart(LocationPojo start) {
        this.start = start;
    }

    public LocationPojo getStart() {
        return start;
    }

    public LocationPojo getEnd() {

        return end;
    }

    public void setEnd(LocationPojo end) {
        this.end = end;
    }
}
