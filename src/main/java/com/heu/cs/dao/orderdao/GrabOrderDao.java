package com.heu.cs.dao.orderdao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.utils.GenericDao;
import com.heu.cs.utils.GenericDaoImpl;
import com.heu.cs.pojo.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by memgq on 2017/5/28.
 */
public class GrabOrderDao {
    private final String operateSuccess = "1";
    private final String operateFailure = "0";
    private final double NEARBY=50;
    private final String formatDateStr="yyyy-MM-dd";
    private final String timezero=" 23:59:59";

    public String grabOrder(String latitude,String longitude) throws ParseException {
        ArrayList<GrabOrderPojo> grabOrderPojoList=new ArrayList<GrabOrderPojo>();
        String returnRes="";
        Gson gson=new Gson();
        ReturnInfoPojo returnInfo=new ReturnInfoPojo();
        List<OrderPojo> orderPojoList= getOrderList();
        if(orderPojoList.size()>0) {
            GenericDao genericDao =new GenericDaoImpl();
            for (OrderPojo order : orderPojoList) {
                String distance = genericDao.getDistance(latitude, longitude, order.getStartLocation().getLatitude(), order.getStartLocation().getLongitude());
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
                    grabOrderPojo.setOrderPrice(order.getOrderPrice());
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





    /**
     * 设置显示的期望货物送达时间
     * @param orderReceiveTime
     * @return
     * @throws ParseException
     */
    public String formatReceiveTime(String orderReceiveTime,DateTime nowDateTime,GenericDao genericDao) throws ParseException {
        String returnTime="";
        String[] tlist=orderReceiveTime.split(" ");
        Long receiveTimeStamp= genericDao.getTimestamp(orderReceiveTime);
        String todayStr= nowDateTime.toString(formatDateStr);
        long todayTimeStamp= genericDao.getTimestamp(todayStr+timezero);
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
    private String formatSendTime(String orderSendTime,Long nowTimestamp,GenericDao genericDao) {
        String sendTime="";
        Long sendTimestamp= genericDao.getTimestamp(orderSendTime);
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
            GenericDao genericDao =new GenericDaoImpl();
            for(Document d:findIterable){
                OrderPojo order = gson.fromJson(d.toJson(), OrderPojo.class);
                genericDao.updateOrderId(d,collection);
                long receiveTimeStamp= genericDao.getTimestamp(order.getReceiveTime());
                if(receiveTimeStamp>nowTimestamp){
                    order.setOrderId(d.get("_id").toString());
                    order.setSendTime(formatSendTime(order.getSendTime(),nowTimestamp, genericDao));
                    order.setReceiveTime(formatReceiveTime(order.getReceiveTime(),nowDateTime, genericDao)+" 之前");
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
