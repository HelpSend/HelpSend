package com.heu.cs.dao.userdao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.ReturnInfoPojo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

/**
 * Created by memgq on 2017/6/17.
 */
public class GetRoleByUserIdDao {
    public String getRoleByUserId(String userId){
        String role="";
        ConnMongoDB connMongoDB=new ConnMongoDB();
        MongoCollection collection=connMongoDB.getCollection("bbddb","user");
        MongoCursor<Document> cursor=collection.find(new Document("userId",userId)).iterator();
        if(cursor.hasNext()){
            role=cursor.next().getString("role");
        }
        cursor.close();
        connMongoDB.getMongoClient().close();
        ReturnInfoPojo returnInfoPojo=new ReturnInfoPojo();
        Gson gson=new Gson();
        returnInfoPojo.setStatus(role);
        return gson.toJson(returnInfoPojo, ReturnInfoPojo.class);
    }
}
