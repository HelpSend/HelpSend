package com.heu.cs.api;

import com.heu.cs.dao.userdao.GetExpByIdDao;
import com.heu.cs.dao.userdao.QuickLoginDao;
import com.heu.cs.dao.userdao.SendQuickLoginCodeDao;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;

/**
 * Created by memgq on 2017/6/3.
 */
@Path("/user")
public class UserApi {



    @Context
    UriInfo uriInfo;

    @Context
    HttpServletRequest request;

    @GET
    @Produces("text/plain;charset=utf-8")
    public String index(){
        return "OK";
    }


    @GET
    @Path("/sendquicklogincode")
    @Produces("text/plain;charset=utf-8")
    public String sendQuickLoginCode(@QueryParam("telNumber") String telNumber) throws IOException {
        SendQuickLoginCodeDao sendCode=new SendQuickLoginCodeDao();
        String resultStr=sendCode.sendQuickLoginCode(telNumber);
        return resultStr;
    }

    @GET
    @Path("/quicklogin")
    @Produces("text/plain;charset=utf-8")
    public String quickLogin(@QueryParam("telNumber") String telNumber) throws IOException {
        QuickLoginDao quickLoginDao=new QuickLoginDao();
        String resultStr=quickLoginDao.quickLogin(telNumber);
        return resultStr;
    }

    @GET
    @Path("/getexpbyuserid")
    @Produces("text/plain;charset=utf-8")
    public String getExpByUserId(@QueryParam("userId") String userId) throws IOException {
        GetExpByIdDao getExpByIdDao=new GetExpByIdDao();
        String resultStr=getExpByIdDao.getExpById(userId);
        return resultStr;
    }





}
