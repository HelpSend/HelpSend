package com.heu.cs.dao.orderdao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.SMSReplyPojo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;


/**
 * Created by memgq on 2017/6/6.
 */
public class GetUserReplySMSDao {
    public void getUserReplySMS(String sms_reply){
        Gson gson=new Gson();
        SMSReplyPojo smsReplyPojo=gson.fromJson(sms_reply,SMSReplyPojo.class);
        ConnMongoDB connMongoDB=new ConnMongoDB();
        MongoCollection collection=connMongoDB.getCollection("bbddb","normalorder");
        Document filter=new Document();
        filter.append("receiverTel",smsReplyPojo.getMobile())
                .append("orderReplyCode",smsReplyPojo.getText())
                .append("orderStatus","1");
        MongoCursor<Document> cursor=collection.find(filter).iterator();
        Document d=cursor.next();
        Document update = new Document();
        Document newValue=new Document();
        newValue.append("orderStatus", "2")
                .append("orderReplyCode","");
        update.append("$set", newValue);
        collection.updateOne(d, update);
        cursor.close();
        connMongoDB.getMongoClient().close();
    }
}
