package com.heu.cs.dao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.*;
import com.mongodb.MongoClientException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by memgq on 2017/5/28.
 */
public class GrabOrderDao {
    private static final String operateSuccess = "1";
    private static final  String operateFailure = "0";
    private static final  double EARTH_RADIUS = 6378137;//赤道半径(单位m)
    private static final SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String grabOrder(String latitude,String longitude) {
        System.out.println(latitude+":"+longitude);
        ArrayList<GrabOrderPojo> grabOrderPojoArrayList=new ArrayList<GrabOrderPojo>();
        String returnRes="";
        Gson gson=new Gson();
        ReturnInfoPojo returnInfo=new ReturnInfoPojo();
        try{
            ConnMongoDB connMongoDB=new ConnMongoDB();
            MongoCollection collection = connMongoDB.getCollection("bbddb", "normalorder");
            Document document = new Document();
            document.append("orderStatus", "0");
            Document sortDocument=new Document();
            sortDocument.append("sendTime",1);
            FindIterable<Document> findIterable= collection.find(document).sort(sortDocument).limit(20);
            if (findIterable != null) {
                for (Document d : findIterable) {
                    OrderPojo order = gson.fromJson(d.toJson(), OrderPojo.class);
                    GrabOrderPojo grabOrderPojo=new GrabOrderPojo();
                    String receiveTimeStamp=parseData(order.getReceiveTime());
                    Date nowTime=new Date();
                    String nowTimeStamp=nowTime.getTime()/1000+"";
                    if(Integer.parseInt(receiveTimeStamp)>Integer.parseInt(nowTimeStamp)){
                        grabOrderPojo.setSendTime(formatSendTime(order.getSendTime(),nowTimeStamp));
                        grabOrderPojo.setReceiveTime(formatReceiveTime(order.getReceiveTime())+" 之前");
                        grabOrderPojo.setOrderId(d.get("_id").toString());
                        String[] goodsProp= order.getGoodsName().split(" - ");
                        grabOrderPojo.setGoodsCategory(goodsProp[0]);
                        grabOrderPojo.setGoodsWeight(goodsProp[1]);
                        grabOrderPojo.setEndLocationPojo(order.getEndLocation());
                        grabOrderPojo.setStartLocationPojo(order.getStartLocation());
                        grabOrderPojo.setOrderPrice(order.getOrderPrice());
                        grabOrderPojo.setOrderOwnerId(order.getOrderOwnerId());
                        String distance=getDistance(latitude,longitude,order.getStartLocation().getLatitude(),order.getStartLocation().getLongitude());
                        grabOrderPojo.setDistance(distance+" 公里");
                        String price=setPrice(distance);
                        grabOrderPojo.setOrderPrice(price);
                        grabOrderPojoArrayList.add(grabOrderPojo);
                    }else{
                        CancelOrderDao cancelOrderDao=new CancelOrderDao();
                        String cancelOrderResult=cancelOrderDao.cancelOrder(d.get("_id").toString());
                    }
                }
                connMongoDB.getMongoClient().close();
                GrabOrderResponsePojo grabOrderResponsePojo=new GrabOrderResponsePojo();
                grabOrderResponsePojo.setStatus(operateSuccess);
                grabOrderResponsePojo.setMessage(grabOrderPojoArrayList);
                returnRes=gson.toJson(grabOrderResponsePojo,GrabOrderResponsePojo.class);
            }
            return returnRes;

        }catch (MongoClientException e){
            e.printStackTrace();
            returnInfo.setStatus(operateFailure);
            returnInfo.setMessage("MongoClient Error");
            returnRes=gson.toJson(returnInfo,ReturnInfoPojo.class);
            return returnRes;
        } catch (Exception e) {
            e.printStackTrace();
            returnInfo.setStatus(operateFailure);
            returnInfo.setMessage("解析错误");
            returnRes=gson.toJson(returnInfo,ReturnInfoPojo.class);
            return returnRes;
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


    public String setPrice(String diatance){
        Double dis=Double.parseDouble(diatance);
        Double pr;
        if(dis<=1.5){
            pr=2.0;
        }else if(dis>1.5&&dis<3){
            pr=2.0+(dis-1.5)*0.7;
        }else {
            pr=2.0+(dis-1.5)*1.4;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return String.valueOf(df.format(pr))+" 元";
    }
}
