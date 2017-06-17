package com.heu.cs.dao.orderdao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.ReturnInfoPojo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Created by memgq on 2017/5/28.
 */
public class CancelOrderDao {
    private final String operateSuccess = "1";
    private final String operateFailure = "0";

    public String cancelOrder(String orderId) {
        ConnMongoDB connMongoDB = new ConnMongoDB();
        Gson gson = new Gson();
        String operateResult = "";
        ReturnInfoPojo returnInfoPojo = new ReturnInfoPojo();
        try {
            MongoCollection collection = connMongoDB.getCollection("bbddb", "normalorder");
            Document filter = new Document();
            filter.append("_id", new ObjectId(orderId));
            MongoCursor<Document> mongoCursor = collection.find(filter).iterator();
            if (mongoCursor.hasNext()) {

                Document d = mongoCursor.next();
                if (d.get("orderStatus").equals("0")) {
                    Document update = new Document();
                    update.append("$set", new Document("orderStatus", "-1"));
                    collection.updateOne(filter, update);


                    MongoCollection userCollection = connMongoDB.getCollection("bbddb", "user");
                    Document userFilter = new Document();
                    userFilter.append("userId", d.getString("orderOwnerId"));

                    MongoCursor<Document> userCursor = userCollection.find(userFilter).iterator();
                    int temp = -(int) (Double.parseDouble(d.get("orderPrice").toString()) * 10);
                    returnInfoPojo.setExp(String.valueOf(userCursor.next().getInteger("experience") + temp));
                    userCursor.close();


                    Document userUpdate = new Document();
                    userUpdate.append("experience", temp);
                    userCollection.findOneAndUpdate(userFilter, new Document("$inc", userUpdate));

                    returnInfoPojo.setStatus(operateSuccess);
                    returnInfoPojo.setMessage("取消成功");
                    connMongoDB.getMongoClient().close();

                } else {
                    returnInfoPojo.setStatus(operateFailure);
                    returnInfoPojo.setMessage("无法取消");
                }

            }
            mongoCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            returnInfoPojo.setStatus(operateFailure);
            returnInfoPojo.setMessage("出现异常");
        } finally {
            connMongoDB.getMongoClient().close();
            operateResult = gson.toJson(returnInfoPojo, ReturnInfoPojo.class);
            return operateResult;
        }

    }
}
