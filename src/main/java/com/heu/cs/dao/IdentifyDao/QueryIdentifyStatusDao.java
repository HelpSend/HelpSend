package com.heu.cs.dao.IdentifyDao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.ReturnInfoPojo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

/**
 * Created by memgq on 2017/6/16.
 */
public class QueryIdentifyStatusDao {
    public String queryIdentifyStatu(String userId){
        String status="";
        ReturnInfoPojo returnInfoPojo=new ReturnInfoPojo();
        Gson gson=new Gson();
        ConnMongoDB connMongoDB=new ConnMongoDB();
        MongoCollection collection=connMongoDB.getCollection("bbddb","identify");
        Document filter=new Document("userId",userId);
        MongoCursor<Document> cursor=collection.find(filter).iterator();
        if(cursor.hasNext()){
            Document d=cursor.next();
            status=d.getString("identifyStatus");
        }else {
            status="-1";
            collection.insertOne(filter.append("identifyStatus","-1"));
        }
        returnInfoPojo.setStatus(status);
        cursor.close();
        connMongoDB.getMongoClient().close();
        return gson.toJson(returnInfoPojo,ReturnInfoPojo.class);
    }
}
