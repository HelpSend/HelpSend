package com.heu.cs.dao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.OrderPojo;
import com.heu.cs.pojo.QueryOrderResponsePojo;
import com.heu.cs.pojo.ReturnInfoPojo;
import com.mongodb.MongoClientException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;

/**
 * Created by memgq on 2017/5/28.
 */
public class QuerySelfAllOrderDao {
    private static final String operateSuccess = "1";
    private static final  String operateFailure = "0";

    public String querySelfAllOrder(String orderOwnerId){
        Gson gson=new Gson();
        try{
            ArrayList<OrderPojo> res=new ArrayList<OrderPojo>();
            ConnMongoDB connMongoDB=new ConnMongoDB();
            MongoCollection collection = connMongoDB.getCollection("bbddb", "normalorder");
            Document document=new Document();
            document.append("orderOwnerId",orderOwnerId);
            Document sortDocument=new Document();
            sortDocument.append("orderStatus",-1);
            FindIterable<Document> findIterable=collection.find(document).sort(sortDocument);
            for(Document d:findIterable){
                OrderPojo order = gson.fromJson(d.toJson(), OrderPojo.class);
                res.add(order);
            }
            connMongoDB.getMongoClient().close();
            QueryOrderResponsePojo queryOrderResponse=new QueryOrderResponsePojo();
            queryOrderResponse.setStatus(operateSuccess);
            queryOrderResponse.setMessage(res);
            return  gson.toJson(queryOrderResponse,QueryOrderResponsePojo.class);
        }catch (MongoClientException e){
            e.printStackTrace();
            ReturnInfoPojo returnInfo=new ReturnInfoPojo();
            returnInfo.setStatus(operateFailure);
            returnInfo.setMessage("MongoClient Error");
            return gson.toJson(returnInfo,ReturnInfoPojo.class);
        }
    }
}
