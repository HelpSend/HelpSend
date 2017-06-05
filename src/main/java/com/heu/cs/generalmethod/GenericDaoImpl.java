package com.heu.cs.generalmethod;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by memgq on 2017/6/5.
 */
public class GenericDaoImpl implements GenericDao {
    public final DateTimeFormatter formatDateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public String getTimeDif(String targetTime){
        DateTime dateTime=new DateTime();
        Long nowTimestamp=dateTime.getMillis();
        long putOrderTimestamp=getTimestamp(targetTime);
        Long dis=(nowTimestamp/1000-putOrderTimestamp/1000);
        if(dis<60){
            targetTime=dis+" 秒钟前";
        }else if(dis>=60&&dis<=3600){
            targetTime=dis/60+" 分钟前";
        }else {
            targetTime=targetTime;
        }
        return targetTime;
    }


    @Override
    public long getTimestamp(String t){
        DateTime dateTime= formatDateTime.parseDateTime(t);
        return dateTime.getMillis();
    }

    @Override
    public void updateOrderId(Document document, MongoCollection collection){
        if(document.get("orderId").equals("1234")){
        Document update = new Document();
        update.append("$set", new Document("orderId",document.get("_id").toString()));
        collection.updateOne(document, update);
        }
    }
}
