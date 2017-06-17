package com.heu.cs.api;

import com.google.gson.Gson;
import com.heu.cs.dao.IdentifyDao.QueryIdentifyStatusDao;
import com.heu.cs.dao.orderdao.CreateOrderDao;

import com.heu.cs.pojo.ReturnInfoPojo;
import com.heu.cs.pojo.User.TextInfoPojo;
import com.heu.cs.utils.*;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by memgq on 2017/6/16.
 */
@Path("/identify")
public class IdentifyApi {



    @Context
    UriInfo uriInfo;

    @Context
    HttpServletRequest request;

    @GET
    @Produces("text/plain;charset=utf-8")
    public String index() {
        return "OK";
    }

    @POST
    @Path("/getidentifyinfo")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("text/plain;charset=utf-8")
    public void cteateOrderWithImgURL(@FormDataParam("textInfo") String textInfo,
                                        FormDataMultiPart formDataMultiPart) {
        List<FormDataBodyPart> list= formDataMultiPart.getFields("file");

        UploadFile uploadFile=new UploadFileImpl();
        String ROOTPATH = System.getProperty("user.dir");
        String RESOURCE_DIR="/src/main/identify_images/";
        String imageDir=ROOTPATH+RESOURCE_DIR;
        String[] identifyImagesPathList=new String[3];
        String[] nameList=new String[3];
        for(int i=0;i<list.size();i++){
            FormDataBodyPart part=list.get(i);
            String fileName=part.getContentDisposition().getFileName();
            InputStream inputStream=part.getValueAs(InputStream.class);
            uploadFile.uploadAndCompressImage(inputStream,imageDir,fileName);
            identifyImagesPathList[i]=imageDir+fileName;
            nameList[i]=fileName;
        }
        TencentYouTu tencentYouTu=new TencentYouTuImpl();
        tencentYouTu.ocrIdCard(textInfo,identifyImagesPathList,nameList);
    }

    /**
     * 根据id查询认证状态
     * @param userId
     * @return  -1：未认证，1：已认证，0：认证中
     */
    @GET
    @Path("/queryidentifystatus")
    @Produces("text/plain;charset=utf-8")
    public String getIdCardUri(@QueryParam("userId") String userId) {
        QueryIdentifyStatusDao queryIdentifyStatusDao=new QueryIdentifyStatusDao();
        return queryIdentifyStatusDao.queryIdentifyStatu(userId);
    }



}