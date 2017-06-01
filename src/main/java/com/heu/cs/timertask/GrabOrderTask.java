package com.heu.cs.timertask;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.dao.CancelOrderDao;
import com.heu.cs.pojo.OrderPojo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by memgq on 2017/6/1.
 */
public class GrabOrderTask {
    public List<OrderPojo> getOrderList(){
        Gson gson=new Gson();
        List<OrderPojo> orderPojoList=new ArrayList<OrderPojo>();
        ConnMongoDB connMongoDB=new ConnMongoDB();
        FindIterable<Document> findIterable;
        Date nowTime=new Date();
        String nowTimeStamp=nowTime.getTime()/1000+"";
        try{
            MongoCollection collection = connMongoDB.getCollection("bbddb", "normalorder");
            Document document = new Document();
            document.append("orderStatus", "0");
            Document sortDocument=new Document();
            sortDocument.append("sendTime",1);
            findIterable = collection.find(document).sort(sortDocument).limit(20);
            for(Document d:findIterable){
                OrderPojo order = gson.fromJson(d.toJson(), OrderPojo.class);
                String receiveTimeStamp=parseData(order.getReceiveTime());
                if(Integer.parseInt(receiveTimeStamp)>Integer.parseInt(nowTimeStamp)){
                    order.setOrderId(d.get("_id").toString());
                    order.setSendTime(formatSendTime(order.getSendTime(),nowTimeStamp));
                    order.setReceiveTime(formatReceiveTime(order.getReceiveTime())+" 之前");
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



    /**
     * 设置显示的期望货物送达时间
     * @param t
     * @return
     * @throws ParseException
     */
    public String formatReceiveTime(String t) throws ParseException {
        String returnTime="";
        String timezero=" 23:59:59";
        String[] tlist=t.split(" ");
        String receiveTimeStamp= parseData(t);
        Calendar now = Calendar.getInstance();
        String today=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH);
        String todayTimeStamp=parseData(today+timezero);
        int todayTimeInt= Integer.parseInt(todayTimeStamp);
        int receiveTimeInt=Integer.parseInt(receiveTimeStamp);
        if(receiveTimeInt<=todayTimeInt&&receiveTimeInt>now.getTimeInMillis()/1000){
            returnTime="今天 "+tlist[1];
        }else if (receiveTimeInt<=todayTimeInt+24*60*60&&receiveTimeInt>todayTimeInt){
            returnTime="明天 "+tlist[1];
        }else if (receiveTimeInt<=todayTimeInt+48*60*60&&receiveTimeInt>todayTimeInt+24*60*60){
            returnTime="后天 "+tlist[1];
        }else {
            returnTime=t;
        }
        return returnTime;

    }


    /**
     * 将字符串日期转成时间戳
     * @param t
     * @return
     * @throws ParseException
     */
    public String parseData(String t) throws ParseException {
        SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d=simpleDateFormat.parse(t);
        return d.getTime()/1000+"";
    }

    /**
     * 设置显示的取货时间
     * @param orderSendTime
     * @param nowTimeStamp
     * @return
     * @throws ParseException
     */
    private String formatSendTime(String orderSendTime,String nowTimeStamp) throws ParseException {
        String sendTime="";
        String sendTimeStamp=parseData(orderSendTime);
        int r= Integer.parseInt(sendTimeStamp)-Integer.parseInt(nowTimeStamp);
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
