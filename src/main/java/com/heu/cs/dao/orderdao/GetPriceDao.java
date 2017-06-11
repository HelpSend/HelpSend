package com.heu.cs.dao.orderdao;

import com.google.gson.Gson;
import com.heu.cs.pojo.GetPricePojo;
import com.heu.cs.pojo.LocationPojo;
import com.heu.cs.pojo.ReturnInfoPojo;
import com.heu.cs.utils.GenericMethod;
import com.heu.cs.utils.GenericMethodImpl;

import java.text.DecimalFormat;

/**
 * Created by memgq on 2017/6/7.
 */
public class GetPriceDao {
    public String getPrice(String location){

        Gson gson=new Gson();
        GetPricePojo getPricePojo=gson.fromJson(location,GetPricePojo.class);
        LocationPojo startLocation=getPricePojo.getStart();
        LocationPojo endLocation=getPricePojo.getEnd();
        ReturnInfoPojo returnInfoPojo=new ReturnInfoPojo();
        if(!startLocation.getLatitude().equals("")&&!endLocation.getLatitude().equals("")){
            String startLatitude=startLocation.getLatitude();
            String startLongitude=startLocation.getLongitude();
            String endtLatitude=endLocation.getLatitude();
            String endLongitude=endLocation.getLongitude();
            GenericMethod genericMethod =new GenericMethodImpl();
            String result= genericMethod.getPrice(startLatitude,startLongitude,endtLatitude,endLongitude);
            //String dis=genericMethod.getDistance(startLatitude,startLongitude,endtLatitude,endLongitude);
            returnInfoPojo.setStatus(result);
            DecimalFormat df = new DecimalFormat("0.0");
            if(Double.parseDouble(result)>4.0){
                returnInfoPojo.setMessage("基础价格 4 元\n里程加价 "+(df.format(Double.parseDouble(result)-4))+" 元");
            }else {
                returnInfoPojo.setMessage("基础价格 4 元");
            }
        }
        else {
            returnInfoPojo.setMessage("0");
            returnInfoPojo.setMessage("");
        }
        return gson.toJson(returnInfoPojo,ReturnInfoPojo.class);
    }
}
