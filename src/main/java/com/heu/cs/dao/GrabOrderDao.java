package com.heu.cs.dao;

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
    private static final String operateSuccess = "1";
    private static final  String operateFailure = "0";
    private static final  double EARTH_RADIUS = 6378137;//赤道半径(单位m)
    private static final SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");





    public String grabOrder(String latitude,String longitude) throws ParseException {
        System.out.println(latitude+":"+longitude);
        ArrayList<GrabOrderPojo> grabOrderPojoArrayList=new ArrayList<GrabOrderPojo>();
        String returnRes="";
        Gson gson=new Gson();
        ReturnInfoPojo returnInfo=new ReturnInfoPojo();
        List<String> keysList=groupGrabOrder.getKeys();
        if(keysList.size()!=0){
            String key=keysList.get(0);
            List<OrderPojo> orderPojoList= (List<OrderPojo>) groupGrabOrder.getValue(key);
            for(OrderPojo order:orderPojoList){
                GrabOrderPojo grabOrderPojo=new GrabOrderPojo();
                grabOrderPojo.setSendTime(order.getSendTime());
                grabOrderPojo.setReceiveTime(order.getReceiveTime());
                grabOrderPojo.setOrderId(order.getOrderId());
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
                GrabOrderResponsePojo grabOrderResponsePojo=new GrabOrderResponsePojo();
                grabOrderResponsePojo.setStatus(operateSuccess);
                grabOrderResponsePojo.setMessage(grabOrderPojoArrayList);
                returnRes=gson.toJson(grabOrderResponsePojo,GrabOrderResponsePojo.class);
            }
        }else {
            returnInfo.setStatus(operateFailure);
            returnInfo.setMessage("查询失败");
            returnRes=gson.toJson(returnInfo, ReturnInfoPojo.class);
        }
        return returnRes;
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
