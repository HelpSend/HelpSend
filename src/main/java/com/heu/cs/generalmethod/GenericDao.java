package com.heu.cs.generalmethod;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * Created by memgq on 2017/6/5.
 */
public interface GenericDao {

    String getTimeDif(String targetTime);

    long getTimestamp(String t);

    void updateOrderId(Document document, MongoCollection collection);


    double rad(double d);

    String  getDistance(String lat1Str, String lng1Str, String lat2Str, String lng2Str);

    String setPrice(String diatance);
}
