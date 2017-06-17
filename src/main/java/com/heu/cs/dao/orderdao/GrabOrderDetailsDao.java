package com.heu.cs.dao.orderdao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.*;
import com.heu.cs.pojo.Order.GrabOrderDetailsResponsePojo;
import com.heu.cs.pojo.Order.GrapOrderDetailsPojo;
import com.heu.cs.pojo.Order.OrderPojo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;


/**
 * Created by memgq on 2017/5/29.
 */
public class GrabOrderDetailsDao {
    private  final String operateSuccess = "1";
    private  final  String operateFailure = "0";

    public String grabOrderDetails(String orderId){
        Gson gson=new Gson();
        ReturnInfoPojo returnInfoPojo=new ReturnInfoPojo();
        String returnResult="";
        ConnMongoDB connMongoDB=new ConnMongoDB();
        try{
            MongoCollection orderCollection = connMongoDB.getCollection("bbddb", "normalorder");

            Document document = new Document();
            document.append("_id", new ObjectId(orderId));
            System.out.println(document.toJson());
            FindIterable<Document> findIterable= orderCollection.find(document);
            GrapOrderDetailsPojo grapOrderDetailsPojo=new GrapOrderDetailsPojo();
            MongoCursor<Document> orderCursor=findIterable.iterator();
            if(orderCursor.hasNext()) {
                Document d = orderCursor.next();
                OrderPojo order = gson.fromJson(d.toJson(), OrderPojo.class);
                grapOrderDetailsPojo.setImagePath(order.getImagePath());
                grapOrderDetailsPojo.setOrderId(d.get("_id").toString());
                grapOrderDetailsPojo.setOrderOwnerId(order.getOrderOwnerId());
                grapOrderDetailsPojo.setRemark(order.getRemark());
                String[] goodsProp = order.getGoodsName().split(" - ");
                grapOrderDetailsPojo.setGoodsCategory(goodsProp[0]);
                grapOrderDetailsPojo.setGoodsWeight(goodsProp[1]);
                grapOrderDetailsPojo.setEndLocationPojo(order.getEndLocation());
                grapOrderDetailsPojo.setStartLocationPojo(order.getStartLocation());
                grapOrderDetailsPojo.setSendTime(order.getSendTime());
                grapOrderDetailsPojo.setReceiveTime(order.getReceiveTime());
                grapOrderDetailsPojo.setSenderTel(order.getSenderTel());
                grapOrderDetailsPojo.setReceiverTel(order.getReceiverTel());
                grapOrderDetailsPojo.setCommit(order.getCommit());

                MongoCollection userCollection = connMongoDB.getCollection("bbddb", "user");
                Document orderOwnerDocument = new Document("userId",order.getOrderOwnerId());
                FindIterable<Document> orderOwnerFindIterable = userCollection.find(orderOwnerDocument);
                for (Document usrdoc : orderOwnerFindIterable) {
                    grapOrderDetailsPojo.setOrderOwnerGender(usrdoc.getString("gender"));
                    grapOrderDetailsPojo.setOrderOwnerAvatarPath(usrdoc.getString("avatarPath"));
                    grapOrderDetailsPojo.setOrderOwnerNickName(usrdoc.getString("nickName"));
                }

                Document orderReceiverDocument = new Document("userId",order.getOrderReceiverId());
                FindIterable<Document> orderReceiverFindIterable = userCollection.find(orderReceiverDocument);
                for (Document usrdoc : orderReceiverFindIterable) {

                    grapOrderDetailsPojo.setOrderReceiverAvatarPath(usrdoc.getString("avatarPath"));
                }

                GrabOrderDetailsResponsePojo grabOrderDetailsResponsePojo = new GrabOrderDetailsResponsePojo();
                grabOrderDetailsResponsePojo.setStatus(operateSuccess);
                grabOrderDetailsResponsePojo.setMessage(grapOrderDetailsPojo);
                returnResult = gson.toJson(grabOrderDetailsResponsePojo, GrabOrderDetailsResponsePojo.class);

            }else {
                returnInfoPojo.setStatus(operateFailure);
                returnInfoPojo.setMessage("查找失败");
            }
            orderCursor.close();
        }catch (Exception e){
            e.printStackTrace();
            returnInfoPojo.setStatus(operateFailure);
            returnInfoPojo.setMessage("网络连接失败");
            returnResult= gson.toJson(returnInfoPojo,ReturnInfoPojo.class);
        }finally {
            connMongoDB.getMongoClient().close();

            return returnResult;
        }
    }
}
