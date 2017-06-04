package com.heu.cs.timertask;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.dao.orderdao.CancelOrderDao;
import com.heu.cs.pojo.OrderPojo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by memgq on 2017/6/1.
 */
public class GrabOrderTask {
    private final DateTimeFormatter formatDateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    private final String formatDateStr="yyyy-MM-dd";
    private final String timezero=" 23:59:59";

    public List<OrderPojo> getOrderList(){
        Gson gson=new Gson();
        List<OrderPojo> orderPojoList=new ArrayList<OrderPojo>();
        ConnMongoDB connMongoDB=new ConnMongoDB();
        FindIterable<Document> findIterable;
        DateTime nowDateTime=new DateTime();
        Long nowTimestamp=nowDateTime.getMillis();
        try{
            MongoCollection collection = connMongoDB.getCollection("bbddb", "normalorder");
            Document document = new Document();
            document.append("orderStatus", "0");
            Document sortDocument=new Document();
            sortDocument.append("sendTime",1);
            findIterable = collection.find(document).sort(sortDocument).limit(20);
            for(Document d:findIterable){
                OrderPojo order = gson.fromJson(d.toJson(), OrderPojo.class);
                long receiveTimeStamp=getTimestamp(order.getReceiveTime());
                if(receiveTimeStamp>nowTimestamp){
                    order.setOrderId(d.get("_id").toString());
                    order.setSendTime(formatSendTime(order.getSendTime(),nowTimestamp));
                    order.setReceiveTime(formatReceiveTime(order.getReceiveTime(),nowDateTime)+" 之前");
                    orderPojoList.add(order);
                }else{
                    CancelOrderDao cancelOrderDao=new CancelOrderDao();
                    String cancelOrderResult=cancelOrderDao.cancelOrder(d.get("_id").toString());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            getOrderList();
        }finally {
            connMongoDB.getMongoClient().close();
            return orderPojoList;
        }
    }






    public long getTimestamp(String t){
        DateTime dateTime= formatDateTime.parseDateTime(t);
        return dateTime.getMillis();
    }

    /**
     * 设置显示的期望货物送达时间
     * @param orderReceiveTime
     * @return
     * @throws ParseException
     */
    public String formatReceiveTime(String orderReceiveTime,DateTime nowDateTime) throws ParseException {
        String returnTime="";
        String[] tlist=orderReceiveTime.split(" ");
        Long receiveTimeStamp= getTimestamp(orderReceiveTime);
        String todayStr= nowDateTime.toString(formatDateStr);
        long todayTimeStamp=getTimestamp(todayStr+timezero);
        if(receiveTimeStamp<=todayTimeStamp&&receiveTimeStamp>nowDateTime.getMillis()){
            returnTime="今天 "+tlist[1];
        }else if (receiveTimeStamp<=todayTimeStamp+24*60*60&&receiveTimeStamp>todayTimeStamp){
            returnTime="明天 "+tlist[1];
        }else if (receiveTimeStamp<=todayTimeStamp+48*60*60&&receiveTimeStamp>todayTimeStamp+24*60*60){
            returnTime="后天 "+tlist[1];
        }else {
            returnTime=orderReceiveTime;
        }
        return returnTime;

    }



    /**
     * 设置显示的取货时间
     * @param orderSendTime
     * @param nowTimestamp
     * @return
     * @throws ParseException
     */
    private String formatSendTime(String orderSendTime,Long nowTimestamp) {
        String sendTime="";
        Long sendTimestamp=getTimestamp(orderSendTime);
        long r= sendTimestamp-nowTimestamp;
        if(r<=20*60){
            sendTime="立即";
        }else if (r<=30*60&&r>20*60){
            sendTime="10分钟内";
        }else if (r<=40*60&&r>30*60){
            sendTime="20分钟内";
        }else if (r<=50*60&&r>40*60){
            sendTime="30分钟内";
        }else if (r<=60*60&&r>50*60){
            sendTime="40分钟内";
        }else if (r<=70*60&&r>60*60){
            sendTime="50分钟内";
        }else if (r<=90*60&&r>70*60){
            sendTime="1小时内";
        }else if (r<=120*60&&r>90*60){
            sendTime="1.5小时内";
        }else if (r<=150*60&&r>120*60){
            sendTime="2小时内";
        }else if (r<=180*60&&r>150*60){
            sendTime="2.5小时内";
        }else if (r<=210*60&&r>180*60){
            sendTime="3小时内";
        }else {
            sendTime= orderSendTime;
        }
        return sendTime;
    }


}
