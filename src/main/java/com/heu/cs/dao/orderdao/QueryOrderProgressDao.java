package com.heu.cs.dao.orderdao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.Order.OrderPojo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

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
            if (orderStatus.equals("0")) {
                String s0=orderPojo.getPutOrderTime() + "\n已下单,订单编号:" + orderPojo.getOrderId();
                progressList.add(s0);
            } else if(orderStatus.equals("-1")){
                String s0=orderPojo.getPutOrderTime() + "\n已下单,订单编号:" + orderPojo.getOrderId();
                String s1=orderPojo.getPutOrderTime() + "\n您已成功取消订单。";
                progressList.add(s1);
                progressList.add(s0);
            } else {
                Document usr = new Document();
                usr.append("userId", orderPojo.getOrderReceiverId());
                MongoCollection usrCollection = connMongoDB.getCollection("bbddb", "user");
                MongoCursor<Document> usrCursor = usrCollection.find(usr).iterator();
                Document u = usrCursor.next();
                if (orderStatus.equals("1")) {
                    String  s0=orderPojo.getPutOrderTime() + "\n已下单,订单编号:" + orderPojo.getOrderId();
                    String s1=orderPojo.getReceiveOrderTime() + "\n帮带员【" + u.get("nickName") + "】已经接单";
                    progressList.add(s1);
                    progressList.add(s0);
                } else if (orderStatus.equals("2")) {
                    String s0=orderPojo.getPutOrderTime() + "\n已下单,订单编号:" + orderPojo.getOrderId();
                    String s1=orderPojo.getReceiveOrderTime() + "\n帮带员【" + u.get("nickName") + "】已经接单";
                    String s2=orderPojo.getDeliveryTime() + "\n帮带员【" + u.get("nickName") + "】已完成订单";
                    progressList.add(s2);
                    progressList.add(s1);
                    progressList.add(s0);
                }else {

                }
                usrCursor.close();
            }
            returnResult=gson.toJson(progressList,ArrayList.class);
            mongoCursor.close();
        }
        connMongoDB.getMongoClient().close();
        return returnResult;
    }
}
