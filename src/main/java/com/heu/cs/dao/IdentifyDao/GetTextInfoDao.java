package com.heu.cs.dao.IdentifyDao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.ReturnInfoPojo;
import com.mongodb.client.MongoCollection;
import org.bson.Document;


/**
 * Created by memgq on 2017/6/16.
 */
public class GetTextInfoDao {
    public String getTextInfo(String userId,String textInfo){
        ConnMongoDB connMongoDB=new ConnMongoDB();
        MongoCollection collection=connMongoDB.getCollection("bbddb","identify");
        Document filter=new Document("userId",userId);
        Document document=Document.parse(textInfo);
        collection.findOneAndUpdate(filter,document);
        connMongoDB.getMongoClient().close();
        ReturnInfoPojo returnInfoPojo=new ReturnInfoPojo();
        returnInfoPojo.setStatus("1");
        returnInfoPojo.setMessage("上传成功");
        Gson gson=new Gson();
        return gson.toJson(returnInfoPojo,ReturnInfoPojo.class);
    }

}
