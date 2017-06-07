package com.heu.cs.generalmethod;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;

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
        if(document.get("orderId").equals("")){
        Document update = new Document();
        update.append("$set", new Document("orderId",document.get("_id").toString()));
        collection.updateOne(document, update);
        }
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
     * 根据距离设定价格
     * @param diatance
     * @return
     */
    @Override
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
}
