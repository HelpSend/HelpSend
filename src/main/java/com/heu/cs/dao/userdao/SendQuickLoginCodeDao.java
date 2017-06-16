package com.heu.cs.dao.userdao;

import com.google.gson.Gson;
import com.heu.cs.conndb.ConnMongoDB;
import com.heu.cs.utils.GenerateVerificationCode;
import com.heu.cs.utils.GenerateVerificationCodeImpl;
import com.heu.cs.utils.SMSApiDaoImpl;
import com.heu.cs.utils.SMSApiDao;
import com.heu.cs.pojo.User.VrfCodeResponsePojo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.io.IOException;

/**
 * Created by memgq on 2017/6/3.
 */
public class SendQuickLoginCodeDao {
    private final String operateSuccess="1";
    private final String operateFailure="0";
    private final String TEXT="【帮帮带】正在进行登录操作，您的验证码是";
    private final String SUCCESSMSG="发送成功";
    private final String FAILUREMSG="操作失败";

    public String sendQuickLoginCode(String telNumber) throws IOException {
        preCreateUser(telNumber);
        String resultStr="";
        GenerateVerificationCode generateVerificationCode =new GenerateVerificationCodeImpl();
        String verificationCode= generateVerificationCode.generateCode(4);
        String textTemplate=TEXT+verificationCode;
        SMSApiDao smsApiDao =new SMSApiDaoImpl();
        String returnMsg= smsApiDao.sendSms(textTemplate,telNumber);
        Gson gson=new Gson();
        VrfCodeResponsePojo vrfCodeResponsePojo=gson.fromJson(returnMsg,VrfCodeResponsePojo.class);
        if(vrfCodeResponsePojo.getCode().equals(0)){
            vrfCodeResponsePojo.setStatus(operateSuccess);
            vrfCodeResponsePojo.setVrfCode(verificationCode);
            vrfCodeResponsePojo.setMessage(SUCCESSMSG);
        }else {
            vrfCodeResponsePojo.setStatus(operateFailure);
            vrfCodeResponsePojo.setMessage(FAILUREMSG);
        }
        resultStr=gson.toJson(vrfCodeResponsePojo,VrfCodeResponsePojo.class);
        return resultStr;
    }

    private void preCreateUser(String telNumber){
        ConnMongoDB connMongoDB=new ConnMongoDB();
        MongoCollection collection = connMongoDB.getCollection("bbddb", "user");
        Document document=new Document();
        document.append("telNumber",telNumber);
        FindIterable<Document> findIterable= collection.find(document);
        MongoCursor<Document> mongoCursor =findIterable.iterator();
        if(!mongoCursor.hasNext()){
            CreateUserDao createUserDao =new CreateUserDao();
            String s=createUserDao.createUser(telNumber);
        }
        mongoCursor.close();
        connMongoDB.getMongoClient().close();
    }
}
