package com.heu.cs.dao;

import com.google.gson.JsonObject;
import com.heu.cs.pojo.OrderPojo;

import java.util.List;

/**
 * Created by memgq on 2017/5/21.
 */
public class FormatOrderData {


    public JsonObject format(JsonObject obj) {
        OrderPojo orderPojo=new OrderPojo();
        List<String> attrsList= orderPojo.getAttributes();
        for(String s:attrsList){
            if(!obj.has(s)){
                if (s.equals("startLocation")){
                    JsonObject object =addLocation("45.776122","126.682277");
                    obj.add(s, object);
                }else if(s.equals("endLocation")){
                    JsonObject object =addLocation("45.771437","126.686118");
                    obj.add(s, object);
                }else {
                    obj.addProperty(s, "123");
                }
            }
        }
        return obj;
    }

    public JsonObject addLocation(String latitude,String longitude){
        JsonObject object = new JsonObject();
        object.addProperty("description", "测试位置");
        object.addProperty("latitude", latitude);
        object.addProperty("longitude", longitude);
        return object;
    }
}
