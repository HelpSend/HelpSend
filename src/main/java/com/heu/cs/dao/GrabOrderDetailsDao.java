package com.heu.cs.dao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Created by memgq on 2017/5/29.
 */
public class GrabOrderDetailsDao {
    private static final String operateSuccess = "1";
    private static final  String operateFailure = "0";

    public String grabOrderDetails(String orderId){
        Gson gson=new Gson();
        ReturnInfoPojo returnInfoPojo=new ReturnInfoPojo();
        String returnResult="";
        try{
            ConnMongoDB connMongoDB=new ConnMongoDB();
            MongoCollection orderCollection = connMongoDB.getCollection("bbddb", "normalorder");
            MongoCollection userCollection = connMongoDB.getCollection("bbddb", "user");
            Document userDocument = new Document();
            Document document = new Document();
            document.append("_id", new ObjectId(orderId));
            FindIterable<Document> findIterable= orderCollection.find(document);
            GrapOrderDetailsPojo grapOrderDetailsPojo=new GrapOrderDetailsPojo();
            if(findIterable!=null){
                for(Document d:findIterable){
                    OrderPojo order = gson.fromJson(d.toJson(), OrderPojo.class);
                    grapOrderDetailsPojo.setImagePath(order.getImagePath());
                    grapOrderDetailsPojo.setOrderId(d.get("_id").toString());
                    grapOrderDetailsPojo.setOrderOwnerId(order.getOrderOwnerId());
                    grapOrderDetailsPojo.setRemark(order.getRemark());
                    String[] goodsProp= order.getGoodsName().split(" - ");
                    grapOrderDetailsPojo.setGoodsCategory(goodsProp[0]);
                    grapOrderDetailsPojo.setGoodsWeight(goodsProp[1]);
                    grapOrderDetailsPojo.setEndLocationPojo(order.getEndLocation());
                    grapOrderDetailsPojo.setStartLocationPojo(order.getStartLocation());
                    grapOrderDetailsPojo.setSendTime(order.getSendTime());
                    grapOrderDetailsPojo.setReceiveTime(order.getReceiveTime());
                    userDocument.append("_id", new ObjectId(order.getOrderOwnerId()));
                    FindIterable<Document> userFindIterable= userCollection.find(userDocument);
                    for(Document usrdoc:userFindIterable){
                        grapOrderDetailsPojo.setOrderOwnerGender(usrdoc.getString("gender"));
                        grapOrderDetailsPojo.setOrderOwnerAvatarPath(usrdoc.getString("avatarPath"));
                        grapOrderDetailsPojo.setOrderOwnerNickName(usrdoc.getString("nickName"));
                    }
                }
                GrabOrderDetailsResponsePojo grabOrderDetailsResponsePojo=new GrabOrderDetailsResponsePojo();
                grabOrderDetailsResponsePojo.setStatus(operateSuccess);
                grabOrderDetailsResponsePojo.setMessage(grapOrderDetailsPojo);
                returnResult=gson.toJson(grabOrderDetailsResponsePojo,GrabOrderDetailsResponsePojo.class);
            }else {
                returnInfoPojo.setStatus(operateFailure);
                returnInfoPojo.setMessage("未找到此订单");
                returnResult=gson.toJson(returnInfoPojo,ReturnInfoPojo.class);
            }
            connMongoDB.getMongoClient().close();
            return returnResult;
        }catch (Exception e){
            e.printStackTrace();
            returnInfoPojo.setStatus(operateFailure);
            returnInfoPojo.setMessage("网络连接失败");
            return gson.toJson(returnInfoPojo,ReturnInfoPojo.class);
        }

    }
}
