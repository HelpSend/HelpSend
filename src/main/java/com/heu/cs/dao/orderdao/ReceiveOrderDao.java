package com.heu.cs.dao.orderdao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.ReturnInfoPojo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;


/**
 * Created by memgq on 2017/5/21.
 */
public class ReceiveOrderDao {
    private final String operateSuccess="1";
    private final String operateFailure="0";
    private final String FAILUREMSG="接单失败";
    public String receiveOrder(String orderId,String orderReceiverId) {
        ReturnInfoPojo returnInfo = new ReturnInfoPojo();
        String result = "";
        Gson gson = new Gson();
        if(isHelper(orderReceiverId)){
            ConnMongoDB connMongoDB = new ConnMongoDB();
            try {
                MongoCollection collection = connMongoDB.getCollection("bbddb", "normalorder");
                Document filter = new Document();
                filter.append("_id", new ObjectId(orderId));
                FindIterable<Document> findIterable=collection.find(filter);
                MongoCursor<Document> mongoCursor=findIterable.iterator();
                Document d=mongoCursor.next();
                if(d.get("orderStatus").equals("0")){

                    MongoCollection userCollection=connMongoDB.getCollection("bbddb","user");
                    Document userFilter=new Document();
                    userFilter.append("userId",orderReceiverId);
                    MongoCursor<Document> userCursor=userCollection.find(userFilter).iterator();
                    int temp=(int)(Double.parseDouble(d.getString("orderPrice"))*6);
                    returnInfo.setExp(String.valueOf(userCursor.next().getInteger("experience")+temp));
                    userCursor.close();

                    Document userUpdate=new Document();
                    userUpdate.append("experience",temp);
                    userCollection.findOneAndUpdate(userFilter,new Document("$inc",userUpdate));



                    DateTime dateTime=new DateTime();
                    Document update = new Document();
                    Document newValue=new Document();
                    newValue.append("orderReceiverId", orderReceiverId)
                            .append("orderStatus", "1")
                            .append("receiveOrderTime", dateTime.toString("yyyy-MM-dd HH:mm:ss"));
                    update.append("$set", newValue);
                    collection.updateOne(d, update);


                    returnInfo.setStatus(operateSuccess);
                    returnInfo.setMessage("成功接单");


                }else {
                    returnInfo.setStatus(operateFailure);
                    returnInfo.setMessage(FAILUREMSG);
                }

                mongoCursor.close();
                connMongoDB.getMongoClient().close();
                result=gson.toJson(returnInfo,ReturnInfoPojo.class);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                returnInfo.setStatus(operateFailure);
                returnInfo.setMessage(FAILUREMSG);
                result=gson.toJson(returnInfo,ReturnInfoPojo.class);
                return result;
            }
        }else {
            returnInfo.setStatus(operateFailure);
            returnInfo.setMessage("请先成为帮带员");
            result=gson.toJson(returnInfo,ReturnInfoPojo.class);
            return result;
        }

    }


    private boolean isHelper(String orderReceiverId){
        ConnMongoDB connMongoDB = new ConnMongoDB();
        MongoCollection collection = connMongoDB.getCollection("bbddb", "user");
        Document userDocument = new Document();
        userDocument.append("_id", new ObjectId(orderReceiverId));
        FindIterable<Document> findIterable=collection.find(userDocument);
        MongoCursor<Document> userCursor=findIterable.iterator();
        if(userCursor.hasNext()) {
            Document d = userCursor.next();
            if(d.get("role").equals("1")){
                userCursor.close();
                connMongoDB.getMongoClient().close();
                return true;
            }else {
                userCursor.close();
                connMongoDB.getMongoClient().close();
                return false;
            }
        }else {
            userCursor.close();
            connMongoDB.getMongoClient().close();
            return false;
        }
    }
}
