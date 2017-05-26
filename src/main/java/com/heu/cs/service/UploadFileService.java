package com.heu.cs.service;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;


/**
 * Created by memgq on 2017/5/22.
 */

@Path("/uploadfile")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.TEXT_PLAIN)
public class UploadFileService {

    /**
     * Constants operating with images
     */
    private static final String ROOTPATH=System.getProperty("user.dir");
    private static final String ARTICLE_IMAGES_PATH = "/upload_images/";
    private static final String JPG_CONTENT_TYPE = "image/jpeg";
    private static final String PNG_CONTENT_TYPE = "image/png";


    /**
     * 第一种方式上传
     *
     * @param fileInputStream
     * @param disposition
     * @return
     */
    @POST
    @Path("/uploadimages")
    public String uploadimage(@FormDataParam("file") InputStream fileInputStream,
                              @FormDataParam("file") FormDataContentDisposition disposition,
                              @FormDataParam("filename") String filename) {
        String imageName = Calendar.getInstance().getTimeInMillis()
                + disposition.getFileName();
//        System.out.println(filename);
        File file = new File(ROOTPATH+ARTICLE_IMAGES_PATH + imageName);
        System.out.println(ROOTPATH+ARTICLE_IMAGES_PATH + imageName);
        try {
            //使用common io的文件写入操作
            FileUtils.copyInputStreamToFile(fileInputStream, file);
            //原来自己的文件写入操作
            //saveFile(fileInputStream, file);
        } catch (IOException ex) {
            Logger.getLogger(UploadFileService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "images/" + imageName;
    }

}

