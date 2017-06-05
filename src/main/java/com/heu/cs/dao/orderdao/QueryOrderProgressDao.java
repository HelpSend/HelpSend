package com.heu.cs.dao.orderdao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.OrderPojo;
import com.heu.cs.pojo.OrderProgressPojo;
import com.heu.cs.pojo.ReturnInfoPojo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

/**
 * Created by memgq on 2017/6/5.
 */
public class QueryOrderProgressDao {
    public String queryOrderProgress(String orderId) {
        Gson gson = new Gson();
        String returnResult = "";
        ArrayList<String> progressList = new ArrayList<String>();
        ConnMongoDB connMongoDB = new ConnMongoDB();
        MongoCollection collection = connMongoDB.getCollection("bbddb", "normalorder");
        Document document = new Document();
        document.append("orderId", orderId);
        FindIterable<Document> findIterable = collection.find(document);
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        if (mongoCursor.hasNext()) {
            Document d = mongoCursor.next();
            OrderPojo orderPojo = gson.fromJson(d.toJson(), OrderPojo.class);
            String orderStatus = orderPojo.getOrderStatus();
            OrderProgressPojo orderProgressPojo = new OrderProgressPojo();
            if (orderStatus.equals("0")) {
                orderProgressPojo.setPutOrder(orderPojo.getPutOrderTime() + " 已下单,订单编号:" + orderPojo.getOrderId());
            } else {
                Document usr = new Document();
                usr.append("userId", orderPojo.getOrderReceiverId());
                MongoCollection usrCollection = connMongoDB.getCollection("bbddb", "user");
                MongoCursor<Document> usrCursor = usrCollection.find(usr).iterator();
                Document u = usrCursor.next();
                if (orderStatus.equals("1")) {
                    orderProgressPojo.setPutOrder(orderPojo.getPutOrderTime() + " 已下单,订单编号:" + orderPojo.getOrderId());
                    orderProgressPojo.setReceiveOrder(orderPojo.getReceiveOrderTime() + " 帮带员:" + u.get("nickName") + " 已经接单");
                } else if (orderStatus.equals("2")) {
                    orderProgressPojo.setPutOrder(orderPojo.getPutOrderTime() + " 已下单,订单编号:" + orderPojo.getOrderId());
                    orderProgressPojo.setReceiveOrder(orderPojo.getReceiveOrderTime() + " 帮带员【" + u.get("nickName") + "】已经接单");
                    orderProgressPojo.setCompleteOrder(orderPojo.getDeliveryTime() + " 帮带员【" + u.get("nickName") + "】已完成订单");
                }else {

                }
                usrCursor.close();
            }
            returnResult=gson.toJson(orderProgressPojo,OrderProgressPojo.class);
            mongoCursor.close();
        }
        connMongoDB.getMongoClient().close();
        return returnResult;
    }
}
