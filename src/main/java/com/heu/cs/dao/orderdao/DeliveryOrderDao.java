package com.heu.cs.dao.orderdao;

import com.heu.cs.conndb.ConnMongoDB;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.joda.time.DateTime;

import javax.print.Doc;

/**
 * Created by memgq on 2017/6/6.
 */
public class DeliveryOrderDao {
    public String deliveryOrder(String orderId) {
        ConnMongoDB connMongoDB = new ConnMongoDB();

        MongoCollection collection = connMongoDB.getCollection("bbddb", "normalorder");
        MongoCursor<Document> cursor = collection.find().iterator();
        Document document = cursor.next();
        Document update = new Document();
        Document newValue = new Document();
        DateTime dateTime = new DateTime();
        newValue.append("orderStatus", "2")
                .append("deliveryTime", dateTime.toString("yyyy-MM-dd HH:mm:ss"));
        update.append("$set", newValue);
        collection.updateOne(document, update);
        return "1";
    }


}
