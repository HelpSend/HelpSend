package com.heu.cs.dao;

import com.heu.cs.conndb.ConnMongoDB;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;


/**
 * Created by memgq on 2017/5/21.
 */
public class ReceiveOrderDao {
    private String operateSuccess="1";
    private String operateFailure="0";
    public ReceiveOrderDao() {
    }


    public String receiveOrder(String orderId,String orderReceiverId) {
        try {
            ConnMongoDB connMongoDB = new ConnMongoDB();
            MongoCollection collection = connMongoDB.getCollection("bbddb", "normalorder");
            Document filter = new Document();
            filter.append("orderId", orderId);
            Document update = new Document();
            update.append("$set", new Document("orderReceiverId", orderReceiverId));
            update.append("$set", new Document("orderStatus", "1"));
            collection.updateOne(filter, update);
            connMongoDB.getMongoClient().close();
            return operateSuccess;
        } catch (Exception e) {
            e.printStackTrace();
            return operateFailure;
        }
    }
}
