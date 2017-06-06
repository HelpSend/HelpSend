package com.heu.cs.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * Created by memgq on 2017/6/3.
 */
@Path("/vrfcode")
public class VerificationCodeService {


    @Context
    UriInfo uriInfo;

    @Context
    HttpServletRequest request;

    @GET
    @Produces("text/plain;charset=utf-8")
    public String index(){
        return "OK";
    }




    @POST
    @Path("/getreplysms")
    @Produces("text/plain;charset=utf-8")
    public String sendQuickLoginCode(@QueryParam("sms_reply") String sms_reply){
        String resultStr="";
        return sms_reply;
    }
}
