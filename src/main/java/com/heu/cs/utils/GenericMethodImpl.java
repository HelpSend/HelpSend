package com.heu.cs.utils;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;

/**
 * Created by memgq on 2017/6/5.
 */
public class GenericMethodImpl implements GenericMethod {
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
        if(!t.equals("")&&!t.isEmpty()){
        DateTime dateTime= formatDateTime.parseDateTime(t);
            return dateTime.getMillis();
        }else {
            return 1497516839;
        }

    }

    @Override
    public void updateOrderId(Document document, MongoCollection collection){
        if(document.get("orderId").equals("")){
        Document update = new Document();
        update.append("$set", new Document("orderId",document.get("_id").toString()));
        collection.updateOne(document, update);
        }
    }



    @Override
    public String formatTelNumber(String telNumber){
        telNumber=telNumber.replace(" ","");
        telNumber=telNumber.replace("-","");
        telNumber=telNumber.substring(telNumber.length()-11,telNumber.length());
        return telNumber;
    }



    private final double EARTH_RADIUS = 6378137;//赤道半径(单位m)
    /**
     *  转化为弧度(rad)
     * @param d
     * @return
     */
    @Override
    public double rad(double d)
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
    @Override
    public String  getDistance(String lat1Str, String lng1Str, String lat2Str, String lng2Str)
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
     * 通过当前时间获取此时为白天还是夜间,早上5点到晚上23点均属于白天，晚上23点到凌晨5点属于夜间
     * @param time
     * @return
     */
    @Override
    public String getDayOrNight(DateTime time) {
        String date = "";//返回值：白天或夜间
        String dateTime = time.toString("HH:mm:ss");
        int dateHour = Integer.parseInt(dateTime.substring(0,2));
        if(dateHour>=5&&dateHour<23){
            date = "白天";
        }else{
            date = "夜间";
        }
        return date;
    }

    /**
     * 根据两点经纬度算出的距离获取非加急价格
     * @param lat1Str 用户纬度
     * @param lng1Str 用户经度
     * @param lat2Str 商家纬度
     * @param lng2Str 商家经度
     * @return
     */
    @Override
    public String getPrice(String lat1Str, String lng1Str, String lat2Str, String lng2Str){
        Double totalPrice;//总费用
        final Double basicPrice = 4.0 ;//基价
        final Double firstSinglePrice = 2.4 ;//2公里至8公里的行驶单价
        final Double secondSinglePrice = 3.6 ;//8公里以上的行驶单价
        final Double nightPrice = 0.8 ;//夜间额外增加的行驶单价
        Double dis=Double.parseDouble(getDistance(lat1Str,lng1Str,lat2Str,lng2Str));//公里数
        DateTime time = new DateTime();
        String date = getDayOrNight(time);//返回白天或夜间

        if(date.equals("白天")){
            if(dis>=0.0&&dis<1.0){
                totalPrice = basicPrice ;
            }else if(dis>=1.0&&dis<8.0){
                totalPrice = basicPrice + (dis-1)*firstSinglePrice ;
            }else{
                totalPrice = basicPrice + 7.0*firstSinglePrice + (dis-8)*secondSinglePrice ;
            }
        }else{
            if(dis>=0.0&&dis<1.0){
                totalPrice = basicPrice + 1*nightPrice ;
            }else if(dis>=1.0&&dis<8.0){
                totalPrice = basicPrice + (dis-1)*(firstSinglePrice+nightPrice);
            }else{
                totalPrice = basicPrice + 7.0*(firstSinglePrice+nightPrice) + (dis-8)*(firstSinglePrice+nightPrice) ;
            }
        }
        DecimalFormat df = new DecimalFormat("0.0");
        return String.valueOf(df.format(totalPrice));
    }

    /**
     * 根据距离获取加急价格
     * @return
     */
    @Override
    public String getUrgentPrice(String lat1Str, String lng1Str, String lat2Str, String lng2Str) {
        String price = getPrice(lat1Str,lng1Str,lat2Str,lng2Str);//获取非加急价钱
        Double urgentPrice = Double.parseDouble(price)*1.3;//加急费用
        DecimalFormat df = new DecimalFormat("0.0");
        return String.valueOf(df.format(urgentPrice));
    }
}
