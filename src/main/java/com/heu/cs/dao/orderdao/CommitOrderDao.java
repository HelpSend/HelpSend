package com.heu.cs.dao.orderdao;


import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.ReturnInfoPojo;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * Created by memgq on 2017/6/10.
 */
public class CommitOrderDao {
    public String commitOrder(double commit,String orderId){
        ConnMongoDB connMongoDB=new ConnMongoDB();
        MongoCollection collection= connMongoDB.getCollection("bbddb","normalorder");
        Document filter=new Document();
        filter.append("orderId",orderId);
        Document update=new Document();
        update.append("$set",new Document().append("commit",String.valueOf(commit)));
        ReturnInfoPojo returnInfoPojo=new ReturnInfoPojo();
        try {
            collection.findOneAndUpdate(filter,update);
            returnInfoPojo.setStatus("1");
            returnInfoPojo.setMessage("评价成功");
        } catch (Exception e){
            e.printStackTrace();
            returnInfoPojo.setStatus("0");
            returnInfoPojo.setMessage("评价失败");
        }
        return new Gson().toJson(returnInfoPojo,ReturnInfoPojo.class);
    }
}
