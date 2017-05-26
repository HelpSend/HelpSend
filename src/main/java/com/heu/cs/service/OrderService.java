package com.heu.cs.service;


import com.google.gson.Gson;
import com.heu.cs.dao.QueryNewOrderDao;
import com.heu.cs.dao.ReceiveOrderDao;
import com.heu.cs.pojo.ReturnInfoPojo;
import com.heu.cs.dao.CreateOrderDao;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by memgq on 2017/5/14.
 */
@Path("/order")
public class OrderService {

    /**
     * Constants operating with images
     */
    private static final String ROOTPATH = System.getProperty("user.dir");
    private static final String ARTICLE_IMAGES_PATH = "/src/main/resources/upload_images/";
    private static final String JPG_CONTENT_TYPE = "image/jpeg";
    private static final String PNG_CONTENT_TYPE = "image/png";
    private String result = "";
    private String status = "";


    @GET
    @Produces({"text/html"})
    public String index() {
        return "OK";
    }


    @GET
    @Path("/receiveorder;charset=utf-8")
//    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String receiveOrderURL(@QueryParam("orderId") String orderId,
                                  @QueryParam("orderReceiverId") String orderReceiverId) {
        ReceiveOrderDao receiveOrderDao = new ReceiveOrderDao();
        status = receiveOrderDao.receiveOrder(orderId, orderReceiverId);
        ReturnInfoPojo returnInfoPojo=new ReturnInfoPojo();
        returnInfoPojo.setStatus(status);
        returnInfoPojo.setMessage("接单成功");
        Gson gson=new Gson();
        return gson.toJson(returnInfoPojo,ReturnInfoPojo.class);
    }


    @POST
    @Path("/createorder1")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("text/plain;charset=utf-8")
    public String cteateOrderURL1(@FormDataParam("photos") InputStream fileInputStream,
                                 @FormDataParam("photos") FormDataContentDisposition disposition,
                                 @FormDataParam("orderinfo") String orderInfoStr) {
        ReturnInfoPojo returnInfo = new ReturnInfoPojo();
        CreateOrderDao createOrderDao = new CreateOrderDao();
        String imageName = disposition.getFileName();
        Gson gson = new Gson();
        if (!imageName.equals("")) {
            imageName = Calendar.getInstance().getTimeInMillis() + imageName;
            status = createOrderDao.insertOrder(orderInfoStr, ARTICLE_IMAGES_PATH + imageName);
            File file = new File(ROOTPATH + ARTICLE_IMAGES_PATH + imageName);
            try {
                //使用common io的文件写入操作
                FileUtils.copyInputStreamToFile(fileInputStream, file);
                returnInfo.setStatus(status);
                returnInfo.setMessage("图片上传成功，下单成功");
                return gson.toJson(returnInfo);
            } catch (IOException ex) {
                Logger.getLogger(UploadFileService.class.getName()).log(Level.SEVERE, null, ex);
                returnInfo.setStatus("0");
                returnInfo.setMessage("IOError");
                return gson.toJson(returnInfo);
            }
        } else {
            status = createOrderDao.insertOrder(orderInfoStr, ARTICLE_IMAGES_PATH);
            returnInfo.setStatus(status);
            returnInfo.setMessage("下单成功");
            return gson.toJson(returnInfo);
        }
    }


    @POST
    @Path("/createorder")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("text/plain")
    public String cteateOrderURL(@FormDataParam("photos") InputStream fileInputStream,
                                 @FormDataParam("photos") FormDataContentDisposition disposition,
                                 @FormDataParam("orderinfo") String orderInfoStr) throws UnsupportedEncodingException {
        ReturnInfoPojo returnInfo = new ReturnInfoPojo();
        CreateOrderDao createOrderDao = new CreateOrderDao();
        System.out.println(orderInfoStr);
        String imageName = URLDecoder.decode(disposition.getFileName(),"UTF-8");
        Gson gson = new Gson();
        if (!imageName.equals("")) {
            imageName = Calendar.getInstance().getTimeInMillis() + imageName;
            System.out.println(imageName);
            status = createOrderDao.insertOrder(orderInfoStr, ARTICLE_IMAGES_PATH + imageName);
            File file = new File(ROOTPATH + ARTICLE_IMAGES_PATH + imageName);
            try {
                //使用common io的文件写入操作
                FileUtils.copyInputStreamToFile(fileInputStream, file);
                returnInfo.setStatus(status);
                returnInfo.setMessage("图片上传成功，下单成功");
                return gson.toJson(returnInfo);
            } catch (IOException ex) {
                Logger.getLogger(UploadFileService.class.getName()).log(Level.SEVERE, null, ex);
                returnInfo.setStatus("0");
                returnInfo.setMessage("IOError");
                return gson.toJson(returnInfo);
            }
        } else {
            status = createOrderDao.insertOrder(orderInfoStr, ARTICLE_IMAGES_PATH);
            returnInfo.setStatus(status);
            returnInfo.setMessage("下单成功");
            return gson.toJson(returnInfo);
        }
    }


    @GET
    @Path("/queryneworder")
//    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("text/plain;charset=utf-8")
    public String queryNewOrderURL(@QueryParam("orderOwnerId") String orderOwnerId) {
        ReturnInfoPojo returnInfo = new ReturnInfoPojo();
        QueryNewOrderDao queryNewOrderDao =new QueryNewOrderDao();
        result=queryNewOrderDao.queryNewOrder(orderOwnerId);
        return result;
    }
}
