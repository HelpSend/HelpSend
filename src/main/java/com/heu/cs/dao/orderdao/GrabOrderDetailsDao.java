package com.heu.cs.dao.orderdao;

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
                MongoCollection userCollection = connMongoDB.getCollection("bbddb", "user");
                Document userDocument = new Document();
                userDocument.append("_id", new ObjectId(order.getOrderOwnerId()));
                FindIterable<Document> userFindIterable = userCollection.find(userDocument);
                for (Document usrdoc : userFindIterable) {
                    UserPojo userPojo=gson.fromJson(usrdoc.toJson(),UserPojo.class);
                    grapOrderDetailsPojo.setOrderOwnerGender(userPojo.getGender());
                    grapOrderDetailsPojo.setOrderOwnerAvatarPath(userPojo.getAvatarPath());
                    grapOrderDetailsPojo.setOrderOwnerNickName(userPojo.getNickName());

                }
                GrabOrderDetailsResponsePojo grabOrderDetailsResponsePojo = new GrabOrderDetailsResponsePojo();
                grabOrderDetailsResponsePojo.setStatus(operateSuccess);
                grabOrderDetailsResponsePojo.setMessage(grapOrderDetailsPojo);
                returnResult = gson.toJson(grabOrderDetailsResponsePojo, GrabOrderDetailsResponsePojo.class);
                orderCursor.close();
            }else {
                returnInfoPojo.setStatus(operateFailure);
                returnInfoPojo.setMessage("查找失败");
            }
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
