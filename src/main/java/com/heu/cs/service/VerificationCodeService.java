package com.heu.cs.service;

import com.heu.cs.dao.orderdao.GetUserReplySMSDao;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.*;
import java.net.URLDecoder;
import java.util.Enumeration;

/**
 * Created by memgq on 2017/6/3.
 */
@Path("/vrfcode")
public class VerificationCodeService {


    @Context
    UriInfo uriInfo;


    @GET
    @Produces("text/plain;charset=utf-8")
    public String index(){
        return "OK";
    }




    @POST
    @Path("/getreplysms")
    @Produces("text/plain;charset=utf-8")
    public String  sendCompleteCode(@FormParam("sms_reply") String sms_reply) throws IOException {
        GetUserReplySMSDao getUserReplySMSDao=new GetUserReplySMSDao();
        getUserReplySMSDao.getUserReplySMS(URLDecoder.decode(sms_reply,"UTF-8"));
        return "0";
    }








//
//
//
//        Enumeration<String> names=request.getAttributeNames();
//        String n="";
//        if(names.hasMoreElements()) {
//            n+="  +  "+names.nextElement();
//        }
//        String fileName = "C:\\test\\attrs.txt";
//
//        //使用这个构造函数时，如果存在kuka.txt文件，
//        //则先把这个文件给删除掉，然后创建新的kuka.txt
//        FileWriter writer = new FileWriter(fileName);
//        writer.write(n);
//        writer.close();
//
//        Enumeration<String> parameterNames=request.getParameterNames();
//        String pn="";
//        if(parameterNames.hasMoreElements()) {
//            pn+="  +  "+parameterNames.nextElement();
//        }
//        String fileName3 = "C:\\test\\pnames.txt";
//
//        //使用这个构造函数时，如果存在kuka.txt文件，
//        //则先把这个文件给删除掉，然后创建新的kuka.txt
//        FileWriter writer3 = new FileWriter(fileName3);
//        writer3.write(pn);
//        writer3.close();
//        String a=request.getParameter("sms_reply");
//        String b=URLDecoder.decode(a);
//        String fileName2 = "C:\\test\\sms.txt";
//        FileWriter writer2 = new FileWriter(fileName2);
//        writer2.write(a+"  \n  ++\n"+b+"\n+\n"+URLDecoder.decode(a,"UTF-8"));
//        writer2.close();
//            System.out.println("回复的信息处理：：：：：" + sms_reply);
//            String sms_replyDecode = URLDecoder.decode(sms_reply);
//            System.out.println("回复的信息处理：：：：：" + sms_replyDecode);
//            GetUserReplySMSDao getUserReplySMSDao = new GetUserReplySMSDao();
//            getUserReplySMSDao.getUserReplySMS(sms_replyDecode);
//



}
