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
 * Created by memgq on 2017/5/17.
 */
public class QueryNewOrderDao {
    private static final String operateSuccess = "1";
    private static final  String operateFailure = "0";

    public QueryNewOrderDao() { }

    public String queryNewOrder(String orderOwnerId){

        Gson gson=new Gson();
        try{
            ArrayList<OrderPojo> res=new ArrayList<OrderPojo>();
            ConnMongoDB connMongoDB=new ConnMongoDB();
            MongoCollection collection = connMongoDB.getCollection("bbddb", "normalorder");
            Document document=new Document();
            document.append("orderOwnerId",orderOwnerId).append("orderStatus","0");
            System.out.println(document.toString());
            FindIterable<Document> findIterable=collection.find(document);
            for(Document d:findIterable){

              //  JsonObject obj = new JsonParser().parse(d.toString()).getAsJsonObject();
                OrderPojo order = gson.fromJson(d.toJson(), OrderPojo.class);
                res.add(order);
            }
            connMongoDB.getMongoClient().close();
           // System.out.println(gson.toJson(res,new TypeToken<List<OrderPojo>>(){}.getType()));

            QueryOrderResponsePojo queryOrderResponse=new QueryOrderResponsePojo();
            queryOrderResponse.setStatus("1");
            queryOrderResponse.setMessage(res);
            return  gson.toJson(queryOrderResponse,QueryOrderResponsePojo.class);
            }catch (MongoClientException e){
            e.printStackTrace();
            ReturnInfoPojo returnInfo=new ReturnInfoPojo();
            returnInfo.setStatus("0");
            returnInfo.setMessage("MongoClient Error");
            return gson.toJson(returnInfo,ReturnInfoPojo.class);
        }
    }
}
