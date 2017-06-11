package com.heu.cs.utils;

import com.google.gson.Gson;
import com.heu.cs.pojo.PornDetecResultListPojo;
import com.heu.cs.pojo.PornDetectDataPojo;
import com.heu.cs.pojo.PornDetectResultPojo;
import com.heu.cs.service.image.ImageClient;
import com.heu.cs.service.image.request.PornDetectRequest;

import java.util.ArrayList;

/**
 * Created by memgq on 2017/6/7.
 */
public class TencentYouTuImpl implements TencentYouTu {

    @Override
    public String detectPorn(String url, ImageClient imageClient,String bucketName) {
        String[] pornUrlList = new String[1];
        pornUrlList[0]=url;
        PornDetectRequest pornReq = new PornDetectRequest(bucketName, pornUrlList);
        String ret = imageClient.pornDetect(pornReq);
        System.out.println(ret);
        Gson gson=new Gson();
        String status="";
        PornDetecResultListPojo resultPojoList=gson.fromJson(ret,PornDetecResultListPojo.class);
        ArrayList<PornDetectResultPojo> resultPojo=resultPojoList.getResult_list();

        for(PornDetectResultPojo result:resultPojo){
            if(result.getCode()==0){
                PornDetectDataPojo dataPojo=result.getData();
                if(dataPojo.getResult()==0){
                    status="0";
                }else {
                    status="1";
                }
            }else {
                status="1";
            }
        }
        return status;
    }
}
