package com.heu.cs.dao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.ReturnInfoPojo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;


/**
 * Created by memgq on 2017/5/21.
 */
public class ReceiveOrderDao {
    private String operateSuccess="1";
    private String operateFailure="0";


    public String receiveOrder(String orderId,String orderReceiverId) {
        Gson gson = new Gson();
        ReturnInfoPojo returnInfo = new ReturnInfoPojo();
        String result = "";
        ConnMongoDB connMongoDB = new ConnMongoDB();
        try {
            MongoCollection collection = connMongoDB.getCollection("bbddb", "normalorder");
            Document filter = new Document();
            filter.append("orderId", orderId);
            Document update = new Document();
            update.append("$set", new Document("orderReceiverId", orderReceiverId));
            update.append("$set", new Document("orderStatus", "1"));
            collection.updateOne(filter, update);
            returnInfo.setStatus(operateSuccess);
            returnInfo.setMessage("接单成功");
        } catch (Exception e) {
            e.printStackTrace();
            returnInfo.setStatus(operateFailure);
            returnInfo.setMessage("接单失败");
        }finally {
            connMongoDB.getMongoClient().close();
            result=gson.toJson(returnInfo,ReturnInfoPojo.class);
            return result;
        }
    }
}
