package com.heu.cs.dao.userdao;
import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.UserPojo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

/**
 * Created by memgq on 2017/5/17.
 */
public class QuickLoginDao {
    private final String operateSuccess="1";
    private final String operateFailure="0";


    public String quickLogin(String telNumber){
        String resultStr="";
        ConnMongoDB connMongoDB=new ConnMongoDB();
        Gson gson=new Gson();

        try{
            MongoCollection collection = connMongoDB.getCollection("bbddb", "user");
            Document document=new Document();
            document.append("telNumber",telNumber);
            FindIterable<Document> findIterable= collection.find(document);
            MongoCursor<Document> mongoCursor =findIterable.iterator();
            if(mongoCursor.hasNext()){
                Document d= mongoCursor.next();
                Document update = new Document();
                update.append("$set", new Document("userId",d.get("_id").toString()));
                collection.updateOne(d, update);
                UserPojo userPojo=gson.fromJson(d.toJson(), UserPojo.class);
                userPojo.setStatus(operateSuccess);
                userPojo.setUserId(d.get("_id").toString());
                resultStr=gson.toJson(userPojo,UserPojo.class);
            }else {
                CreateUserDao createUserDao =new CreateUserDao();
                resultStr=createUserDao.createUser(telNumber);
            }
            mongoCursor.close();
        }catch (Exception e){
            e.printStackTrace();
            UserPojo userPojo=new UserPojo();
            userPojo.setStatus(operateFailure);
            resultStr=gson.toJson(userPojo,UserPojo.class);
        }finally {
            connMongoDB.getMongoClient().close();
            return resultStr;
        }
    }



}
