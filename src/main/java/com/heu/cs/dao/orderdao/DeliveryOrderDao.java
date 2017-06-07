package com.heu.cs.dao.orderdao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.generalmethod.GenerateVerificationCode;
import com.heu.cs.generalmethod.GenerateVerificationCodeImpl;
import com.heu.cs.generalmethod.SMSApiDaoImpl;
import com.heu.cs.pojo.ReturnInfoPojo;
import com.heu.cs.pojo.VrfCodeResponsePojo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.joda.time.DateTime;

import java.io.IOException;

/**
 * Created by memgq on 2017/6/6.
 */
public class DeliveryOrderDao {
    public String deliveryOrder(String orderId) throws IOException {

        Gson gson = new Gson();
        ConnMongoDB connMongoDB = new ConnMongoDB();
        MongoCollection collection = connMongoDB.getCollection("bbddb", "normalorder");
        Document d = new Document();
        d.append("orderId", orderId);
        MongoCursor<Document> cursor = collection.find(d).iterator();
        Document document = cursor.next();
        String mobile = document.getString("receiverTel");
        String goodsName=document.getString("goodsName");
        //发送验证码
        GenerateVerificationCode generateVerificationCode = new GenerateVerificationCodeImpl();
        String replyCode = generateVerificationCode.generateCode(6);
        String text ="【帮帮带】物品["+goodsName+"]已送达，请回复验证码"+replyCode+"完成此订单。";
        SMSApiDaoImpl smsApiDaoImpl = new SMSApiDaoImpl();
        String replymsg = smsApiDaoImpl.sendSms(text, mobile);
        VrfCodeResponsePojo responsePojo = gson.fromJson(replymsg, VrfCodeResponsePojo.class);
        ReturnInfoPojo returnInfoPojo=new ReturnInfoPojo();
        if (responsePojo.getCode().equals(0)) {
            Document update = new Document();
            Document newValue = new Document();
            DateTime dateTime = new DateTime();
            newValue.append("deliveryTime", dateTime.toString("yyyy-MM-dd HH:mm:ss"))
                    .append("orderReplyCode", replyCode);
            update.append("$set", newValue);
            collection.updateOne(document, update);
            cursor.close();
            returnInfoPojo.setStatus("1");
            returnInfoPojo.setMessage("发送成功");
        }else {
            returnInfoPojo.setStatus("0");
            returnInfoPojo.setMessage("发送失败");
        }
        connMongoDB.getMongoClient().close();
        return gson.toJson(returnInfoPojo,ReturnInfoPojo.class);
    }
}
