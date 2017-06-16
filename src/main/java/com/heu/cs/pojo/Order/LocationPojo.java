package com.heu.cs.pojo.Order;

/**
 * Created by memgq on 2017/5/22.
 */
public class LocationPojo {
    private String latitude="";
    private String longitude="";

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description="";


}
