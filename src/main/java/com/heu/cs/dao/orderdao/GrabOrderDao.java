package com.heu.cs.dao.orderdao;

import com.google.gson.Gson;
import com.heu.cs.cache.Group;
import com.heu.cs.cache.GroupCacheFactory;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.*;
import com.mongodb.MongoClientException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.bson.Document;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.heu.cs.timertask.Factory.groupGrabOrder;

/**
 * Created by memgq on 2017/5/28.
 */
public class GrabOrderDao {
    private final String operateSuccess = "1";
    private final String operateFailure = "0";
    private final double EARTH_RADIUS = 6378137;//赤道半径(单位m)
    private final double NEARBY=50;
    private final DateTimeFormatter formatDateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    private final String formatDateStr="yyyy-MM-dd";
    private final String timezero=" 23:59:59";

    public String grabOrder(String latitude,String longitude) throws ParseException {
        ArrayList<GrabOrderPojo> grabOrderPojoList=new ArrayList<GrabOrderPojo>();
        String returnRes="";
        Gson gson=new Gson();
        ReturnInfoPojo returnInfo=new ReturnInfoPojo();
        List<OrderPojo> orderPojoList= getOrderList();
        if(orderPojoList.size()>0) {
            for (OrderPojo order : orderPojoList) {
                String distance = getDistance(latitude, longitude, order.getStartLocation().getLatitude(), order.getStartLocation().getLongitude());
                if (Double.parseDouble(distance) < NEARBY) {
                    GrabOrderPojo grabOrderPojo = new GrabOrderPojo();
                    grabOrderPojo.setSendTime(order.getSendTime());
                    grabOrderPojo.setReceiveTime(order.getReceiveTime());
                    grabOrderPojo.setOrderId(order.getOrderId());
                    String[] goodsProp = order.getGoodsName().split(" - ");
                    grabOrderPojo.setGoodsCategory(goodsProp[0]);
                    grabOrderPojo.setGoodsWeight(goodsProp[1]);
                    grabOrderPojo.setEndLocationPojo(order.getEndLocation());
                    grabOrderPojo.setStartLocationPojo(order.getStartLocation());
                    grabOrderPojo.setOrderPrice(order.getOrderPrice());
                    grabOrderPojo.setOrderOwnerId(order.getOrderOwnerId());
                    grabOrderPojo.setDistance(distance + " 公里");
                    String price = setPrice(distance);
                    grabOrderPojo.setOrderPrice(price);
                    grabOrderPojoList.add(grabOrderPojo);
                    GrabOrderResponsePojo grabOrderResponsePojo = new GrabOrderResponsePojo();
                    grabOrderResponsePojo.setStatus(operateSuccess);
                    grabOrderResponsePojo.setMessage(grabOrderPojoList);
                    returnRes = gson.toJson(grabOrderResponsePojo, GrabOrderResponsePojo.class);
                }
            }
        }else {
            returnInfo.setStatus(operateFailure);
            returnInfo.setMessage("查询失败");
            returnRes=gson.toJson(returnInfo,ReturnInfoPojo.class);
        }
        return returnRes;

//        ArrayList<GrabOrderPojo> grabOrderPojoArrayList=new ArrayList<GrabOrderPojo>();
//        String returnRes="";
//        Gson gson=new Gson();
//        ReturnInfoPojo returnInfo=new ReturnInfoPojo();
//        List<String> keysList=groupGrabOrder.getKeys();
//        if(keysList.size()!=0){
//            String key=keysList.get(0);
//            List<OrderPojo> orderPojoList= (List<OrderPojo>) groupGrabOrder.getValue(key);
//            for(OrderPojo order:orderPojoList){
//                String distance=getDistance(latitude,longitude,order.getStartLocation().getLatitude(),order.getStartLocation().getLongitude());
//                if(Double.parseDouble(distance)<NEARBY){
//                    GrabOrderPojo grabOrderPojo=new GrabOrderPojo();
//                    grabOrderPojo.setSendTime(order.getSendTime());
//                    grabOrderPojo.setReceiveTime(order.getReceiveTime());
//                    grabOrderPojo.setOrderId(order.getOrderId());
//                    String[] goodsProp= order.getGoodsName().split(" - ");
//                    grabOrderPojo.setGoodsCategory(goodsProp[0]);
//                    grabOrderPojo.setGoodsWeight(goodsProp[1]);
//                    grabOrderPojo.setEndLocationPojo(order.getEndLocation());
//                    grabOrderPojo.setStartLocationPojo(order.getStartLocation());
//                    grabOrderPojo.setOrderPrice(order.getOrderPrice());
//                    grabOrderPojo.setOrderOwnerId(order.getOrderOwnerId());
//                    grabOrderPojo.setDistance(distance+" 公里");
//                    String price=setPrice(distance);
//                    grabOrderPojo.setOrderPrice(price);
//                    grabOrderPojoArrayList.add(grabOrderPojo);
//                    GrabOrderResponsePojo grabOrderResponsePojo=new GrabOrderResponsePojo();
//                    grabOrderResponsePojo.setStatus(operateSuccess);
//                    grabOrderResponsePojo.setMessage(grabOrderPojoArrayList);
//                    returnRes=gson.toJson(grabOrderResponsePojo,GrabOrderResponsePojo.class);
//                }
//            }
//        }else {
//            returnInfo.setStatus(operateFailure);
//            returnInfo.setMessage("查询失败");
//            returnRes=gson.toJson(returnInfo, ReturnInfoPojo.class);
//        }
//        return returnRes;

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



    /**
     *  转化为弧度(rad)
     * @param d
     * @return
     */
    private double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两个位置的经纬度，来计算两地的距离（单位为KM）
     * 参数为String类型
     * @param lat1Str 用户纬度
     * @param lng1Str 用户经度
     * @param lat2Str 商家纬度
     * @param lng2Str 商家经度
     * @return
     */
    private String  getDistance(String lat1Str, String lng1Str, String lat2Str, String lng2Str)
    {
        Double lat1 = Double.parseDouble(lat1Str);
        Double lng1 = Double.parseDouble(lng1Str);
        Double lat2 = Double.parseDouble(lat2Str);
        Double lng2 = Double.parseDouble(lng2Str);

        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        distance = distance * EARTH_RADIUS;
        //s = Math.round(s * 10000) / 10000;
        DecimalFormat df = new DecimalFormat("0.00");
        return String.valueOf(df.format(distance/1000));
    }


    /**
     * 根据距离设定价格
     * @param diatance
     * @return
     */
    public String setPrice(String diatance){
        Double dis=Double.parseDouble(diatance);
        Double pr;
        if(dis<=1.5){
            pr=2.0;
        }else if(dis>1.5&&dis<=3){
            pr=2.0+(dis-1.5)*0.7;
        }else {
            pr=2.0+(dis-1.5)*1.4;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return String.valueOf(df.format(pr))+" 元";
    }



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
                if(order.getOrderId().equals("1234")){
                    Document update = new Document();
                    update.append("$set", new Document("orderId",d.get("_id").toString()));
                    collection.updateOne(d, update);
                }
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

        }finally {
            connMongoDB.getMongoClient().close();
            return orderPojoList;
        }
    }
}
