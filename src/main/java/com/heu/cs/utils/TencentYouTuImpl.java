package com.heu.cs.utils;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.pojo.Identify.IdCardBackResListPojo;
import com.heu.cs.pojo.Identify.IdCardBackResultPojo;
import com.heu.cs.pojo.Identify.IdCardFaceResListPojo;
import com.heu.cs.pojo.Identify.IdCardFaceResultPojo;
import com.heu.cs.pojo.PornDetecResultListPojo;
import com.heu.cs.pojo.PornDetectDataPojo;
import com.heu.cs.pojo.PornDetectResultPojo;
import com.heu.cs.pojo.ReturnInfoPojo;
import com.heu.cs.pojo.User.TextInfoPojo;
import com.heu.cs.service.image.ImageClient;
import com.heu.cs.service.image.common_utils.CommonFileUtils;
import com.heu.cs.service.image.demo.Demo;
import com.heu.cs.service.image.request.FaceCompareRequest;
import com.heu.cs.service.image.request.FaceIdCardCompareRequest;
import com.heu.cs.service.image.request.IdcardDetectRequest;
import com.heu.cs.service.image.request.PornDetectRequest;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by memgq on 2017/6/7.
 */
public class TencentYouTuImpl implements TencentYouTu {

    @Override
    public String detectPorn(String url) {
        String[] keylist=getAPPKey();
        ImageClient imageClient =new ImageClient( Integer.parseInt(keylist[0]), keylist[1], keylist[2]);
        String[] pornUrlList = new String[1];
        pornUrlList[0]=url;
        PornDetectRequest pornReq = new PornDetectRequest(keylist[3], pornUrlList);
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
        imageClient.shutdown();
        return status;
    }



    @Override
    public String ocrIdCard(String textInfo, String[] idcardPathList,String[] nameList) {
        Gson gson = new Gson();
        TextInfoPojo textInfoPojo = gson.fromJson(textInfo, TextInfoPojo.class);
        String[] keylist = getAPPKey();
        ImageClient imageClient = new ImageClient(Integer.parseInt(keylist[0]), keylist[1], keylist[2]);
        System.out.println("====================================================");
        //2. 图片内容方式,识别身份证正面 0有照片，1国徽
        String[] idcardNameList = new String[1];
        String[] idcardImageList = new String[1];
        try {
            idcardNameList[0] = nameList[0];
            idcardImageList[0] = CommonFileUtils.getFileContent(idcardPathList[0]);
        } catch (Exception ex) {
            Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
        }
        IdcardDetectRequest idReq = new IdcardDetectRequest(keylist[3], idcardNameList, idcardImageList, 0);
        String ret = imageClient.idcardDetect(idReq);
        System.out.println("idcard detect ret:" + ret);
        IdCardFaceResListPojo idCardFaceResListPojo=gson.fromJson(ret,IdCardFaceResListPojo.class);
        IdCardFaceResultPojo idCardFaceResultPojo= idCardFaceResListPojo.getResult_list().get(0);
        System.out.println("====================================================");
        //识别身份证反面
        try {
            idcardNameList[0] = nameList[1];
            idcardImageList[0] = CommonFileUtils.getFileContent(idcardPathList[1]);
        } catch (Exception ex) {
            Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
        }
        idReq = new IdcardDetectRequest(keylist[3], idcardNameList, idcardImageList, 1);
        ret = imageClient.idcardDetect(idReq);
        IdCardBackResListPojo idCardBackResListPojo=gson.fromJson(ret,IdCardBackResListPojo.class);
        IdCardBackResultPojo idCardBackResultPojo=idCardBackResListPojo.getResult_list().get(0);
        System.out.println("idcard detect ret:" + ret);
        if(isValidDate(idCardBackResultPojo.getData().getValid_date())) {
            if (idCardFaceResultPojo.getData().getName().equals(textInfoPojo.getName()) &&
                    idCardFaceResultPojo.getData().getId().equals(textInfoPojo.getIdCard())) {
                insertTextInfo(textInfoPojo, textInfo,idcardPathList);
            }
        }
        return "0";
    }



    private void insertTextInfo( TextInfoPojo textInfoPojo,String textInfo,String[] idcardPathList){
        ConnMongoDB connMongoDB=new ConnMongoDB();
        MongoCollection collection=connMongoDB.getCollection("bbddb","identify");
        Document filter=new Document("userId",textInfoPojo.getUserId());
        Document update=Document.parse(textInfo);
        update.append("status","0").append("idCardPathList",idcardPathList);
        collection.updateOne(filter,new Document("$set",update));
        connMongoDB.getMongoClient().close();
    }


    private String[] getAPPKey() {
        ConnMongoDB connMongoDB=new ConnMongoDB();
        MongoCollection collection = connMongoDB.getCollection("bbddb", "secret");
        MongoCursor<Document> cursor = collection.find(new Document("info", "tencentyoutu")).iterator();
        Document document = cursor.next();
        String[] keylist = new String[4];
        keylist[0] = document.get("appId").toString();
        keylist[1] = document.get("secretId").toString();
        keylist[2] = document.get("secretKey").toString();
        keylist[3] = document.get("bucketName").toString();
        cursor.close();
        connMongoDB.getMongoClient().close();
        return keylist;
    }

    public boolean isValidDate(String valid_date){
        valid_date=valid_date.split("-")[1];
        GenericMethod genericMethod=new GenericMethodImpl();
        valid_date=valid_date.replace(".","-");
        long mi=genericMethod.getTimestamp(valid_date+" 00:00:00");
        DateTime now=new DateTime();
        if(mi>now.getMillis()){
            return true;
        }else {
            return false;
        }
    }



//    public void faceCompare(){
//        ConnMongoDB connMongoDB=new ConnMongoDB();
//        String[] keylist = getAPPKey(connMongoDB);
//        ImageClient imageClient = new ImageClient(Integer.parseInt(keylist[0]), keylist[1], keylist[2]);
//        System.out.println("====================================================");
//        String[] compareNameList = new String[2];
//        String[] compareImageList = new String[2];
//        try {
//            compareNameList[0] = "face.jpg";
//            compareNameList[1] = "face3.jpg";
//            compareImageList[0] = CommonFileUtils.getFileContent("C:\\Users\\memgq\\Desktop\\porntest\\face.jpg");
//            compareImageList[1] = CommonFileUtils.getFileContent("C:\\Users\\memgq\\Desktop\\porntest\\face3.jpg");
//        } catch (Exception ex) {
//            Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        FaceCompareRequest  faceCompareReq = new FaceCompareRequest(keylist[3], compareNameList, compareImageList);
//        String ret = imageClient.faceCompare(faceCompareReq);
//        System.out.println("face compare ret:" + ret);
//    }
}
