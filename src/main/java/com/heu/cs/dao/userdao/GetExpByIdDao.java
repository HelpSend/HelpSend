package com.heu.cs.dao.userdao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.ReturnInfoPojo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

/**
 * Created by memgq on 2017/6/15.
 */
public class GetExpByIdDao {
    public String getExpById(String userId){
        ConnMongoDB connMongoDB=new ConnMongoDB();
        MongoCollection collection=connMongoDB.getCollection("bbddb","user");
        Document filter=new Document("userId",userId);
        MongoCursor<Document> cursor=collection.find(filter).iterator();
        Document d= cursor.next();
        ReturnInfoPojo returnInfoPojo=new ReturnInfoPojo();
        Gson gson=new Gson();
        returnInfoPojo.setExp(d.get("experience").toString());
        returnInfoPojo.setStatus("1");
        returnInfoPojo.setMessage("");
        cursor.close();
        connMongoDB.getMongoClient().close();
        return gson.toJson(returnInfoPojo,ReturnInfoPojo.class);
    }
}
