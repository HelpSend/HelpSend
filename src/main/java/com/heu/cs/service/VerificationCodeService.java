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


}
