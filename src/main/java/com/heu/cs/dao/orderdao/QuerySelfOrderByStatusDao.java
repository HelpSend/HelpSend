package com.heu.cs.dao.orderdao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.Order.OrderPojo;
import com.heu.cs.pojo.Order.QueryOrderResponsePojo;
import com.heu.cs.pojo.ReturnInfoPojo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by memgq on 2017/5/17.
 */
public class QuerySelfOrderByStatusDao {
    private  final String operateSuccess = "1";
    private  final String operateFailure = "0";


    public String queryNewOrder(String orderOwnerId, String orderStatus) {
        Gson gson = new Gson();
        ReturnInfoPojo returnInfo = new ReturnInfoPojo();
        String result = "";
        List<OrderPojo> resList = new ArrayList<OrderPojo>();
        ConnMongoDB connMongoDB = new ConnMongoDB();
        try {
            MongoCollection collection = connMongoDB.getCollection("bbddb", "normalorder");
            Document document = new Document();
            document.append("orderOwnerId", orderOwnerId).append("orderStatus", orderStatus);
            FindIterable<Document> findIterable = collection.find(document);
            for (Document d : findIterable) {
                //  JsonObject obj = new JsonParser().parse(d.toString()).getAsJsonObject();
                OrderPojo order = gson.fromJson(d.toJson(), OrderPojo.class);
                resList.add(order);
            }
            QueryOrderResponsePojo queryOrderResponse = new QueryOrderResponsePojo();
            queryOrderResponse.setStatus(operateSuccess);
            queryOrderResponse.setMessage(resList);
            result = gson.toJson(queryOrderResponse, QueryOrderResponsePojo.class);
        } catch (Exception e) {
            e.printStackTrace();
            returnInfo.setStatus(operateFailure);
            returnInfo.setMessage("操作失败");
            result = gson.toJson(returnInfo, ReturnInfoPojo.class);
        } finally {
            connMongoDB.getMongoClient().close();
            return result;
        }
    }
}
