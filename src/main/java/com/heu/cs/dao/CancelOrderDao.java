package com.heu.cs.dao;

import com.heu.cs.conndb.ConnMongoDB;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * Created by memgq on 2017/5/28.
 */
public class CancelOrderDao {
    private static final String operateSuccess = "1";
    private static final  String operateFailure = "0";

    public String cancelOrder(String orderId){
        try{
            ConnMongoDB connMongoDB=new ConnMongoDB();
            MongoCollection collection= connMongoDB.getCollection("bbddb","normalorder");
            Document filter = new Document();
            filter.append("_id", orderId);
            Document update = new Document();
            update.append("$set", new Document("orderStatus", "-1"));
            collection.updateOne(filter, update);
            connMongoDB.getMongoClient().close();
            return operateSuccess;
        }catch (MongoException e){
            e.printStackTrace();
            return operateFailure;
        }

    }
}
