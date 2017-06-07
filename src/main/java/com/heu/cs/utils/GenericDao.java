package com.heu.cs.utils;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.joda.time.DateTime;

/**
 * Created by memgq on 2017/6/5.
 */
public interface GenericDao {

    String getTimeDif(String targetTime);

    long getTimestamp(String t);

    void updateOrderId(Document document, MongoCollection collection);

    double rad(double d);

    String  getDistance(String lat1Str, String lng1Str, String lat2Str, String lng2Str);


    String getPrice(String lat1Str, String lng1Str, String lat2Str, String lng2Str);

    String getDayOrNight(DateTime time);

    String getUrgentPrice(String lat1Str, String lng1Str, String lat2Str, String lng2Str);
}
