package com.heu.cs.dao.orderdao;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.heu.cs.pojo.OrderPojo;
import com.mongodb.client.MongoCollection;
import com.heu.cs.conndb.ConnMongoDB;
import org.bson.Document;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by memgq on 2017/5/17.
 */
public class CreateOrderDao {
    private  final String operateSuccess = "1";
    private  final  String operateFailure = "0";
    private  final String IMAGE_URL="/upload_images/";
    private  final String PROJECT_URL="http://mengqipoet.cn:8080";
    private final DateTimeFormatter formatDateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * @param orderStr json格式的字符串
     * @return 返回操作状态，成功或失败
     */
    public String insertOrder(String orderStr, String imagePath) {

        /**
         * 先转成jsonObject ,然后格式化看有没有漏掉的字段，有的话加上
         */
        String operateResult="";
        DateTime d=new DateTime();
        String dateNowStr = d.toString("yyyy-MM-dd HH:mm:ss");
        JsonObject obj = new JsonParser().parse(orderStr).getAsJsonObject();
        FormatOrderData formatOrderData = new FormatOrderData();
        obj = formatOrderData.format(obj);
        Gson gson = new Gson();
        OrderPojo order = gson.fromJson(obj, OrderPojo.class);
        order.setOrderStatus("0");
        order.setPutOrderTime(dateNowStr);
        if (imagePath.equals(IMAGE_URL)) {
            order.setImagePath("");
        } else {
            order.setImagePath(PROJECT_URL+imagePath);
        }
        orderStr = gson.toJson(order);
        ConnMongoDB connMongoDB = new ConnMongoDB();
        try {
            MongoCollection collection = connMongoDB.getCollection("bbddb", "normalorder");
            Document document = Document.parse(orderStr);
            collection.insertOne(document);
            operateResult=operateSuccess;

        } catch (Exception exception) {
            exception.printStackTrace();
            operateResult=operateFailure;
        }finally {
            connMongoDB.getMongoClient().close();
            return operateResult;
        }

    }
}
