package com.heu.cs.dao.userdao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.UserPojo;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * Created by memgq on 2017/6/4.
 */
public class CreateUserDao {
    private final String operateSuccess="1";
    private final String operateFailure="0";

    public String createUser(String telNumber){
        String resultStr="";
        ConnMongoDB connMongoDB=new ConnMongoDB();
        FormatUserData formatUserData=new FormatUserData();
        UserPojo userPojo=formatUserData.format(telNumber);
        Gson gson=new Gson();
        String userPojoStr =gson.toJson(userPojo,UserPojo.class);
        try {
            MongoCollection mongoCollection=connMongoDB.getCollection("bbddb","user");
            Document document = Document.parse(userPojoStr);
            mongoCollection.insertOne(document);
            userPojo.setStatus(operateSuccess);
        }catch (Exception e){
            e.printStackTrace();
            userPojo.setStatus(operateFailure);
        }finally {
            connMongoDB.getMongoClient().close();
            resultStr= gson.toJson(userPojo,UserPojo.class);
            return resultStr;
        }
    }
}
