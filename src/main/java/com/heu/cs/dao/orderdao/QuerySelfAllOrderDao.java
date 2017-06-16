package com.heu.cs.dao.orderdao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.Order.OrderPojo;
import com.heu.cs.pojo.Order.QueryOrderResponsePojo;
import com.heu.cs.pojo.ReturnInfoPojo;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;

/**
 * Created by memgq on 2017/5/28.
 */
public class QuerySelfAllOrderDao {
    private  final String operateSuccess = "1";
    private  final  String operateFailure = "0";

    public String querySelfAllOrder(String orderOwnerId){
        Gson gson=new Gson();
        ReturnInfoPojo returnInfo=new ReturnInfoPojo();
        String result="";
        ArrayList<OrderPojo> res=new ArrayList<OrderPojo>();
        ConnMongoDB connMongoDB=new ConnMongoDB();
        try{
            MongoCollection collection = connMongoDB.getCollection("bbddb", "normalorder");
            Document document=new Document();
            document.append("orderOwnerId",orderOwnerId);
            Document sortDocument=new Document();
            sortDocument.append("orderStatus",-1);
            FindIterable<Document> findIterable=collection.find(document).sort(sortDocument);
            for(Document d:findIterable){

                OrderPojo order = gson.fromJson(d.toJson(), OrderPojo.class);
                res.add(order);
            }

            QueryOrderResponsePojo queryOrderResponse=new QueryOrderResponsePojo();
            queryOrderResponse.setStatus(operateSuccess);
            queryOrderResponse.setMessage(res);
            result=gson.toJson(queryOrderResponse,QueryOrderResponsePojo.class);
        }catch (MongoException e){
            e.printStackTrace();
            returnInfo.setStatus(operateFailure);
            returnInfo.setMessage("数据查询失败");
            result=gson.toJson(returnInfo,ReturnInfoPojo.class);
        }catch (Exception e){
            e.printStackTrace();
            returnInfo.setStatus(operateFailure);
            returnInfo.setMessage("操作失败");
            result=gson.toJson(returnInfo,ReturnInfoPojo.class);
        }finally {
            connMongoDB.getMongoClient().close();
            return result;
        }
    }
}
