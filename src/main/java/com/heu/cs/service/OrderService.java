package com.heu.cs.service;


import com.google.gson.Gson;
import com.heu.cs.dao.*;
import com.heu.cs.pojo.ReturnInfoPojo;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    private static final String ROOT_IMAGES_PATH = "/src/main/resources";
    private static final String JPG_CONTENT_TYPE = "image/jpeg";
    private static final String PNG_CONTENT_TYPE = "image/png";
    private static final String IMAGE_URL="/upload_images/";



    @GET
    @Produces({"text/html"})
    public String index() {
        return "OK";
    }


    /**
     * 接单，通过订单ID和接单人ID来确定接单
     * @param orderId
     * @param orderReceiverId
     * @return
     */
    @GET
    @Path("/receiveorder;charset=utf-8")
//    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String receiveOrderURL(@QueryParam("orderId") String orderId,
                                  @QueryParam("orderReceiverId") String orderReceiverId) {
        ReceiveOrderDao receiveOrderDao = new ReceiveOrderDao();
        String status = receiveOrderDao.receiveOrder(orderId, orderReceiverId);
        ReturnInfoPojo returnInfoPojo=new ReturnInfoPojo();
        returnInfoPojo.setStatus(status);
        returnInfoPojo.setMessage("接单成功");
        Gson gson=new Gson();
        return gson.toJson(returnInfoPojo,ReturnInfoPojo.class);
    }




    /**
     * 下单，可选择是否上传图片
     * @param fileInputStream
     * @param disposition
     * @param orderInfoStr
     * @return
     */
    @POST
    @Path("/createorder")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("text/plain")
    public String cteateOrderURL(@FormDataParam("photos") InputStream fileInputStream,
                                 @FormDataParam("photos") FormDataContentDisposition disposition,
                                 @FormDataParam("orderinfo") String orderInfoStr){
        ReturnInfoPojo returnInfo = new ReturnInfoPojo();
        CreateOrderDao createOrderDao = new CreateOrderDao();
        System.out.println(orderInfoStr);
        String imageName = disposition.getFileName();
        Gson gson = new Gson();
        if (!imageName.equals("")) {
            imageName = Calendar.getInstance().getTimeInMillis() + imageName;
            System.out.println(imageName);
            String status = createOrderDao.insertOrder(orderInfoStr, IMAGE_URL + imageName);
            File file = new File(ROOTPATH + ROOT_IMAGES_PATH+IMAGE_URL+ imageName);
            try {
                //使用common io的文件写入操作
                FileUtils.copyInputStreamToFile(fileInputStream, file);
                returnInfo.setStatus(status);
                returnInfo.setMessage("下单成功");
                return gson.toJson(returnInfo);
            } catch (IOException ex) {
                Logger.getLogger(UploadFileService.class.getName()).log(Level.SEVERE, null, ex);
                returnInfo.setStatus("0");
                returnInfo.setMessage("IOError");
                return gson.toJson(returnInfo);
            }
        } else {
            String status = createOrderDao.insertOrder(orderInfoStr, IMAGE_URL);
            returnInfo.setStatus(status);
            returnInfo.setMessage("下单成功");
            return gson.toJson(returnInfo);
        }
    }


    /**
     * 通过状态码来查询自己的不同状态的订单
     * @param orderOwnerId
     * @param orderStatus 0:已下单 1:已接单 2:已完成 -1:已取消
     * @return
     */
    @GET
    @Path("/queryselforderbystatus")
    @Produces("text/plain;charset=utf-8")
    public String queryOrderByURL(@QueryParam("orderOwnerId") String orderOwnerId,
                                   @QueryParam("orderStatus") String orderStatus) {
        QueryOrderByOrderOwnerDao queryOrderByOrderOwnerDao =new QueryOrderByOrderOwnerDao();
        String result= queryOrderByOrderOwnerDao.queryNewOrder(orderOwnerId,orderStatus);
        return result;
    }

    /**
     * 查询自己发出的所有订单
     * @param orderOwnerId
     * @return
     */
    @GET
    @Path("/queryselfallorder")
    @Produces("text/plain;charset=utf-8")
    public String queryNewOrderByStatusURL(@QueryParam("orderOwnerId") String orderOwnerId) {
        QuerySelfAllOrderDao querySelfAllOrderDao=new QuerySelfAllOrderDao();
        String result=querySelfAllOrderDao.querySelfAllOrder(orderOwnerId);
        return result;
    }

    /**
     * 根据订单Id取消订单
     * @param orderId
     * @return
     */
    @GET
    @Path("/cancelorder")
    @Produces("text/plain;charset=utf-8")
    public String cancelOrderURL(@QueryParam("orderId") String orderId) {
       Gson gson=new Gson();
        CancelOrderDao cancelOrderDao=new CancelOrderDao();
       String result=cancelOrderDao.cancelOrder(orderId);
       ReturnInfoPojo returnInfoPojo=new ReturnInfoPojo();
       returnInfoPojo.setStatus(result);
       returnInfoPojo.setMessage("已取消订单");
       return gson.toJson(returnInfoPojo,ReturnInfoPojo.class);
    }


    /**
     * 抢单，查询附近所有的没人接的订单，并按照期望配送员取物品时间降序排列
     * @return
     */
    @GET
    @Path("/graborder")
    @Produces("text/plain;charset=utf-8")
    public String grabOrderURL(@QueryParam("latitude") String latitude,@QueryParam("longitude") String longitude){
        GrabOrderDao grabOrderDao=new GrabOrderDao();
        String result=grabOrderDao.grabOrder(latitude,longitude);
        return result;
    }

    @GET
    @Path("/graborderdetails")
    @Produces("text/plain;charset=utf-8")
    public String grabOrderDetailsURL(@QueryParam("orderId") String orderId){
        GrabOrderDetailsDao grabOrderDetailsDao=new GrabOrderDetailsDao();
        String result=grabOrderDetailsDao.grabOrderDetails(orderId);
        return result;
    }


}
